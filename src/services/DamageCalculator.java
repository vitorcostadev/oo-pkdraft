package services;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import domain.models.pokemon.Attribute;
import domain.models.pokemon.Move;
import domain.models.pokemon.MovementCategory;
import domain.models.battle.Pokemon;

/**
 * Classe responsável apenas por calc o dano aplicado aos Pokémons.
 */
public abstract class DamageCalculator {

    /**
     * Calcula a dedução do HP a aplicar no alvo.
     * @param player Pokemon originador do ataque.
     * @param opponent Pokemon alvo da ofensiva.
     * @param mov Move utilizado.
     * @return Numero inteiro absoluto de pontos de dano a aplicar.
     */
    public static int calc(Pokemon player, Pokemon opponent, Move mov) {
        if (mov.category() == MovementCategory.STATUS) {
            return 0;
        }

        int nivel = player.getLevel(), poder = mov.power(),
                atributoAtacante = 0,
                atributoDefensor = 0;

        if(mov.category().equals(MovementCategory.SPECIAL)){
            atributoAtacante = player.getStats().getValor(Attribute.SPA);
            atributoDefensor = opponent.getStats().getValor(Attribute.SPD);
        }else if(mov.category().equals(MovementCategory.PHYSICAL)){
            atributoAtacante = player.getStats().getValor(Attribute.ATK);
            atributoDefensor = opponent.getStats().getValor(Attribute.DEF);
        }

        double stab = 1.0;
        if (player.hasType(mov.type())) {
            stab = 1.5;
        }

        double efetividade = mov.type()
                .calcEffectiveness(opponent.getFirst(), opponent.getSecond());
        if (efetividade == 0.0) {
            return 0;
        }

        Random randomNumber = new Random();
        double random = 0.85 + (randomNumber.nextDouble() * 0.15);

        double modify = stab * efetividade * random;

        int baseDano = ((2 * nivel) / 5) + 2;
        double finalCalc = ((baseDano * poder *
                ((double) atributoAtacante / atributoDefensor)) / 50.0) + 2;

        return (int) (finalCalc * modify);
    }
}