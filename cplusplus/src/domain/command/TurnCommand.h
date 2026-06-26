#pragma once 
#include "../models/Battle.h"

class LogService;
class TurnCommand{
    public:
        virtual int getPriority() = 0;
        virtual int getSpeed() = 0;
        virtual void execute(Battle* battle, LogService* log) = 0;
        virtual ~TurnCommand() = default;
};