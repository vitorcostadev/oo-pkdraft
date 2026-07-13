package domain.strategy;

import domain.commands.AttackCommand;
import domain.commands.TradeCommand;
import domain.commands.TurnCommand;
import domain.models.battle.Pokemon;
import domain.models.battle.Player;
import domain.models.pokemon.Move;
import services.DamageCalculator;

public class HeuristicStrategy implements DecisionStrategy {
    private boolean lastTurnWasTrade;

    /**
     * Decide a ação que o Pokémon player vai exercer (se vai trocar, ou atacar).
      * @param player Representa o objeto treinador do utilizador.
     * @param opponent Representa o objeto treinador opponent.
     * @return A chamada polimórfica da ‘interface’ TurnCommand, que ditará a ação a ser tomada.
     */
    @Override
    public TurnCommand chooseAction(Player player, Player opponent) {
        Pokemon active = player.getActivePokemon();
        Pokemon alvo = opponent.getActivePokemon();

        if (deveTrocar(player, alvo) && !this.lastTurnWasTrade) {
            Pokemon substituto = chooseSubstitute(player, alvo);
            if (substituto != null && substituto != active) {
                this.lastTurnWasTrade = true;
                return new TradeCommand(player, substituto);
            }
        }

        this.lastTurnWasTrade = false;
        Move melhorMove = getBestMove(active, alvo);
        return new AttackCommand(player, melhorMove);
    }

    /**
     * Faz uma busca em todos os Pokémons disponíveis do treinador player, e retornará
     * o melhor Pokémon para substituir o atual active.
     * @param player Representa o objeto treinador do utilizador.
     * @param opponent Representa o Pokémon do opponent.
     * @return O melhor Pokémon para assumir o lugar do antigo active.
     */
    @Override
    public Pokemon chooseSubstitute(Player player, Pokemon opponent) {
        Pokemon melhor = null;
        double melhorScore = -100.0;

        for (Pokemon candidato : player.getTeam()) {
            if (candidato.isFainted() || candidato == player.getActivePokemon()) {
                continue;
            }

            double scoreDefensivo = 4.0 - calcRiskType(opponent, candidato);
            double scoreOfensivo = calcRiskType(candidato, opponent);
            double scoreTotal = scoreDefensivo + scoreOfensivo;

            if (scoreTotal > melhorScore) {
                melhorScore = scoreTotal;
                melhor = candidato;
            }
        }

        return melhor;
    }

    private boolean deveTrocar(Player player, Pokemon opponent) {
        double efetividadeInimiga = calcRiskType(
                opponent, player.getActivePokemon());
        double nossaEfetividade = calcRiskType(player.getActivePokemon(), opponent);

        return nossaEfetividade == 0D || (efetividadeInimiga >= 2D && nossaEfetividade <= 0.5);
    }

    private double calcRiskType(Pokemon player, Pokemon opponent) {
        double risco1 = player.getFirst()
                .calcEffectiveness(opponent.getFirst(), opponent.getSecond());
        double risco2 = 0D;

        if (player.getSecond() != null) {
            risco2 = player.getSecond()
                    .calcEffectiveness(opponent.getFirst(), opponent.getSecond());
        }

        return Math.max(risco1, risco2);
    }

    private Move getBestMove(Pokemon player, Pokemon opponent) {
        Move melhor = player.getMovimentos().getFirst();
        int maiorDano = -1;

        for (Move mov : player.getMovimentos()) {
            int dano = DamageCalculator.calc(player, opponent, mov);
            if (dano > maiorDano) {
                maiorDano = dano;
                melhor = mov;
            }
        }

        return melhor;
    }
}