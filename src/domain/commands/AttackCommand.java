package domain.commands;

import controller.LogService;
import domain.models.battle.Battle;
import domain.models.battle.Pokemon;
import domain.models.battle.Player;
import domain.models.pokemon.Attribute;
import domain.models.pokemon.Move;
import domain.models.pokemon.MovementCategory;
import services.DamageCalculator;

/**
 * Representa e executa a ação de atacar no seu turno.
 */
public class AttackCommand implements TurnCommand {
    private final Player attacker;
    private final Move move;

    public AttackCommand(Player attacker, Move move) {
        this.attacker = attacker;
        this.move = move;
    }

    @Override
    public int getPriority() {
        return 0;
    } // 0, pois no turno em que ele ataca, a prioridade não existe.

    @Override
    public int getSpeed() {
        Pokemon ativo = attacker.getActivePokemon();
        return ativo != null ? ativo.getStats().getValor(Attribute.SPE) : 0;
    }

    /**
     * Executa a ação de atacar o Pókemon inimigo e registrar no 'log'.
     * @param battle A instância do objeto que representa a battle atual.
     * @param log A instância do objeto 'log' para registrar o evento.
     */
    @Override
    public void execute(Battle battle, LogService log) {
        Pokemon pAtacante = attacker.getActivePokemon();
        Player oponente = battle.getOpponentOf(attacker);
        Pokemon pDefensor = oponente.getActivePokemon();

        if (pAtacante == null || pAtacante.isFainted()
                || pDefensor == null || pDefensor.isFainted()) {
            return;
        }

        log.registrarEvento("[" +
                attacker.getName() + "]: "
                + pAtacante.getName() + " usou  " + move.name() + "!");

        int dano = DamageCalculator.calc(pAtacante, pDefensor, move);

        if (dano == 0 && move.category() != MovementCategory.STATUS) {
            log.registrarEvento("O ataque nao afetou " + pDefensor.getName() + "!");
        } else {
            pDefensor.receiveDamage(dano);
            log.registrarEvento(pDefensor.getName() + " recebeu " + dano + " pontos de dano." + getEffectiveMsg(move, pDefensor));

            if (pDefensor.isFainted()) {
                log.registrarEvento(pDefensor.getName() + " desmaiou!");
            }
        }
    }

    private String getEffectiveMsg(Move move, Pokemon pokemon) {
        double efetividadeTotal = move.type().calcEffectiveness(
                pokemon.getFirst(),
                pokemon.getSecond()
        );

        if (efetividadeTotal > 1.0) {
            return " É super efetivo!";
        } else if (efetividadeTotal < 1.0 && efetividadeTotal > 0.0) {
            return " Não é muito efetivo...";
        }

        return "";
    }
}