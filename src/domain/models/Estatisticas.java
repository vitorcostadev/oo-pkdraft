package domain.models;

/**
 * Abstrai e encapsula o bloco de dados numéricos de atributos de um Pokemon.
 */
public class Estatisticas {
    private final int hp;
    private final int ataque;
    private final int defesa;
    private final int ataqueEspecial;
    private final int defesaEspecial;
    private final int velocidade;

    public Estatisticas(int hp,
                        int ataque,
                        int defesa,
                        int ataqueEspecial,
                        int defesaEspecial,
                        int velocidade) {
        this.hp = hp;
        this.ataque = ataque;
        this.defesa = defesa;
        this.ataqueEspecial = ataqueEspecial;
        this.defesaEspecial = defesaEspecial;
        this.velocidade = velocidade;
    }

    /**
     * Recupera o valor de um atributo especifico.
     * @param a Enum correspondente ao atributo desejado.
     * @return Valor numérico inteiro do atributo solicitado.
     */
    public int getValor(Atributo a) {
        return switch (a) {
            case ATAQUE -> ataque;
            case DEFESA -> defesa;
            case ATAQUE_ESPECIAL -> ataqueEspecial;
            case DEFESA_ESPECIAL -> defesaEspecial;
            case VELOCIDADE -> velocidade;
        };
    }

    public int getHp() {
        return hp;
    }

    @Override
    public String toString() {
        return "Estatisticas{" +
                "HP=" + hp +
                ", ATK=" + ataque +
                ", DEF=" + defesa +
                ", SPATK=" + ataqueEspecial +
                ", SPDEF=" + defesaEspecial +
                ", SPE=" + velocidade +
                '}';
    }
}