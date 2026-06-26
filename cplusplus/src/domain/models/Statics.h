#pragma once 
#include "../enums/Enums.h"

class Statics {
    private:
        int hp, attack, defense, specialAttack, specialDefense, speed;
    public:
        Statics(int hp, int attack, int defense, int specialAttack, int specialDefense, int speed) : hp(hp), attack(attack), defense(defense), specialAttack(specialAttack), specialDefense(specialDefense), speed(speed) {};

        int getHp() const {return hp;};
        int getSpeed() const {return speed;};
        int getAttack() const {return attack;};
        int getDefense() const {return defense;};
        int getValue(Attribute attribute) const {
            switch(attribute){
                case Attribute::HP:
                    return hp;
                case Attribute::ATTACK:
                    return attack;
                case Attribute::DEFENSE:
                    return defense;
                case Attribute::SPECIAL_ATTACK:
                    return specialAttack;
                case Attribute::SPECIAL_DEFENSE:
                    return specialDefense;
                case Attribute::SPEED:
                    return speed;
                default:
                    return 0;
            }
        }
        ~Statics() {};
};