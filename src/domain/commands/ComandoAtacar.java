package domain.commands;

import controller.LogService;
import domain.models.CategoriaMovimento;
import domain.Pokemon;
import domain.models.Atributo;
import domain.models.Batalha;
import domain.models.Movimento;
import domain.models.Treinador;
import services.CalculadoraDeDano;

/**
 * Representa e executa a ação de atacar no seu turno.
 */
public class ComandoAtacar implements ComandoTurno {
    private final Treinador atacante;
    private final Movimento movimento;
    private final CalculadoraDeDano calculadora;

    public ComandoAtacar(Treinador atacante, Movimento movimento) {
        this.atacante = atacante;
        this.movimento = movimento;
        this.calculadora = new CalculadoraDeDano();
    }

    @Override
    public int getPrioridade() {
        return 0;
    }

    @Override
    public int getVelocidadeAtor() {
        Pokemon ativo = atacante.getPokemonAtivo();
        return ativo != null ? ativo.getEstatisticas().getValor(Atributo.VELOCIDADE) : 0;
    }

    @Override
    public void executar(Batalha batalha, LogService log) {
        Pokemon pAtacante = atacante.getPokemonAtivo();
        Treinador oponente = batalha.getOponenteDe(atacante);
        Pokemon pDefensor = oponente.getPokemonAtivo();

        if (pAtacante == null || pAtacante.isDesmaiado() || pDefensor == null || pDefensor.isDesmaiado()) {
            return;
        }

        log.registrarEvento(atacante.getNome() + " ordenou que " + pAtacante.getNome() + " usasse " + movimento.getNome() + "!");

        int dano = calculadora.calcular(pAtacante, pDefensor, movimento);

        if (dano == 0 && movimento.getCategoria() != CategoriaMovimento.STATUS) {
            log.registrarEvento("O ataque nao afetou " + pDefensor.getNome() + "!");
        } else {
            pDefensor.receberDano(dano);
            log.registrarEvento(pDefensor.getNome() + " recebeu " + dano + " pontos de dano.");

            if (pDefensor.isDesmaiado()) {
                log.registrarEvento(pDefensor.getNome() + " desmaiou!");
            }
        }
    }
}