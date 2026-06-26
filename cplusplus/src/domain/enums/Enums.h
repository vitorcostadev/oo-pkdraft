#pragma once 



enum class Type {
    NORMAL,
    FIRE,
    WATER,
    GRASS,
    ELECTRIC,
    ICE,
    FIGHTING,
    POISON,
    GROUND,
    FLYING,
    PSYCHIC,
    BUG,
    ROCK,
    GHOST,
    DRAGON,
    DARK,
    STEEL,
    FAIRY
};

enum class Attribute{
    HP,
    ATTACK,
    DEFENSE,
    SPECIAL_ATTACK,
    SPECIAL_DEFENSE,
    SPEED
};

enum class MovementCategory{
    PHYSICAL,
    SPECIAL,
    STATUS
};

class Nature{
    private:
        Attribute increasedStat;
        Attribute decreasedStat;
    
    public:
        Nature(Attribute increasedStat, Attribute decreasedStat) : increasedStat(increasedStat), decreasedStat(decreasedStat) {};

        const static Nature HARDY;
        const static Nature LONELY;
        const static Nature BRAVE;
        const static Nature ADAMANT;
        const static Nature NAUGHTY;
        const static Nature BOLD;
        const static Nature DOCILE;
        const static Nature RELAXED;
        const static Nature IMPISH;
        const static Nature LAX;
        const static Nature TIMID;
        const static Nature HASTY;
        const static Nature SERIOUS;
        const static Nature JOLLY;
        const static Nature NAIVE;
        const static Nature MODEST;
        const static Nature MILD;
        const static Nature QUIET;
        const static Nature BASHFUL;
        const static Nature RASH;
        const static Nature CALM;
        const static Nature GENTLE;
        const static Nature SASSY;
        const static Nature CAREFUL;
        const static Nature QUIRKY;

        static double calculateEffect(Nature nature, Attribute attribute) {
            if (attribute == nature.increasedStat) {
                return 1.1;
            } else if (attribute == nature.decreasedStat) {
                return 0.9;
            } else {
                return 1.0;
            }
        }
};

