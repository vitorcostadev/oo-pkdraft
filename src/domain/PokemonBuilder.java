package domain;

import domain.models.Estatisticas;
import domain.models.Movimento;
import domain.models.Natureza;
import domain.models.Tipo;

import java.util.ArrayList;
import java.util.List;

/**
 * Aplica o padrão Builder para gerir a instanciação complexa de um Pokemon.
 */
public class PokemonBuilder {
    private String nome;
    private final int NIVEL = 100;
    private Natureza natureza;
    private Tipo tipo1;
    private Tipo tipo2;
    private Estatisticas baseStats;
    private final Estatisticas ivs;
    private final Estatisticas evs;
    private final List<Movimento> movimentos;

    public PokemonBuilder() {
        this.ivs = new Estatisticas(31, 31, 31, 31, 31, 31);
        this.evs = new Estatisticas(0, 0, 0, 0, 0, 0);
        this.movimentos = new ArrayList<>();
    }

    /**
     * Define as informações iniciais de identificação e tipagem.
     * @param nome Nome do Pokémon.
     * @param t1 Tipo primário.
     * @param t2 Tipo secundário, podendo ser nulo.
     * @return A propria instancia do Builder.
     */
    public PokemonBuilder setDadosBase(String nome, Tipo t1, Tipo t2) {
        this.nome = nome;
        this.tipo1 = t1;
        this.tipo2 = t2;
        return this;
    }

    /**
     * Define os pontos base (Base Stats) inerentes a espécie.
     * @param base Objeto Estatísticas contendo os pontos base.
     * @return A propria instancia do Builder.
     */
    public PokemonBuilder setEstatisticasPadrao(Estatisticas base) {
        this.baseStats = base;
        return this;
    }

    /**
     * Define a natureza aleatória ou escolhida.
     * @param natureza Natureza escolhida.
     * @return A propria instancia do Builder.
     */
    public PokemonBuilder setNatureza(Natureza natureza) {
        this.natureza = natureza;
        return this;
    }

    /**
     * Adiciona um ataque a lista de movimentos se o limite não foi atingido.
     * @param m Objeto do movimento.
     * @return A propria instancia do Builder.
     */
    public PokemonBuilder adicionarMovimento(Movimento m) {
        if (this.movimentos.size() < 4) {
            this.movimentos.add(m);
        }
        return this;
    }

    /**
     * Finaliza a construção e devolve a entidade integra.
     * @return Instancia valida e calculada de Pokemon.
     */
    public Pokemon build() {
        if (this.nome == null || this.baseStats == null || this.natureza == null || this.tipo1 == null) {
            throw new IllegalStateException("Faltam parâmetros obrigatórios para construir o Pokemon.");
        }
        return new Pokemon(this);
    }

    public String getNome() { return nome; }
    public int getNivel() { return NIVEL; }
    public Natureza getNatureza() { return natureza; }
    public Tipo getTipo1() { return tipo1; }
    public Tipo getTipo2() { return tipo2; }
    public Estatisticas getBaseStats() { return baseStats; }
    public Estatisticas getIvs() { return ivs; }
    public Estatisticas getEvs() { return evs; }
    public List<Movimento> getMovimentos() { return movimentos; }
}