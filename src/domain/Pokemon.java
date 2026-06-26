package domain;

import domain.models.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um Pokemon.
 */
public class Pokemon {
    private final String nome;
    private final int nivel;
    private final Natureza natureza;
    private final Tipo tipo1;
    private final Tipo tipo2;
    private final Estatisticas baseStats;
    private final Estatisticas ivs;
    private final Estatisticas evs;
    private Estatisticas statsCombate;
    private final List<Movimento> movimentos;
    private int hpAtual;

    Pokemon(PokemonBuilder builder) {
        this.nome = builder.getNome();
        this.nivel = builder.getNivel();
        this.natureza = builder.getNatureza();
        this.tipo1 = builder.getTipo1();
        this.tipo2 = builder.getTipo2();
        this.baseStats = builder.getBaseStats();
        this.ivs = builder.getIvs();
        this.evs = builder.getEvs();
        this.movimentos = new ArrayList<>(builder.getMovimentos());
        calcularStatsDeCombate();
        this.hpAtual = this.statsCombate.getHp();
    }

    private void calcularStatsDeCombate() {
        int hp = calcularHpMaximo();
        int ataque = calcularEstatistica(Atributo.ATAQUE);
        int defesa = calcularEstatistica(Atributo.DEFESA);
        int ataqueEspecial = calcularEstatistica(Atributo.ATAQUE_ESPECIAL);
        int defesaEspecial = calcularEstatistica(Atributo.DEFESA_ESPECIAL);
        int velocidade = calcularEstatistica(Atributo.VELOCIDADE);

        this.statsCombate = new Estatisticas(hp, ataque, defesa, ataqueEspecial, defesaEspecial, velocidade);
    }

    private int calcularHpMaximo() {
        int base = this.baseStats.getHp();
        int iv = this.ivs.getHp();
        int ev = this.evs.getHp();
        return ((2 * base + iv + (ev / 4)) * this.nivel) / 100 + this.nivel + 10;
    }

    private int calcularEstatistica(Atributo atributo) {
        int base = this.baseStats.getValor(atributo);
        int iv = this.ivs.getValor(atributo);
        int ev = this.evs.getValor(atributo);
        double multiplicador = this.natureza.obterMultiplicador(atributo);

        int calculoBase = ((2 * base + iv + (ev / 4)) * this.nivel) / 100 + 5;
        return (int) (calculoBase * multiplicador);
    }

    /**
     * Aplica redução no ponto de vida atual garantindo que o valor não seja negativo.
     * @param dano Quantidade de dano bruto calculada.
     */
    public void receberDano(int dano) {
        this.hpAtual -= dano;
        if (this.hpAtual < 0) {
            this.hpAtual = 0;
        }
    }

    /**
     * Restaura os pontos de vida para a capacidade maxima calculada.
     */
    public void curarTotalmente() {
        this.hpAtual = this.statsCombate.getHp();
    }

    /**
     * Verifica se o Pokemon possui um determinado tipo.
     * @param t Tipo a ser verificado.
     * @return True caso o Pokemon possua o tipo.
     */
    public boolean temTipo(Tipo t) {
        return this.tipo1 == t || this.tipo2 == t;
    }

    /**
     * Valida se o Pokemon zerou seus pontos de vida.
     * @return True caso o hpAtual seja zero.
     */
    public boolean isDesmaiado() {
        return this.hpAtual == 0;
    }

    public String getNome() {
        return nome;
    }

    public Estatisticas getEstatisticas() {
        return statsCombate;
    }

    public List<Movimento> getMovimentos() {
        return movimentos;
    }

    public int getHpAtual() {
        return hpAtual;
    }

    public Tipo getTipo1() {
        return tipo1;
    }

    public Tipo getTipo2() {
        return tipo2;
    }

    public Natureza getNatureza() {
        return natureza;
    }
}