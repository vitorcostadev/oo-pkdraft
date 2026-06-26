#pragma once
#include "../models/Pokemon.h"
#include "../models/Movement.h"
class DamageCalculator {
    public:
        static int calculateDamage(Pokemon* attacker, Pokemon* defender, Movement* movement) {
            
            int basePower = movement->getPower();
            int attackStat = attacker->getStats().getAttack();
            int defenseStat = defender->getStats().getDefense();

            int damage = ((2 * attacker->getLevel() / 5 + 2) * basePower * attackStat / defenseStat) / 50 + 2;

            return damage;
        }
};