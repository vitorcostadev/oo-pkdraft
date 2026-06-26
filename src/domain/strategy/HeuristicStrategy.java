package domain.strategy;

import domain.commands.ComandoAtacar;
import domain.commands.ComandoTrocar;
import domain.commands.ComandoTurno;
import domain.Pokemon;
import domain.models.Movimento;
import domain.models.Treinador;
import services.CalculadoraDeDano;

/**
 * Implementa a lógica de decisao autonoma baseada em heuristica de risco e efetividade elementar.
 */
public class HeuristicStrategy implements EstrategiaDecisao {
    private boolean ultimoTurnoFoiTroca = false;

    @Override
    public ComandoTurno escolherAcao(Treinador aliado, Treinador inimigo) {
        Pokemon ativo = aliado.getPokemonAtivo();
        Pokemon alvo = inimigo.getPokemonAtivo();

        if (deveTrocar(aliado, alvo) && !this.ultimoTurnoFoiTroca) {
            Pokemon substituto = escolherSubstituto(aliado, alvo);
            if (substituto != null && substituto != ativo) {
                this.ultimoTurnoFoiTroca = true;
                return new ComandoTrocar(aliado, substituto);
            }
        }

        this.ultimoTurnoFoiTroca = false;
        Movimento melhorMovimento = obterMelhorMovimento(ativo, alvo);
        return new ComandoAtacar(aliado, melhorMovimento);
    }

    @Override
    public Pokemon escolherSubstituto(Treinador aliado, Pokemon inimigo) {
        Pokemon melhor = null;
        double melhorScore = -100.0;

        for (Pokemon candidato : aliado.getEquipe()) {
            if (candidato.isDesmaiado() || candidato == aliado.getPokemonAtivo()) {
                continue;
            }

            double scoreDefensivo = 4.0 - calcularRiscoTipagem(inimigo, candidato);
            double scoreOfensivo = calcularRiscoTipagem(candidato, inimigo);
            double scoreTotal = scoreDefensivo + scoreOfensivo;

            if (scoreTotal > melhorScore) {
                melhorScore = scoreTotal;
                melhor = candidato;
            }
        }

        return melhor;
    }

    private boolean deveTrocar(Treinador aliado, Pokemon inimigo) {
        Pokemon ativo = aliado.getPokemonAtivo();
        double efetividadeInimiga = calcularRiscoTipagem(inimigo, ativo);
        double nossaEfetividade = calcularRiscoTipagem(ativo, inimigo);

        return efetividadeInimiga >= 2.0 && nossaEfetividade <= 0.5;
    }

    private double calcularRiscoTipagem(Pokemon atacante, Pokemon defensor) {
        double risco1 = atacante.getTipo1().calcularEfetividade(defensor.getTipo1(), defensor.getTipo2());
        double risco2 = 0.0;

        if (atacante.getTipo2() != null) {
            risco2 = atacante.getTipo2().calcularEfetividade(defensor.getTipo1(), defensor.getTipo2());
        }

        return Math.max(risco1, risco2);
    }

    private Movimento obterMelhorMovimento(Pokemon atacante, Pokemon defensor) {
        CalculadoraDeDano calculadora = new CalculadoraDeDano();
        Movimento melhor = atacante.getMovimentos().get(0);
        int maiorDano = -1;

        for (Movimento mov : atacante.getMovimentos()) {
            int dano = calculadora.calcular(atacante, defensor, mov);
            if (dano > maiorDano) {
                maiorDano = dano;
                melhor = mov;
            }
        }

        return melhor;
    }
}