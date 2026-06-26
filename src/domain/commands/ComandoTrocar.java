package domain.commands;

import controller.LogService;
import domain.Pokemon;
import domain.models.Batalha;
import domain.models.Treinador;

/**
 * Representa e executa a acao tática de substituicao do Pokemon em campo.
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