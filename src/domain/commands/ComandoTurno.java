package domain.commands;

import controller.LogService;
import domain.models.battle.Batalha;

public interface ComandoTurno {
    int getPrioridade();
    int getVelocidadeAtor();
    void executar(Batalha batalha, LogService log);
}