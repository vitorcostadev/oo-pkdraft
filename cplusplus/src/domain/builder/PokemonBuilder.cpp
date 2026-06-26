#include "PokemonBuilder.h"
#include "../Pokemon.h"

Pokemon PokemonBuilder::build() {
    return Pokemon(*this);
}