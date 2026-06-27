package domain.strategy;

import domain.commands.ComandoAtacar;
import domain.commands.ComandoTrocar;
import domain.commands.ComandoTurno;
import domain.models.battle.Pokemon;
import domain.models.pokemon.Movimento;
import domain.models.battle.Treinador;
import services.CalculadoraDeDano;

public class HeuristicStrategy implements EstrategiaDecisao {
    private boolean ultimoTurnoFoiTroca = false;

    /**
     * Decide a ação que o pokémon aliado vai exercer (se vai trocar, ou atacar).
      * @param aliado Representa o objeto treinador do utilizador.
     * @param inimigo Representa o objeto treinador inimigo.
     * @return A chamada polimórfica da interface ComandoTurno, que ditará a ação a ser tomada.
     */
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

    /**
     * Faz uma busca em todos os Pokémons disponíveis do treinador aliado, e retornará
     * o melhor Pokémon para substituir o atual ativo.
     * @param aliado Representa o objeto treinador do utilizador.
     * @param inimigo Representa o Pokémon do inimigo.
     * @return O melhor Pokémon para assumir o lugar do antigo ativo.
     */
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

        if (nossaEfetividade == 0.0) {
            return true;
        }

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