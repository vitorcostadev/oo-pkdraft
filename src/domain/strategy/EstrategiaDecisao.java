package domain.strategy;

import domain.commands.ComandoTurno;
import domain.Pokemon;
import domain.models.Treinador;

/**
 * Padroniza a injecao de comportamentos de inteligência no treinador.
 */
public interface EstrategiaDecisao {
    ComandoTurno escolherAcao(Treinador aliado, Treinador inimigo);
    Pokemon escolherSubstituto(Treinador aliado, Pokemon inimigo);
}