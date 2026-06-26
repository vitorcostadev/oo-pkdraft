package integration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import domain.models.CategoriaMovimento;
import domain.models.Estatisticas;
import domain.strategy.HeuristicStrategy;
import domain.models.Movimento;
import domain.models.Natureza;
import domain.PokemonBuilder;
import domain.models.Tipo;
import domain.models.Treinador;

import static utils.Parser.*;

/**
 * Implementa a fachada de dados realizando a leitura e processamento seguro de arquivos locais.
 */
public class JsonLocalFacade implements PokemonDadosFacade {

    private final List<PokemonBuilder> repositorioBase;
    private final List<String> keysInt = List.of("hp", "atk", "def", "spa", "spd", "spe");
    private final List<String> keysStr = List.of("nome", "t1", "t2");

    public JsonLocalFacade() {
        this.repositorioBase = new ArrayList<>();
        carregarDadosDoDisco();
    }

    private void carregarDadosDoDisco() {
        try {
            String conteudo = new String(Files.readAllBytes(Paths.get("pokemons_base.json")));
            processarJson(conteudo);
        } catch (Exception e) {
            carregarDadosDeContingencia();
        }
    }

    private Movimento extrairMovimento(String json) {
        String nome = extrairTexto(json, "name")
                .orElseThrow(() -> new IllegalArgumentException("Campo 'name' nao encontrado"));

        Tipo tipo = Tipo.valueOf(extrairTexto(json, "type")
                .orElseThrow(() -> new IllegalArgumentException("Campo 'type' nao encontrado")));

        int power = Integer.parseInt(extrairTexto(json, "power")
                .orElseThrow(() -> new IllegalArgumentException("Campo 'power' nao encontrado")));

        String p = extrairTexto(json, "category")
                .orElseThrow(() -> new IllegalArgumentException("Campo 'category' nao encontrado"));

        return new Movimento(nome, tipo, power, CategoriaMovimento.fromString(p));
    }

    private void processarJson(String jsonBruto) {
        repositorioBase.clear();

        List<String> blocos = separarObjetos(jsonBruto)
                .orElseThrow(() -> new RuntimeException("Objetos nao encontrados."));

        for (String conteudo : blocos) {
            Map<String, String> sMap = new HashMap<>();
            Map<String, Integer> iMap = new LinkedHashMap<>();

            for (String k : keysStr) {
                sMap.put(k, extrairTexto(conteudo, k).orElse(null));
            }

            for (String k : keysInt) {
                iMap.put(k, Integer.parseInt(extrairTexto(conteudo, k)
                        .orElseThrow(() -> new RuntimeException("Campo '" + k + "' nao encontrado"))));
            }

            String m1 = extrairObjeto(conteudo, "m1").orElseThrow(() -> new RuntimeException("m1 ausente"));
            String m2 = extrairObjeto(conteudo, "m2").orElseThrow(() -> new RuntimeException("m2 ausente"));
            String m3 = extrairObjeto(conteudo, "m3").orElseThrow(() -> new RuntimeException("m3 ausente"));
            String m4 = extrairObjeto(conteudo, "m4").orElseThrow(() -> new RuntimeException("m4 ausente"));

            Tipo t1 = Tipo.valueOf(sMap.get("t1"));
            Tipo t2 = sMap.get("t2") == null ? null : Tipo.valueOf(sMap.get("t2"));

            Estatisticas stats = new Estatisticas(iMap.get("hp"), iMap.get("atk"), iMap.get("def"), iMap.get("spa"), iMap.get("spd"), iMap.get("spe"));

            PokemonBuilder builder = new PokemonBuilder()
                    .setDadosBase(sMap.get("nome"), t1, t2)
                    .setNatureza(Natureza.HARDY)
                    .setEstatisticasPadrao(stats)
                    .adicionarMovimento(extrairMovimento(m1))
                    .adicionarMovimento(extrairMovimento(m2))
                    .adicionarMovimento(extrairMovimento(m3))
                    .adicionarMovimento(extrairMovimento(m4));

            repositorioBase.add(builder);
        }

        if (repositorioBase.isEmpty()) {
            carregarDadosDeContingencia();
        }
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
            selecionados.add(copia.get(i));
        }

        return selecionados;
    }

    @Override
    public List<Treinador> carregarLigaDesafiantes(String regiao) {
        List<Treinador> liga = new ArrayList<>();

        Treinador lorelei = new Treinador("Lorelei (Elite 4)", new HeuristicStrategy());
        lorelei.adicionarPokemon(criarMolde("Lapras", Tipo.AGUA, Tipo.GELO, new Estatisticas(130, 85, 80, 85, 95, 60))
                .setNatureza(Natureza.MODEST)
                .adicionarMovimento(new Movimento("Ice Beam", Tipo.GELO, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Surf", Tipo.AGUA, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Thunderbolt", Tipo.ELETRICO, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Psychic", Tipo.PSIQUICO, 90, CategoriaMovimento.ESPECIAL)).build());
        lorelei.adicionarPokemon(criarMolde("Cloyster", Tipo.AGUA, Tipo.GELO, new Estatisticas(50, 95, 180, 85, 45, 70))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Icicle Crash", Tipo.GELO, 85, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Hydro Pump", Tipo.AGUA, 110, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Poison Jab", Tipo.VENENOSO, 80, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Explosion", Tipo.NORMAL, 250, CategoriaMovimento.FISICO)).build());

        Treinador bruno = new Treinador("Bruno (Elite 4)", new HeuristicStrategy());
        bruno.adicionarPokemon(criarMolde("Machamp", Tipo.LUTADOR, null, new Estatisticas(90, 130, 80, 65, 85, 55))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Dynamic Punch", Tipo.LUTADOR, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Stone Edge", Tipo.PEDRA, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Knock Off", Tipo.NOTURNO, 65, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO)).build());
        bruno.adicionarPokemon(criarMolde("Onix", Tipo.PEDRA, Tipo.TERRA, new Estatisticas(35, 45, 160, 30, 45, 70))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Rock Slide", Tipo.PEDRA, 75, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Iron Tail", Tipo.ACO, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Double-Edge", Tipo.NORMAL, 120, CategoriaMovimento.FISICO)).build());

        Treinador agatha = new Treinador("Agatha (Elite 4)", new HeuristicStrategy());
        agatha.adicionarPokemon(criarMolde("Gengar", Tipo.FANTASMA, Tipo.VENENOSO, new Estatisticas(60, 65, 60, 130, 75, 110))
                .setNatureza(Natureza.TIMID)
                .adicionarMovimento(new Movimento("Shadow Ball", Tipo.FANTASMA, 80, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Sludge Wave", Tipo.VENENOSO, 95, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Thunderbolt", Tipo.ELETRICO, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Dazzling Gleam", Tipo.FADA, 80, CategoriaMovimento.ESPECIAL)).build());
        agatha.adicionarPokemon(criarMolde("Arbok", Tipo.VENENOSO, null, new Estatisticas(60, 95, 69, 65, 79, 80))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Gunk Shot", Tipo.VENENOSO, 120, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Crunch", Tipo.NOTURNO, 80, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Rock Slide", Tipo.PEDRA, 75, CategoriaMovimento.FISICO)).build());

        Treinador lance = new Treinador("Lance (Elite 4)", new HeuristicStrategy());
        lance.adicionarPokemon(criarMolde("Dragonite", Tipo.DRAGAO, Tipo.VOADOR, new Estatisticas(91, 134, 95, 100, 100, 80))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Outrage", Tipo.DRAGAO, 120, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Fire Punch", Tipo.FOGO, 75, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Extreme Speed", Tipo.NORMAL, 80, CategoriaMovimento.FISICO)).build());
        lance.adicionarPokemon(criarMolde("Gyarados", Tipo.AGUA, Tipo.VOADOR, new Estatisticas(95, 125, 79, 60, 100, 81))
                .setNatureza(Natureza.JOLLY)
                .adicionarMovimento(new Movimento("Waterfall", Tipo.AGUA, 80, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Ice Fang", Tipo.GELO, 65, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Crunch", Tipo.NOTURNO, 80, CategoriaMovimento.FISICO)).build());

        Treinador campeao = new Treinador("Blue (Campeao)", new HeuristicStrategy());
        campeao.adicionarPokemon(criarMolde("Pidgeot", Tipo.NORMAL, Tipo.VOADOR, new Estatisticas(83, 80, 75, 70, 70, 101))
                .setNatureza(Natureza.JOLLY)
                .adicionarMovimento(new Movimento("Brave Bird", Tipo.VOADOR, 120, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Double-Edge", Tipo.NORMAL, 120, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("U-turn", Tipo.INSETO, 70, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Steel Wing", Tipo.ACO, 70, CategoriaMovimento.FISICO)).build());
        campeao.adicionarPokemon(criarMolde("Alakazam", Tipo.PSIQUICO, null, new Estatisticas(55, 50, 45, 135, 95, 120))
                .setNatureza(Natureza.TIMID)
                .adicionarMovimento(new Movimento("Psychic", Tipo.PSIQUICO, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Shadow Ball", Tipo.FANTASMA, 80, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Focus Blast", Tipo.LUTADOR, 120, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Energy Ball", Tipo.GRAMA, 90, CategoriaMovimento.ESPECIAL)).build());
        campeao.adicionarPokemon(criarMolde("Rhydon", Tipo.TERRA, Tipo.PEDRA, new Estatisticas(105, 130, 120, 45, 45, 40))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Stone Edge", Tipo.PEDRA, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Megahorn", Tipo.INSETO, 120, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Hammer Arm", Tipo.LUTADOR, 100, CategoriaMovimento.FISICO)).build());
        campeao.adicionarPokemon(criarMolde("Exeggutor", Tipo.GRAMA, Tipo.PSIQUICO, new Estatisticas(95, 95, 85, 125, 65, 55))
                .setNatureza(Natureza.MODEST)
                .adicionarMovimento(new Movimento("Giga Drain", Tipo.GRAMA, 75, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Psychic", Tipo.PSIQUICO, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Sludge Bomb", Tipo.VENENOSO, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Ancient Power", Tipo.PEDRA, 60, CategoriaMovimento.ESPECIAL)).build());
        campeao.adicionarPokemon(criarMolde("Arcanine", Tipo.FOGO, null, new Estatisticas(90, 110, 80, 100, 80, 95))
                .setNatureza(Natureza.JOLLY)
                .adicionarMovimento(new Movimento("Flare Blitz", Tipo.FOGO, 120, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Extreme Speed", Tipo.NORMAL, 80, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Wild Charge", Tipo.ELETRICO, 90, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Close Combat", Tipo.LUTADOR, 120, CategoriaMovimento.FISICO)).build());
        campeao.adicionarPokemon(criarMolde("Blastoise", Tipo.AGUA, null, new Estatisticas(79, 83, 100, 85, 105, 78))
                .setNatureza(Natureza.BOLD)
                .adicionarMovimento(new Movimento("Hydro Pump", Tipo.AGUA, 110, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Ice Beam", Tipo.GELO, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Aura Sphere", Tipo.LUTADOR, 80, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Dark Pulse", Tipo.NOTURNO, 80, CategoriaMovimento.ESPECIAL)).build());

        liga.add(lorelei);
        liga.add(bruno);
        liga.add(agatha);
        liga.add(lance);
        liga.add(campeao);

        return liga;
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