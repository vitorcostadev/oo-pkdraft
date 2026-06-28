package domain.facade;

import domain.builder.PokemonBuilder;
import domain.exceptions.BadJsonFormatException;
import domain.models.battle.Pokemon;
import domain.models.battle.Treinador;
import domain.models.pokemon.*;
import domain.strategy.HeuristicStrategy;
import utils.JsonValue;
import utils.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Facade que cria uma ‘interface’ única para ser utilizada pelo restante do programa.
 */
public class JsonLocalFacade implements PokemonDadosFacade {

    private final List<PokemonBuilder> repositorioBase;

    public JsonLocalFacade() throws IOException {
        this.repositorioBase = new ArrayList<>();
        carregarDadosDoDisco();
    }

    private void carregarDadosDoDisco() throws IOException{
        processarJson(lerArquivoSeguro("pokemons_base.json"));
    }

    private String lerArquivoSeguro(String pathname) throws IOException {
        return new String(Files.readAllBytes(Paths.get(pathname)));
    }

    private void processarJson(String jsonBruto) throws BadJsonFormatException{
        this.repositorioBase.clear();
        List<String> blocos = Parser.separarObjetos(jsonBruto)
                .orElseThrow(() -> new BadJsonFormatException("Não foi possível separar os objetos do json."));

        for (String bloco : blocos) {
            JsonValue pokeAst = Parser.parsePokemonToAst(bloco);
            this.repositorioBase.add(astToBuilder(pokeAst));
        }

        if (this.repositorioBase.isEmpty()) {
            carregarDadosDeContingencia();
        }
    }

    private PokemonBuilder astToBuilder(JsonValue noPokemon) {
        Map<String, JsonValue> mapaPoke = noPokemon.asObject();
        String nome = mapaPoke.get("nome").asString();
        Tipo t1 = normalizarTipo(mapaPoke.get("t1").asString());
        int anInt = ThreadLocalRandom.current().nextInt(0,
                Natureza.values().length - 1);

        JsonValue t2No = mapaPoke.get("t2");
        Tipo t2 = (t2No == null || t2No instanceof JsonValue.JNull || t2No.asString().isEmpty() || "null".equalsIgnoreCase(t2No.asString()))
                ? null
                : normalizarTipo(t2No.asString());

        Estatisticas stats = new Estatisticas(
                mapaPoke.get("hp").asInt(),
                mapaPoke.get("atk").asInt(),
                mapaPoke.get("def").asInt(),
                mapaPoke.get("spa").asInt(),
                mapaPoke.get("spd").asInt(),
                mapaPoke.get("spe").asInt()
        );

        PokemonBuilder builder = criarMolde(nome, t1, t2, stats).setNatureza(Natureza.values()[anInt]);

        for (int mSlot = 1; mSlot <= 4; mSlot++) {
            JsonValue moveNo = mapaPoke.get("m" + mSlot);
            if (moveNo != null && !(moveNo instanceof JsonValue.JNull)) {
                Map<String, JsonValue> mFields = moveNo.asObject();
                builder.adicionarMovimento(new Movimento(
                        mFields.get("name").asString(),
                        normalizarTipo(mFields.get("type").asString()),
                        mFields.get("power").asInt(),
                        CategoriaMovimento.fromString(mFields.get("category").asString())
                ));
            }
        }

        return builder;
    }

    @Override
    public List<PokemonBuilder> obterTresOpcoesAleatorias() {
        if (this.repositorioBase.isEmpty()) {
            carregarDadosDeContingencia();
        }

        List<PokemonBuilder> copia = new ArrayList<>(this.repositorioBase);
        Collections.shuffle(copia, new Random());


        List<PokemonBuilder> selecionados = new ArrayList<>();
        for (int i = 0; i < 3 && i < copia.size(); i++) {
            //selecionados.add(copia.get(i));
            int temp = new Pokemon(copia.get(i))
                    .sumBst();

            if(temp >= 700) selecionados.add(copia.get(i));
        }

        return selecionados;
    }

    @Override
    public List<Treinador> carregarLigaDesafiantes(String regiao) {
        List<Treinador> liga = new ArrayList<>();

        try {
            String m = lerArquivoSeguro("league_data.json");

            List<String> separar = Parser.separarObjetos(m)
                    .orElseThrow(() -> new BadJsonFormatException("Um erro ocorreu na tentativa de separar os objetos do json."));

            JsonValue astRaiz = Parser.parseLeagueToAst(separar);
            Map<String, JsonValue> mapaLiga = astRaiz.asObject();

            for (int i = 0; i < astRaiz.size(); i++) {
                JsonValue trainerVal = mapaLiga.get(String.valueOf(i + 1));
                Map<String, JsonValue> trainerMap = trainerVal.asObject();
                Treinador treinador = new Treinador(trainerMap.get("nome").asString(), new HeuristicStrategy());
                Map<String, JsonValue> timeMap = trainerMap.get("time").asObject();

                for (int pSlot = 1; pSlot <= 6; pSlot++) {
                    JsonValue pokeNo = timeMap.get(String.valueOf(pSlot));
                    if (pokeNo != null && !(pokeNo instanceof JsonValue.JNull)) {
                        treinador.adicionarPokemon(astToBuilder(pokeNo).build());
                    }
                }
                liga.add(treinador);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Não foi possível encontrar o league_data.json, assumindo dados de contigencia.");
        } catch(IOException e){
            System.err.println("Um erro inesperado ocorreu na tentativa de carregar o league_data.json");
        } finally{
            if(liga.isEmpty()) liga = gerarLigaContingencia();
        }

        return liga;
    }

    private List<Treinador> gerarLigaContingencia() {
        List<Treinador> demergencia = new ArrayList<>();
        Treinador t = new Treinador("Campeao Blue", new HeuristicStrategy());
        t.adicionarPokemon(criarMolde("Blastoise", Tipo.AGUA, null, new Estatisticas(79, 83, 100, 85, 105, 78))
                .adicionarMovimento(new Movimento("Hydro Pump", Tipo.AGUA, 110, CategoriaMovimento.ESPECIAL)).build());
        demergencia.add(t);
        return demergencia;
    }

    private Tipo normalizarTipo(String tipoBruto) {
        if (tipoBruto == null || tipoBruto.isBlank() || "null".equalsIgnoreCase(tipoBruto)) {
            return null;
        }
        return switch (tipoBruto.trim().toUpperCase()) {
            case "PLANTA"  -> Tipo.GRAMA;
            case "VENENO"  -> Tipo.VENENOSO;
            case "SOMBRIO" -> Tipo.NOTURNO;
            default -> Tipo.valueOf(tipoBruto.trim().toUpperCase());
        };
    }

    private void carregarDadosDeContingencia() {
        this.repositorioBase.clear();
        this.repositorioBase.add(criarMolde("Charizard", Tipo.FOGO, Tipo.VOADOR, new Estatisticas(78, 84, 78, 109, 85, 100))
                .adicionarMovimento(new Movimento("Flamethrower", Tipo.FOGO, 90, CategoriaMovimento.ESPECIAL)));
    }

    private PokemonBuilder criarMolde(String nome, Tipo t1, Tipo t2, Estatisticas base) {
        PokemonBuilder builder = new PokemonBuilder();
        builder.setDadosBase(nome, t1, t2);
        builder.setEstatisticasPadrao(base);
        return builder;
    }
}