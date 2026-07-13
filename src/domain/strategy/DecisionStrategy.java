package domain.strategy;

import domain.commands.TurnCommand;
import domain.models.battle.Pokemon;
import domain.models.battle.Player;

public interface DecisionStrategy {
    TurnCommand chooseAction(Player aliado, Player inimigo);
    Pokemon chooseSubstitute(Player aliado, Pokemon inimigo);
}