package view;

import domain.models.battle.Battle;

/**
 * Contrato formado exclusivamente para os view (facilitar a implementação de outros view's)
 * Permite que as regras de negócio operem de forma indiferente ao dispositivo de saída.
 */
public interface InterfaceJogo {
    void exibirMensagem(String mensagem);
    void aguardarProximoDesafiante();
    void exibirStatusBatalha(Battle battle);
    int lerInputInteiro(int min, int max);
}