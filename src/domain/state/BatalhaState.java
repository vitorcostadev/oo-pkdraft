package domain.state;

import controller.CampanhaController;
import domain.models.battle.Batalha;

/**
 * Define o contrato para os estados mutáveis da batalha.
 */
public interface BatalhaState {
    void executar(Batalha batalha, CampanhaController context);
}