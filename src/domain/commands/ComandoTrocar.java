package domain.commands;

import controller.LogService;
import domain.models.battle.Pokemon;
import domain.models.battle.Batalha;
import domain.models.battle.Treinador;

/**
 * Representa e executa a ação de substituição do Pokemon em campo.
 */
public class ComandoTrocar implements ComandoTurno {
    private final Treinador treinador;
    private final Pokemon substituto;

    public ComandoTrocar(Treinador treinador, Pokemon substituto) {
        this.treinador = treinador;
        this.substituto = substituto;
    }

    @Override
    public int getPrioridade() {
        return 1;
    }

    @Override
    public int getVelocidadeAtor() {
        return 0;
    }

    /**
     * Executa a troca do Pokémon ativo com o substituto.
     * @param batalha A instância atual do objeto Batalha.
     * @param log A instância atual do objeto LogService.
     */
    @Override
    public void executar(Batalha batalha, LogService log) {
        Pokemon atual = treinador.getPokemonAtivo();

        if (atual != null && !atual.isDesmaiado()) {
            log.registrarEvento(treinador.getNome() + " recuou o " + atual.getNome() + ".");
        }

        treinador.colocarComoAtivo(substituto);
        log.registrarEvento(treinador.getNome() + " enviou o " + substituto.getNome() + " para a batalha!");
    }
}