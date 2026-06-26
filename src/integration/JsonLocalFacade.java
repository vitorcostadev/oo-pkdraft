package integration;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import domain.models.CategoriaMovimento;
import domain.models.Estatisticas;
import domain.strategy.IAHeuristicaStrategy;
import domain.models.Movimento;
import domain.models.Natureza;
import domain.PokemonBuilder;
import domain.models.Tipo;
import domain.models.Treinador;

/**
 * Implementa a fachada de dados realizando a leitura e interpretação de arquivos locais.
 */
public class JsonLocalFacade implements PokemonDadosFacade {

    private final List<PokemonBuilder> repositorioBase;

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

    private void processarJson(String jsonBruto) {
        this.repositorioBase.clear();
        String[] blocos = jsonBruto.split("\\{");

        for (int i = 1; i < blocos.length; i++) {
            String bloco = blocos[i];
            int indiceFechamento = bloco.indexOf('}');
            if (indiceFechamento == -1) {
                continue;
            }

            String conteudo = bloco.substring(0, indiceFechamento);

            String nome = extrairTexto(conteudo, "nome");
            String t1Str = extrairTexto(conteudo, "t1");
            String t2Str = extrairTexto(conteudo, "t2");

            int hp = extrairInteiro(conteudo, "hp");
            int atk = extrairInteiro(conteudo, "atk");
            int def = extrairInteiro(conteudo, "def");
            int spa = extrairInteiro(conteudo, "spa");
            int spd = extrairInteiro(conteudo, "spd");
            int spe = extrairInteiro(conteudo, "spe");

            String m1 = extrairTexto(conteudo, "m1");
            String m2 = extrairTexto(conteudo, "m2");
            String m3 = extrairTexto(conteudo, "m3");
            String m4 = extrairTexto(conteudo, "m4");

            Tipo t1 = Tipo.valueOf(t1Str);
            Tipo t2 = (t2Str != null) ? Tipo.valueOf(t2Str) : null;

            Estatisticas stats = new Estatisticas(hp, atk, def, spa, spd, spe);

            PokemonBuilder builder = new PokemonBuilder()
                    .setDadosBase(nome, t1, t2)
                    .setEstatisticasPadrao(stats)
                    .adicionarMovimento(new Movimento(m1, t1, 90, CategoriaMovimento.ESPECIAL))
                    .adicionarMovimento(new Movimento(m2, Tipo.NORMAL, 80, CategoriaMovimento.FISICO))
                    .adicionarMovimento(new Movimento(m3, t1, 75, CategoriaMovimento.ESPECIAL))
                    .adicionarMovimento(new Movimento(m4, Tipo.LUTADOR, 100, CategoriaMovimento.FISICO));

            this.repositorioBase.add(builder);
        }

        if (this.repositorioBase.isEmpty()) {
            carregarDadosDeContingencia();
        }
    }

    private String extrairTexto(String bloco, String chave) {
        String parametro = "\"" + chave + "\":";
        int inicio = bloco.indexOf(parametro);
        if (inicio == -1) {
            return null;
        }
        inicio += parametro.length();

        int fimVirgula = bloco.indexOf(',', inicio);
        int fimBloco = bloco.length();
        int fim = (fimVirgula != -1) ? fimVirgula : fimBloco;

        String valorBruto = bloco.substring(inicio, fim).trim();
        if (valorBruto.equals("null")) {
            return null;
        }
        return valorBruto.replace("\"", "").trim();
    }

    private int extrairInteiro(String bloco, String chave) {
        String texto = extrairTexto(bloco, chave);
        return (texto != null) ? Integer.parseInt(texto) : 0;
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

        Treinador lorelei = new Treinador("Lorelei (Elite 4)", new IAHeuristicaStrategy());
        PokemonBuilder lapras = criarMolde("Lapras", Tipo.AGUA, Tipo.GELO, new Estatisticas(130, 85, 80, 85, 95, 60))
                .setNatureza(Natureza.MODEST)
                .adicionarMovimento(new Movimento("Ice Beam", Tipo.GELO, 90, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Surf", Tipo.AGUA, 90, CategoriaMovimento.ESPECIAL));
        PokemonBuilder cloyster = criarMolde("Cloyster", Tipo.AGUA, Tipo.GELO, new Estatisticas(50, 95, 180, 85, 45, 70))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Icicle Crash", Tipo.GELO, 85, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Explosion", Tipo.NORMAL, 250, CategoriaMovimento.FISICO));
        lorelei.adicionarPokemon(lapras.build());
        lorelei.adicionarPokemon(cloyster.build());

        Treinador bruno = new Treinador("Bruno (Elite 4)", new IAHeuristicaStrategy());
        PokemonBuilder machamp = criarMolde("Machamp", Tipo.LUTADOR, null, new Estatisticas(90, 130, 80, 65, 85, 55))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Dynamic Punch", Tipo.LUTADOR, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Stone Edge", Tipo.PEDRA, 100, CategoriaMovimento.FISICO));
        PokemonBuilder onix = criarMolde("Onix", Tipo.PEDRA, Tipo.TERRA, new Estatisticas(35, 45, 160, 30, 45, 70))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Rock Slide", Tipo.PEDRA, 75, CategoriaMovimento.FISICO));
        bruno.adicionarPokemon(machamp.build());
        bruno.adicionarPokemon(onix.build());

        Treinador agatha = new Treinador("Agatha (Elite 4)", new IAHeuristicaStrategy());
        PokemonBuilder gengar = criarMolde("Gengar", Tipo.FANTASMA, Tipo.VENENOSO, new Estatisticas(60, 65, 60, 130, 75, 110))
                .setNatureza(Natureza.TIMID)
                .adicionarMovimento(new Movimento("Shadow Ball", Tipo.FANTASMA, 80, CategoriaMovimento.ESPECIAL))
                .adicionarMovimento(new Movimento("Sludge Wave", Tipo.VENENOSO, 95, CategoriaMovimento.ESPECIAL));
        PokemonBuilder arbok = criarMolde("Arbok", Tipo.VENENOSO, null, new Estatisticas(60, 95, 69, 65, 79, 80))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Gunk Shot", Tipo.VENENOSO, 120, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO));
        agatha.adicionarPokemon(gengar.build());
        agatha.adicionarPokemon(arbok.build());

        Treinador lance = new Treinador("Lance (Elite 4)", new IAHeuristicaStrategy());
        PokemonBuilder dragonite = criarMolde("Dragonite", Tipo.DRAGAO, Tipo.VOADOR, new Estatisticas(91, 134, 95, 100, 100, 80))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Outrage", Tipo.DRAGAO, 120, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO));
        PokemonBuilder gyarados = criarMolde("Gyarados", Tipo.AGUA, Tipo.VOADOR, new Estatisticas(95, 125, 79, 60, 100, 81))
                .setNatureza(Natureza.JOLLY)
                .adicionarMovimento(new Movimento("Waterfall", Tipo.AGUA, 80, CategoriaMovimento.FISICO))
                .adicionarMovimento(new Movimento("Ice Fang", Tipo.GELO, 65, CategoriaMovimento.FISICO));
        lance.adicionarPokemon(dragonite.build());
        lance.adicionarPokemon(gyarados.build());

        Treinador campeao = new Treinador("Blue (Campeão)", new IAHeuristicaStrategy());

        PokemonBuilder pidgeot = criarMolde("Pidgeot", Tipo.NORMAL, Tipo.VOADOR, new Estatisticas(83, 80, 75, 70, 70, 101))
                .setNatureza(Natureza.JOLLY)
                .adicionarMovimento(new Movimento("Brave Bird", Tipo.VOADOR, 120, CategoriaMovimento.FISICO));

        PokemonBuilder alakazam = criarMolde("Alakazam", Tipo.PSIQUICO, null, new Estatisticas(55, 50, 45, 135, 95, 120))
                .setNatureza(Natureza.TIMID)
                .adicionarMovimento(new Movimento("Psychic", Tipo.PSIQUICO, 90, CategoriaMovimento.ESPECIAL));

        PokemonBuilder rhydon = criarMolde("Rhydon", Tipo.TERRA, Tipo.PEDRA, new Estatisticas(105, 130, 120, 45, 45, 40))
                .setNatureza(Natureza.ADAMANT)
                .adicionarMovimento(new Movimento("Earthquake", Tipo.TERRA, 100, CategoriaMovimento.FISICO));

        PokemonBuilder exeggutor = criarMolde("Exeggutor", Tipo.GRAMA, Tipo.PSIQUICO, new Estatisticas(95, 95, 85, 125, 65, 55))
                .setNatureza(Natureza.MODEST)
                .adicionarMovimento(new Movimento("Giga Drain", Tipo.GRAMA, 75, CategoriaMovimento.ESPECIAL));

        PokemonBuilder arcanine = criarMolde("Arcanine", Tipo.FOGO, null, new Estatisticas(90, 110, 80, 100, 80, 95))
                .setNatureza(Natureza.JOLLY)
                .adicionarMovimento(new Movimento("Flare Blitz", Tipo.FOGO, 120, CategoriaMovimento.FISICO));

        PokemonBuilder blastoise = criarMolde("Blastoise", Tipo.AGUA, null, new Estatisticas(79, 83, 100, 85, 105, 78))
                .setNatureza(Natureza.BOLD)
                .adicionarMovimento(new Movimento("Hydro Pump", Tipo.AGUA, 110, CategoriaMovimento.ESPECIAL));

        campeao.adicionarPokemon(pidgeot.build());
        campeao.adicionarPokemon(alakazam.build());
        campeao.adicionarPokemon(rhydon.build());
        campeao.adicionarPokemon(exeggutor.build());
        campeao.adicionarPokemon(arcanine.build());
        campeao.adicionarPokemon(blastoise.build());

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
        this.repositorioBase.add(criarMolde("Venusaur", Tipo.GRAMA, Tipo.VENENOSO, new Estatisticas(80, 82, 83, 100, 100, 80))
                .adicionarMovimento(new Movimento("Giga Drain", Tipo.GRAMA, 75, CategoriaMovimento.ESPECIAL)));
        this.repositorioBase.add(criarMolde("Blastoise", Tipo.AGUA, null, new Estatisticas(79, 83, 100, 85, 105, 78))
                .adicionarMovimento(new Movimento("Hydro Pump", Tipo.AGUA, 110, CategoriaMovimento.ESPECIAL)));
    }

    private PokemonBuilder criarMolde(String nome, Tipo t1, Tipo t2, Estatisticas base) {
        PokemonBuilder builder = new PokemonBuilder();
        builder.setDadosBase(nome, t1, t2);
        builder.setEstatisticasPadrao(base);
        return builder;
    }
}