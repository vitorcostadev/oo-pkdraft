package domain.commands;

import controller.LogService;
import domain.models.Batalha;

/**
 * Contrato para execução padronizada das ações de turno.
 */
public interface ComandoTurno {
    int getPrioridade();
    int getVelocidadeAtor();
    void executar(Batalha batalha, LogService log);
}