#pragma once

#include "BattleState.h"
#include "../command/TurnCommand.h"    

class LogService;
class CampaignController;

class TurnState : public BattleState {
    private:
        LogService* log;

        void resolveActions(Battle* battle,TurnCommand* c1,TurnCommand* c2){
            TurnCommand* first = c1;
            TurnCommand* second = c2;

            if(c1->getPriority() < c2->getPriority()) {
                first = c2;
                second = c1;
            }else if(c1->getPriority() == c2->getPriority()) {
                if(c1->getSpeed() > c2->getSpeed()) {
                    first = c2;
                    second = c1;
                }
            }

            first->execute(battle, this->log);
            second->execute(battle, this->log);
        }
    public:
        TurnState(LogService* log) : log(log) {};

        void execute(Battle* battle, CampaignController* controller) override {
            battle->verifyAndForceSubstitute(log);
            if(!battle->getPlayer()->hasAvailablePokemon() || !battle->getOpponent()->hasAvailablePokemon()) {
                return;
            }

            TurnCommand* playerCommand = battle->getPlayer()->soliciteAction(battle->getOpponent());
            TurnCommand* opponentCommand = battle->getOpponent()->soliciteAction(battle->getPlayer());

            resolveActions(battle, playerCommand, opponentCommand);
        }
};