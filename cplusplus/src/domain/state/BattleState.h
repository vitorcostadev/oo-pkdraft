#pragma once 
#include "Battle.h"
class CampaignController;

class BattleState {
public:
    virtual void execute(Battle* battle, CampaignController* controller) = 0;
};