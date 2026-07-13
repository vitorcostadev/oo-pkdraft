package domain.state;

import controller.LeagueController;
import controller.LogService;
import domain.commands.TurnCommand;
import domain.models.battle.Battle;
/**
 * Aplica a ordem de prioridades e resolve as ações preparadas para o turno atual.
 */
public class TurnState implements BattleState {
    private final LogService log;

    public TurnState(LogService log) {
        this.log = log;
    }

    /**
     * Decide a ordem de prioridades dos Pokémons ativos e solicita a ação dos Pokémons
     * dos treinadores.
     * @param battle A instância do objeto Battle atual.
     * @param context O contexto da campanha (quem ele está enfrentando).
     */
    @Override
    public void execute(Battle battle, LeagueController context) {
        battle.verifyTrades(this.log);

        if (!battle.getPlayer().hasAlivePokemon() || !battle.getOpponent().hasAlivePokemon()) {
            return;
        }

        TurnCommand playerAction = battle.getPlayer().soliciteAction(battle.getOpponent());
        TurnCommand opponentAction = battle.getOpponent().soliciteAction(battle.getPlayer());

        resolveActions(battle, playerAction, opponentAction);
    }

    /**
     * Responsável por ditar as prioridades dos Pokémons ativos na Battle atual, para depois
     * avançar para execução da ação no turno (atacar ou trocar).
     * @param battle A battle atual que está a ocorrer.
     * @param c1 A ação a ser tomada pelo first treinador
     * @param c2 A ação a ser tomada pelo second treinador
     */
    private void resolveActions(Battle battle, TurnCommand c1, TurnCommand c2) {
        TurnCommand first = c1, second = c2;

        if (c2.getPriority() > c1.getPriority()) {
            first = c2; second = c1;
        } else if (c2.getPriority() == c1.getPriority()) {
            if (c2.getSpeed() > c1.getSpeed()) {
                first = c2; second = c1;
            }
        }

        first.execute(battle, this.log);
        second.execute(battle, this.log);
    }
}