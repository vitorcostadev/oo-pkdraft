package domain.state;

import controller.CampanhaController;
import domain.models.battle.Batalha;


public interface BatalhaState {
    void executar(Batalha batalha, CampanhaController context);
}