#pragma once 
#include "../state/BattleState.h"
#include "Trainer.h"
class LogService;

class Battle {
    private:
        Trainer* player, *opponent;
        BattleState* currentState;
    public:
        Battle(Trainer* player, Trainer* opponent) : player(player), opponent(opponent), currentState(nullptr) {}

        virtual void setState(BattleState* state) = 0;
        virtual void verifyAndForceSubstitute(LogService* log) = 0;
        // execute
        Trainer* getPlayer() { return player; }
        Trainer* getOpponent() { return opponent; }
        Trainer* getOpponentOf(Trainer* trainer) { return (trainer == player) ? opponent : player; }
        virtual ~Battle() = default;
};