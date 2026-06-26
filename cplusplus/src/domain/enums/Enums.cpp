#include "Enums.h"

const Nature Nature::HARDY  (Attribute::HP, Attribute::HP);
const Nature Nature::LONELY (Attribute::ATTACK, Attribute::DEFENSE);
const Nature Nature::BRAVE  (Attribute::ATTACK, Attribute::SPEED);
const Nature Nature::ADAMANT(Attribute::ATTACK, Attribute::SPECIAL_ATTACK);
const Nature Nature::NAUGHTY(Attribute::ATTACK, Attribute::SPECIAL_DEFENSE);
const Nature Nature::BOLD   (Attribute::DEFENSE, Attribute::ATTACK);
const Nature Nature::DOCILE (Attribute::DEFENSE, Attribute::DEFENSE);
const Nature Nature::RELAXED(Attribute::DEFENSE, Attribute::SPEED);
const Nature Nature::IMPISH (Attribute::DEFENSE, Attribute::SPECIAL_ATTACK);
const Nature Nature::LAX    (Attribute::DEFENSE, Attribute::SPECIAL_DEFENSE);
const Nature Nature::TIMID  (Attribute::SPEED, Attribute::ATTACK);
const Nature Nature::HASTY  (Attribute::SPEED, Attribute::DEFENSE);
const Nature Nature::SERIOUS(Attribute::SPEED, Attribute::SPEED);
const Nature Nature::JOLLY  (Attribute::SPEED, Attribute::SPECIAL_ATTACK);
const Nature Nature::NAIVE  (Attribute::SPEED, Attribute::SPECIAL_DEFENSE);
const Nature Nature::MODEST (Attribute::SPECIAL_ATTACK, Attribute::ATTACK);
const Nature Nature::MILD   (Attribute::SPECIAL_ATTACK, Attribute::DEFENSE);
const Nature Nature::QUIET  (Attribute::SPECIAL_ATTACK, Attribute::SPEED);
const Nature Nature::BASHFUL(Attribute::SPECIAL_ATTACK, Attribute::SPECIAL_ATTACK);
const Nature Nature::RASH   (Attribute::SPECIAL_ATTACK, Attribute::SPECIAL_DEFENSE);
const Nature Nature::CALM   (Attribute::SPECIAL_DEFENSE, Attribute::ATTACK);
const Nature Nature::GENTLE (Attribute::SPECIAL_DEFENSE, Attribute::DEFENSE);
const Nature Nature::SASSY  (Attribute::SPECIAL_DEFENSE, Attribute::SPEED);
const Nature Nature::CAREFUL(Attribute::SPECIAL_DEFENSE, Attribute::SPECIAL_ATTACK);
const Nature Nature::QUIRKY (Attribute::SPECIAL_DEFENSE, Attribute::SPECIAL_DEFENSE);