package domain.commands;

import controller.LogService;
import domain.models.battle.Batalha;
import domain.models.battle.Pokemon;
import domain.models.battle.Treinador;
import domain.models.pokemon.Atributo;
import domain.models.pokemon.CategoriaMovimento;
import domain.models.pokemon.Movimento;
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
    } // 0, pois no turno em que ele ataca, a prioridade não existe.

    @Override
    public int getVelocidadeAtor() {
        Pokemon ativo = atacante.getPokemonAtivo();
        return ativo != null ? ativo.getEstatisticas().getValor(Atributo.VELOCIDADE) : 0;
    }

    /**
     * Executa a ação de atacar o Pókemon inimigo e registrar no 'log'.
     * @param batalha A instância do objeto que representa a batalha atual.
     * @param log A instância do objeto 'log' para registrar o evento.
     */
    @Override
    public void executar(Batalha batalha, LogService log) {
        Pokemon pAtacante = atacante.getPokemonAtivo();
        Treinador oponente = batalha.getOponenteDe(atacante);
        Pokemon pDefensor = oponente.getPokemonAtivo();

        if (pAtacante == null || pAtacante.isDesmaiado() || pDefensor == null || pDefensor.isDesmaiado()) {
            return;
        }

        log.registrarEvento("[" +
                atacante.getNome() + "]: "
                + pAtacante.getNome() + " usou  " + movimento.nome() + "!");

        int dano = calculadora.calcular(pAtacante, pDefensor, movimento);

        if (dano == 0 && movimento.categoria() != CategoriaMovimento.STATUS) {
            log.registrarEvento("O ataque nao afetou " + pDefensor.getNome() + "!");
        } else {
            pDefensor.receberDano(dano);
            log.registrarEvento(pDefensor.getNome() + " recebeu " + dano + " pontos de dano." + getEffectiveMsg(movimento, pDefensor));

            if (pDefensor.isDesmaiado()) {
                log.registrarEvento(pDefensor.getNome() + " desmaiou!");
            }
        }
    }

    private String getEffectiveMsg(Movimento movimento, Pokemon pokemon) {
        double efetividadeTotal = movimento.tipo().calcularEfetividade(
                pokemon.getTipo1(),
                pokemon.getTipo2()
        );

        if (efetividadeTotal > 1.0) {
            return " É super efetivo!";
        } else if (efetividadeTotal < 1.0 && efetividadeTotal > 0.0) {
            return " Não é muito efetivo...";
        }

        return "";
    }
}