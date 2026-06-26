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
        log.registrarEvento("[AVISO] : A batalha vai começar...");

        List<Treinador> liga = facade.carregarLigaDesafiantes("Kanto");

        for (int i = 0; i < liga.size(); i++) {
            Treinador oponente = liga.get(i);
            view.exibirMensagem("\n>>> Novo confronto a vista: " + oponente.getNome() + " <<<");
            log.registrarEvento("Partida iniciada contra " + oponente.getNome() + ".");

            Batalha embate = new Batalha(jogador, oponente);
            TurnoState motorDeTurnos = new TurnoState(log);
            embate.setEstado(motorDeTurnos);

            while (jogador.temPokemonApto() && oponente.temPokemonApto()) {
                view.exibirStatusBatalha(embate);
                embate.executarTurno(this);
            }

            if (!jogador.temPokemonApto()) {
                view.exibirMensagem("\nTodos os seus Pokemons desmaiaram. Foi derrotado por " + oponente.getNome() + ".");
                log.registrarEvento("Resumo: Derrota perante " + oponente.getNome() + ".");
                break;
            } else {
                view.exibirMensagem("\nVitória confirmada sobre " + oponente.getNome() + "!");
                log.registrarEvento("Resumo: Vitória " + oponente.getNome() + ".");

                if (i < liga.size() - 1) {
                    view.aguardarProximoDesafiante();
                }

                jogador.curarEquipe();
                view.exibirMensagem("[AVISO] : Seus pokémons tiveram seu HP restaurado.");
            }
        }

        if (jogador.temPokemonApto()) {
            view.exibirMensagem("\nCAMPANHA CONCLUIDA! Você se tornou o mais novo campeão!");
            log.registrarEvento("Resultado final: Campeão Invicto.");
        }

        log.salvarLogEmArquivo();
        view.exibirMensagem("\nSimulação finalizada. Registos salvos com sucesso.");
    }
}