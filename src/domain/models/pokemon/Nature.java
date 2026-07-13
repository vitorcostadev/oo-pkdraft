package domain.models.pokemon;

public enum Nature {
    HARDY(null, null),
    LONELY(Attribute.ATK, Attribute.DEF),
    BRAVE(Attribute.ATK, Attribute.SPE),
    ADAMANT(Attribute.ATK, Attribute.SPA),
    NAUGHTY(Attribute.ATK, Attribute.SPD),
    BOLD(Attribute.DEF, Attribute.ATK),
    DOCILE(null, null),
    RELAXED(Attribute.DEF, Attribute.SPE),
    IMPISH(Attribute.DEF, Attribute.SPA),
    LAX(Attribute.DEF, Attribute.SPD),
    TIMID(Attribute.SPE, Attribute.ATK),
    HASTY(Attribute.SPE, Attribute.DEF),
    SERIOUS(null, null),
    JOLLY(Attribute.SPE, Attribute.SPA),
    NAIVE(Attribute.SPE, Attribute.SPD),
    MODEST(Attribute.SPA, Attribute.ATK),
    MILD(Attribute.SPA, Attribute.DEF),
    QUIET(Attribute.SPA, Attribute.SPE),
    BASHFUL(null, null),
    RASH(Attribute.SPA, Attribute.SPD),
    CALM(Attribute.SPD, Attribute.ATK),
    GENTLE(Attribute.SPD, Attribute.DEF),
    SASSY(Attribute.SPD, Attribute.SPE),
    CAREFUL(Attribute.SPD, Attribute.SPA),
    QUIRKY(null, null);

    private final Attribute upAttr;
    private final Attribute lowerAttr;

    Nature(Attribute upAttr, Attribute lowerAttr) {
        this.upAttr = upAttr;
        this.lowerAttr = lowerAttr;
    }

    /**
     * Returns the multiplier based on the nature
     * @param a target
     * @return decimal value that's represents the multiplier
     */
    public double getMultiplier(Attribute a) {
        if(a == null) return 0;

        if (this.upAttr == a && this.lowerAttr != a) {
            return 1.1;
        }
        if (this.lowerAttr == a && this.upAttr != a) {
            return 0.9;
        }
        return 1.0;
    }
}