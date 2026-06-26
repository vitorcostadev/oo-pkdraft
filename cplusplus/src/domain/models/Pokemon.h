#pragma once
#include "../enums/Enums.h"
#include "Statics.h"
#include "Movement.h"
#include "builder/PokemonBuilder.h"
#include <list>

class Pokemon{
    private:
        const char* name;
        int level, actualHP;
        Nature nature;
        Type type_one;
        Type type_two;
        Statics baseStats = Statics(0, 0, 0, 0, 0, 0);
        Statics individualValues = Statics(31, 31, 31, 31, 31, 31);
        Statics effortValues = Statics(0, 0, 0, 0, 0, 0);
        Statics stats = Statics(0, 0, 0, 0, 0, 0);
        std::list<Movement> movements;

        int calculateMaxHP() const {
            return ((2 * baseStats.getHp() + individualValues.getValue(Attribute::HP) + (effortValues.getValue(Attribute::HP) / 4)) * level) / 100 + level + 10;
        }

        int calculateStat(Attribute attribute) const {
            int baseStat = baseStats.getValue(attribute);
            int iv = individualValues.getValue(attribute);
            int ev = effortValues.getValue(attribute);
            int stat = ((2 * baseStat + iv + (ev / 4)) * level) / 100 + 5;
            
            double multiplier = Nature::calculateEffect(nature, attribute);
            int baseCalc = ((2 * baseStat + iv + (ev / 4)) * level) / 100 + 5;

            return (int) (baseCalc * multiplier);
        }

        void calculateStats() {
            int hp = calculateMaxHP();
            int attack = calculateStat(Attribute::ATTACK);
            int defense = calculateStat(Attribute::DEFENSE);
            int specialAttack = calculateStat(Attribute::SPECIAL_ATTACK);
            int specialDefense = calculateStat(Attribute::SPECIAL_DEFENSE);
            int speed = calculateStat(Attribute::SPEED);

            stats = Statics(hp, attack, defense, specialAttack, specialDefense, speed);
        }

    public:
        Pokemon(PokemonBuilder& builder) 
            : name(builder.getName()), 
            level(builder.getLevel()), 
            nature(builder.getNature()), 
            type_one(builder.getTypeOne()), 
            type_two(builder.getTypeTwo()), 
            baseStats(builder.getBaseStats()), 
            individualValues(builder.getIndividualValues()), 
            effortValues(builder.getEffortValues()), 
            movements(builder.getMovements()) {
                calculateStats();
                actualHP = stats.getHp();
        };

        const char* getName() const { return name; }
        int getLevel() const { return level; }
        Nature getNature() const { return nature; }
        Type getTypeOne() const { return type_one; }
        Type getTypeTwo() const { return type_two; }
        Statics getBaseStats() const { return baseStats; }
        Statics getIndividualValues() const { return individualValues; }
        Statics getEffortValues() const { return effortValues; }
        Statics getStats() const { return stats; }
        std::list<Movement> getMovements() const { return movements; }
        void cure() { actualHP = stats.getHp(); }
        int getActualHP() const { return actualHP; }
        void setActualHP(int hp) { actualHP = hp; }
        ~Pokemon() {};
};