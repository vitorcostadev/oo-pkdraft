package domain.models;

/**
 * Gerencia as naturezas dos Pokémons,
 * aplicando modificadores sobre os atributos correspondentes.
 */
public enum Natureza {
    HARDY(null, null),
    LONELY(Atributo.ATAQUE, Atributo.DEFESA),
    BRAVE(Atributo.ATAQUE, Atributo.VELOCIDADE),
    ADAMANT(Atributo.ATAQUE, Atributo.ATAQUE_ESPECIAL),
    NAUGHTY(Atributo.ATAQUE, Atributo.DEFESA_ESPECIAL),
    BOLD(Atributo.DEFESA, Atributo.ATAQUE),
    DOCILE(null, null),
    RELAXED(Atributo.DEFESA, Atributo.VELOCIDADE),
    IMPISH(Atributo.DEFESA, Atributo.ATAQUE_ESPECIAL),
    LAX(Atributo.DEFESA, Atributo.DEFESA_ESPECIAL),
    TIMID(Atributo.VELOCIDADE, Atributo.ATAQUE),
    HASTY(Atributo.VELOCIDADE, Atributo.DEFESA),
    SERIOUS(null, null),
    JOLLY(Atributo.VELOCIDADE, Atributo.ATAQUE_ESPECIAL),
    NAIVE(Atributo.VELOCIDADE, Atributo.DEFESA_ESPECIAL),
    MODEST(Atributo.ATAQUE_ESPECIAL, Atributo.ATAQUE),
    MILD(Atributo.ATAQUE_ESPECIAL, Atributo.DEFESA),
    QUIET(Atributo.ATAQUE_ESPECIAL, Atributo.VELOCIDADE),
    BASHFUL(null, null),
    RASH(Atributo.ATAQUE_ESPECIAL, Atributo.DEFESA_ESPECIAL),
    CALM(Atributo.DEFESA_ESPECIAL, Atributo.ATAQUE),
    GENTLE(Atributo.DEFESA_ESPECIAL, Atributo.DEFESA),
    SASSY(Atributo.DEFESA_ESPECIAL, Atributo.VELOCIDADE),
    CAREFUL(Atributo.DEFESA_ESPECIAL, Atributo.ATAQUE_ESPECIAL),
    QUIRKY(null, null);

    private final Atributo atributoAumentado;
    private final Atributo atributoDiminuido;

    Natureza(Atributo atributoAumentado, Atributo atributoDiminuido) {
        this.atributoAumentado = atributoAumentado;
        this.atributoDiminuido = atributoDiminuido;
    }

    /**
     * Retorna o multiplicador estatístico baseado no atributo consultado.
     * @param a Atributo alvo da verificação.
     * @return Valor decimal representativo do modificador.
     */
    public double obterMultiplicador(Atributo a) {
        if (this.atributoAumentado == a && this.atributoDiminuido != a) {
            return 1.1;
        }
        if (this.atributoDiminuido == a && this.atributoAumentado != a) {
            return 0.9;
        }
        return 1.0;
    }
}