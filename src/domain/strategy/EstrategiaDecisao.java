package domain.strategy;

import domain.commands.ComandoTurno;
import domain.models.battle.Pokemon;
import domain.models.battle.Treinador;

public interface EstrategiaDecisao {
    ComandoTurno escolherAcao(Treinador aliado, Treinador inimigo);
    Pokemon escolherSubstituto(Treinador aliado, Pokemon inimigo);
}