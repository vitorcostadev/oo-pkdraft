package domain.models.battle;

import controller.LeagueController;
import controller.LogService;
import domain.state.BattleState;

/**
 * Controla as regras de execução dos turnos e gerenciamento dos treinadores envolvidos.
 */
public final class Battle {
    private final Player player;
    private final Player opponent;
    private BattleState state;

    public Battle(Player player, Player opponent) {
        this.player = player;
        this.opponent = opponent;
    }

    /**
     * Identifica se o Pokemon ativo de algum treinador desmaiou e forca a entrada imediata do substituto.
     * @param log Para registrar a substituição forçada.
     */
    public void verifyTrades(LogService log) {
        if (player.getActivePokemon() != null && player.getActivePokemon().isFainted()) tradePokemon(log, player, opponent.getActivePokemon());
        if (opponent.getActivePokemon() != null && opponent.getActivePokemon().isFainted()) tradePokemon(log, opponent, player.getActivePokemon());

    }

    private void tradePokemon(LogService log, Player target, Pokemon opponent){
        Pokemon sub = target.getStrategy()
                .chooseSubstitute(target, opponent);

        if (sub != null) {
            target.setActivePokemon(sub);
            log.registrarEvento(target.getName() +
                    " enviou " + sub.getName() +
                    " para o lugar do Pokemon desmaiado.");
        }
    }

    public void setState(BattleState state) {
        this.state = state;
    }

    public void executarTurno(LeagueController context) {
        if (this.state != null) {
            this.state.execute(this, context);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public Player getOpponent() {
        return opponent;
    }

    public Player getOpponentOf(Player t) {
        return t == this.player ? this.opponent : this.player;
    }
}