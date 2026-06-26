package services;

import java.util.Random;
import domain.models.Atributo;
import domain.models.CategoriaMovimento;
import domain.models.Movimento;
import domain.Pokemon;

/**
 * Isola as formulas oficiais de calculo de efetividade e dano.
 */
public class CalculadoraDeDano {

    /**
     * Computa a dedução de pontos de vida a aplicar no alvo.
     * @param atacante Pokemon originador do ataque.
     * @param defensor Pokemon alvo da ofensiva.
     * @param mov Movimento utilizado.
     * @return Numero inteiro absoluto de pontos de dano a aplicar.
     */
    public int calcular(Pokemon atacante, Pokemon defensor, Movimento mov) {
        if (mov.getCategoria() == CategoriaMovimento.STATUS) {
            return 0;
        }

        int nivel = 50;
        int poder = mov.getPoder();

        int atributoAtacante;
        int atributoDefensor;

        if (mov.getCategoria() == CategoriaMovimento.FISICO) {
            atributoAtacante = atacante.getEstatisticas().getValor(Atributo.ATAQUE);
            atributoDefensor = defensor.getEstatisticas().getValor(Atributo.DEFESA);
        } else {
            atributoAtacante = atacante.getEstatisticas().getValor(Atributo.ATAQUE_ESPECIAL);
            atributoDefensor = defensor.getEstatisticas().getValor(Atributo.DEFESA_ESPECIAL);
        }

        double stab = 1.0;
        if (atacante.temTipo(mov.getTipo())) {
            stab = 1.5;
        }

        double efetividade = mov.getTipo().calcularEfetividade(defensor.getTipo1(), defensor.getTipo2());
        if (efetividade == 0.0) {
            return 0;
        }

        Random aleatorizador = new Random();
        double aleatorio = 0.85 + (aleatorizador.nextDouble() * 0.15);

        double modificador = stab * efetividade * aleatorio;

        int baseDano = ((2 * nivel) / 5) + 2;
        double calculoGeral = ((baseDano * poder * ((double) atributoAtacante / atributoDefensor)) / 50.0) + 2;

        return (int) (calculoGeral * modificador);
    }
}