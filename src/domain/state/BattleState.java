package domain.state;

import controller.LeagueController;
import domain.models.battle.Battle;


public interface BattleState {
    void execute(Battle battle, LeagueController context);
}