#pragma once
#include "Pokemon.h"
#include <vector>
#include <stdexcept>
#include "../strategy/DecisionStrategy.h"

class TurnCommand;

class Trainer{
    private:
        const char* name;
        std::vector<Pokemon> pokemonTeam;
        DecisionStrategy* strategy;

    public:
        Trainer(const char* name, DecisionStrategy* strategy) : name(name), 
        strategy(strategy) {}

        void addPokemon(Pokemon& pokemon) {
            if (pokemonTeam.size() < 6) {
                pokemonTeam.push_back(pokemon);
            }
        }

        Pokemon* getActivePokemon() {
            if(pokemonTeam.empty()) {
                throw std::runtime_error("No Pokemon in the team.");
            }
            return &pokemonTeam[0];
        }

        //log.registrarEvento(atacante.getNome() + " ordenou que " + pAtacante.getNome() + " usasse " + movimento.getNome() + "!");
        

        bool hasAvailablePokemon() {
            for (const auto& pokemon : pokemonTeam) {
                if (pokemon.getStats().getHp() > 0) {
                    return true;
                }
            }
            return false;
        }

        void setActivePokemon(Pokemon& pokemon) {
            for(auto& p : pokemonTeam) {
                if(&p == &pokemon && p.getStats().getHp() > 0) {
                    this->pokemonTeam.erase(pokemonTeam.begin() + (&p - &pokemonTeam[0]));
                    this->pokemonTeam.push_back(p);
                    return;
                }
            }

        }

        void cureTeam() {
            for(auto& pokemon : pokemonTeam) {
                pokemon.cure();
            }
        }
        
        TurnCommand* soliciteAction(Trainer* opponent) {
            return strategy->chooseAction(this, opponent);
        }

        const char* getName() const {
            return name;
        }

        std::vector<Pokemon>& getPokemonTeam() {
            return pokemonTeam;
        }

        DecisionStrategy* getStrategy() const {
            return strategy;
        }
        
        ~Trainer() {};

};