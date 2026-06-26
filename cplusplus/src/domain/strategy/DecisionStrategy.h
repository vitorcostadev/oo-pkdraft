#pragma once 
#include "../models/Trainer.h"

class DecisionStrategy {
    public:
        virtual TurnCommand* chooseAction(Trainer* self, Trainer* opponent) = 0;
        virtual Pokemon* chooseSubstitute(Trainer* self, Trainer* opponent) = 0;
        virtual ~DecisionStrategy() = default;
};