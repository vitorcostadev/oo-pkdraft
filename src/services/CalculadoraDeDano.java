package services;

import java.util.Random;
import domain.models.pokemon.Atributo;
import domain.models.pokemon.CategoriaMovimento;
import domain.models.pokemon.Movimento;
import domain.models.battle.Pokemon;

/**
 * Classe responsável apenas por calcular o dano aplicado aos Pokémons.
 */
public abstract class CalculadoraDeDano {

    /**
     * Calcula a dedução do HP a aplicar no alvo.
     * @param atacante Pokemon originador do ataque.
     * @param defensor Pokemon alvo da ofensiva.
     * @param mov Movimento utilizado.
     * @return Numero inteiro absoluto de pontos de dano a aplicar.
     */
    public static int calcular(Pokemon atacante, Pokemon defensor, Movimento mov) {
        if (mov.categoria() == CategoriaMovimento.STATUS) {
            return 0;
        }

        int nivel = 100, poder = mov.poder(), atributoAtacante = 0 ,atributoDefensor = 0;

        switch (mov.categoria()){
            case FISICO -> {
                atributoAtacante = atacante.getEstatisticas().getValor(Atributo.ATAQUE);
                atributoDefensor = defensor.getEstatisticas().getValor(Atributo.DEFESA);
            }

            case ESPECIAL -> {
                atributoAtacante = atacante.getEstatisticas().getValor(Atributo.ATAQUE_ESPECIAL);
                atributoDefensor = defensor.getEstatisticas().getValor(Atributo.DEFESA_ESPECIAL);
            }

        }

        double stab = 1.0;
        if (atacante.temTipo(mov.tipo())) {
            stab = 1.5;
        }

        double efetividade = mov.tipo().calcularEfetividade(defensor.getTipo1(), defensor.getTipo2());
        if (efetividade == 0.0) {
            return 0;
        }

        Random aleatorizado = new Random();
        double aleatorio = 0.85 + (aleatorizado.nextDouble() * 0.15);

        double modificador = stab * efetividade * aleatorio;

        int baseDano = ((2 * nivel) / 5) + 2;
        double calculoGeral = ((baseDano * poder * ((double) atributoAtacante / atributoDefensor)) / 50.0) + 2;

        return (int) (calculoGeral * modificador);
    }
}