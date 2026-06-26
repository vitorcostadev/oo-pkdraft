package controller;

import integration.PokemonDadosFacade;
import java.util.List;
import domain.models.Batalha;
import domain.models.Treinador;
import domain.state.TurnoState;
import view.CLIView;

/**
 * Controla a sequencia de batalhas contra os integrantes da Liga Pokemon.
 */
public class CampanhaController {

    public void iniciarCampanha(Treinador jogador, PokemonDadosFacade facade, CLIView view) {
        LogService log = new LogService(view);
        log.registrarEvento("Aviso: Inicio da Campanha Principal.");

        List<Treinador> liga = facade.carregarLigaDesafiantes("Kanto");

        for (int i = 0; i < liga.size(); i++) {
            Treinador oponente = liga.get(i);
            view.exibirMensagem("\n>>> NOVO CONFRONTO DETECTADO: " + oponente.getNome() + " <<<");
            log.registrarEvento("Batalha iniciada contra " + oponente.getNome() + ".");

            Batalha embate = new Batalha(jogador, oponente);
            TurnoState motorDeTurnos = new TurnoState(log);
            embate.setEstado(motorDeTurnos);

            while (jogador.temPokemonApto() && oponente.temPokemonApto()) {
                view.exibirStatusBatalha(embate);
                embate.executarTurno(this);
            }

            if (!jogador.temPokemonApto()) {
                view.exibirMensagem("\nTodos os seus Pokemons desmaiaram. Foi derrotado por " + oponente.getNome() + ".");
                log.registrarEvento("Desfecho: Derrota critica perante " + oponente.getNome() + ".");
                break;
            } else {
                view.exibirMensagem("\nVitoria confirmada sobre " + oponente.getNome() + "!");
                log.registrarEvento("Desfecho: Vitoria tatica contra " + oponente.getNome() + ".");

                if (i < liga.size() - 1) {
                    view.aguardarProximoDesafiante();
                }

                jogador.curarEquipe();
                view.exibirMensagem("A equipe medica restaurou as condicoes vitais de toda a sua equipe.\n");
            }
        }

        if (jogador.temPokemonApto()) {
            view.exibirMensagem("\nCAMPANHA CONCLUIDA! Foi coroado o novo Campeao do simulador.");
            log.registrarEvento("Estatuto final: Campeao Invicto.");
        }

        log.salvarLogEmArquivo();
        view.exibirMensagem("\nSessao terminada. Registos salvos com sucesso.");
    }
}