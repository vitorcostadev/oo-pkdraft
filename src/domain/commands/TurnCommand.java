package domain.commands;

import controller.LogService;
import domain.models.battle.Battle;

public interface TurnCommand {
    int getPriority();
    int getSpeed();
    void execute(Battle battle, LogService log);
}