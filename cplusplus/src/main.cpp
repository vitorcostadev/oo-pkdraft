#include <iostream>
#include "domain/models/Pokemon.h"
#include "domain/models/builder/PokemonBuilder.h"
#include "domain/enums/Enums.h"
#include "domain/models/Movement.h"

using namespace std;

int main(){
    PokemonBuilder builder;
    Pokemon pokemon = builder.setName("Pikachu")
    .setNature(Nature::TIMID)
    .setTypeOne(Type::ELECTRIC)
    .setTypeTwo(Type::FLYING)
    .setBaseStats(Statics(35, 55, 40, 50, 50, 90))
    .setIndividualValues(Statics(31, 31, 31, 31, 31, 31))
    .setEffortValues(Statics(0, 0, 0, 0, 0, 0))
    .addMovement(Movement("Thunder Shock", Type::ELECTRIC, 40, MovementCategory::SPECIAL))
    .addMovement(Movement("Quick Attack", Type::NORMAL, 40, MovementCategory::PHYSICAL))
    .addMovement(Movement("Thunder Wave", Type::ELECTRIC, 0, MovementCategory::STATUS))
    .addMovement(Movement("Agility", Type::PSYCHIC, 0, MovementCategory::STATUS))
    .build();
    cout << "Pokemon created: " << pokemon.getName() << endl;
    return 0;
}