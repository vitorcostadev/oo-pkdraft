package controller;

import domain.facade.DataPokemonFacade;
import domain.models.battle.Battle;
import domain.models.battle.Player;
import domain.state.TurnState;
import view.InterfaceJogo;

import java.util.List;

public class LeagueController {

    public void iniciarCampanha(Player jogador, DataPokemonFacade facade, InterfaceJogo view) {
        LogService log = new LogService(view);
        log.registrarEvento("[AVISO] : A batalha vai começar...");

        List<Player> liga;
        liga = facade.loadLeague("Kanto");

        for (int i = 0; i < liga.size(); i++) {
            Player oponente = liga.get(i);
            view.exibirMensagem("\n>>> Novo confronto a vista: " + oponente.getName() + " <<<");
            log.registrarEvento("Partida iniciada contra " + oponente.getName() + ".");

            Battle embate = new Battle(jogador, oponente);
            TurnState motorDeTurnos = new TurnState(log);
            embate.setState(motorDeTurnos);

            while (jogador.hasAlivePokemon() && oponente.hasAlivePokemon()) {
                view.exibirStatusBatalha(embate);
                embate.executarTurno(this);
            }

            if (!jogador.hasAlivePokemon()) {
                view.exibirMensagem("\nTodos os seus Pokémons desmaiaram. Foi derrotado por " + oponente.getName() + ".");
                log.registrarEvento("Resumo: Derrota perante " + oponente.getName() + ".");
                break;
            } else {
                view.exibirMensagem("\nVitória confirmada sobre " + oponente.getName() + "!");
                log.registrarEvento("Resumo: Vitória " + oponente.getName() + ".");

                if (i < liga.size() - 1) {
                    view.aguardarProximoDesafiante();
                }

                jogador.healTeam();
                view.exibirMensagem("[AVISO] : Seus Pokémons tiveram seu HP restaurado.");
            }
        }

        if (jogador.hasAlivePokemon()) {
            view.exibirMensagem("\nCAMPANHA CONCLUÍDA! Você se tornou o mais novo campeão!");
            log.registrarEvento("Resultado final: Campeão Invicto.");
        }

        log.salvarLogEmArquivo();
        view.exibirMensagem("\nSimulação finalizada. Registros salvos com sucesso.");
    }
}