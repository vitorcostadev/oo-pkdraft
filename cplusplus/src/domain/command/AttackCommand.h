#pragma once
#include "../models/Trainer.h"
#include "../models/Movement.h"
#include "TurnCommand.h"
#include "../services/DamageCalculator.h"
#include "../../services/LogService.h"

class AttackCommand : public TurnCommand {
    private:
        Trainer* attacker;
        Movement* movement;
        DamageCalculator* damageCalculator;

    public:
        AttackCommand(Trainer* attacker, Movement* movement){
            this->attacker = attacker;
            this->movement = movement;
        }

        int getPriority() override {
            return movement->getCategory() == MovementCategory::STATUS ? 1 : 0;
        }

        int getSpeed() override {
            return attacker->getActivePokemon()->getStats().getSpeed();
        }

        void execute(Battle* battle, LogService* log) override {
            Pokemon* attackerPokemon = attacker->getActivePokemon();
            Trainer* opponent = battle->getOpponentOf(attacker);
            Pokemon* defenderPokemon = opponent->getActivePokemon();

            if(attackerPokemon == NULL || defenderPokemon == NULL || attackerPokemon->getActualHP() <= 0 || defenderPokemon->getActualHP() <= 0) {
                return;
            }

            int damage = DamageCalculator::calculateDamage(attackerPokemon, defenderPokemon, movement);
            if(damage == 0 && movement->getCategory() == MovementCategory::STATUS) {
                log->registerEvent(std::string(attacker->getName()) + "'s " + attackerPokemon->getName() + " used " + movement->getName() + "! But it had no effect!");
                return;
            }

            defenderPokemon->setActualHP(defenderPokemon->getActualHP() - damage);
            if(defenderPokemon->getActualHP() < 0) {
                defenderPokemon->setActualHP(0);
                log->registerEvent(std::string(attacker->getName()) + "'s " + attackerPokemon->getName() + " used " + movement->getName() + "! It dealt " + std::to_string(damage) + " damage! " + std::string(opponent->getName()) + "'s " + defenderPokemon->getName() + " fainted!");
            }

            
        }


};