package domain.strategy;

import domain.commands.ComandoTurno;
import domain.models.battle.Pokemon;
import domain.models.battle.Treinador;

/**
 * Padroniza a injeção de comportamentos de inteligência no treinador, facilitando
 * a checagem dos estados em batalha e escolher uma determinada ação.
 */
public interface EstrategiaDecisao {
    ComandoTurno escolherAcao(Treinador aliado, Treinador inimigo);
    Pokemon escolherSubstituto(Treinador aliado, Pokemon inimigo);
}