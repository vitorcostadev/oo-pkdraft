#pragma once
#include "../enums/Enums.h"

class Movement{
    private:
        const char* name;
        Type type;
        int power;  
        MovementCategory category;
    public:
        Movement(const char* name, Type type, int power, MovementCategory category) 
            : name(name), type(type), power(power), category(category) {};
        const char* getName() const {return name;};
        Type getType() const {return type;};
        int getPower() const {return power;};
        MovementCategory getCategory() const {return category;};
        ~Movement() {};
};