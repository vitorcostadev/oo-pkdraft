#pragma once
#include "../../enums/Enums.h"
#include "../Statics.h"
#include "../Movement.h"
#include <list>

class Pokemon;

class PokemonBuilder{
    private:
        const char* name;
        const int LEVEL = 50;
        Nature nature = Nature::HARDY;
        Type type_one;
        Type type_two;
        Statics baseStats = Statics(0, 0, 0, 0, 0, 0);
        Statics individualValues = Statics(31, 31, 31, 31, 31, 31);
        Statics effortValues = Statics(0, 0, 0, 0, 0, 0);
        std::list<Movement> movements;
    public:
        PokemonBuilder() : 
        movements(std::list<Movement>()) {};

        PokemonBuilder& setName(const char* name) {
            this->name = name;
            return *this;
        }
        PokemonBuilder& setNature(Nature nature){
            this->nature = nature;
            return *this;
        }
        PokemonBuilder& setTypeOne(Type type_one){
            this->type_one = type_one;
            return *this;
        }
        PokemonBuilder& setTypeTwo(Type type_two){
            this->type_two = type_two;
            return *this;
        }
        PokemonBuilder& setBaseStats(Statics baseStats){
            this->baseStats = baseStats;
            return *this;
        }
        PokemonBuilder& setIndividualValues(Statics individualValues){
            this->individualValues = individualValues;
            return *this;
        }
        PokemonBuilder& setEffortValues(Statics effortValues){
            this->effortValues = effortValues;
            return *this;
        }
        PokemonBuilder& addMovement(Movement movement){
            if(this->movements.size() < 4){
                this->movements.push_back(movement);
            }
            return *this;
        }

        const char* getName() const {return name;};
        int getLevel() const {return LEVEL;}; 
        Nature getNature() const {return nature;};
        Type getTypeOne() const {return type_one;};
        Type getTypeTwo() const {return type_two;};
        Statics getBaseStats() const {return baseStats;};
        Statics getIndividualValues() const {return individualValues;};
        Statics getEffortValues() const {return effortValues;};
        std::list<Movement> getMovements() const {return movements;};
        
        Pokemon build();

        ~PokemonBuilder() {};
};