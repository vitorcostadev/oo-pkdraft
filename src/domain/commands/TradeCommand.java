package domain.commands;

import controller.LogService;
import domain.models.battle.Pokemon;
import domain.models.battle.Battle;
import domain.models.battle.Player;

/**
 * Representa e executa a ação de substituição do Pokemon em campo.
 */
public class TradeCommand implements TurnCommand {
    private final Player player;
    private final Pokemon substituto;

    public TradeCommand(Player player, Pokemon substituto) {
        this.player = player;
        this.substituto = substituto;
    }

    @Override
    public int getPriority() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    /**
     * Executa a troca do Pokémon ativo com o substituto.
     * @param battle A instância atual do objeto Battle.
     * @param log A instância atual do objeto LogService.
     */
    @Override
    public void execute(Battle battle, LogService log) {
        Pokemon atual = player.getActivePokemon();

        if (atual != null && !atual.isFainted()) {
            log.registrarEvento(player.getName() + " recuou o " + atual.getName() + ".");
        }

        player.setActivePokemon(substituto);
        log.registrarEvento(player.getName() + " enviou o " + substituto.getName() + " para a battle!");
    }
}