package domain.commands;

import controller.LogService;
import domain.models.*;
import domain.Pokemon;
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

    private String getEffectiveMsg(Movimento movimento, Pokemon pokemon){
        if(movimento.tipo().obterEfetividadeBase(null, pokemon.getTipo1()) >= 2D || movimento.tipo().obterEfetividadeBase(null, pokemon.getTipo2()) >=2D
        ) return " é super efetivo!";
        else if(movimento.tipo().obterEfetividadeBase(null, pokemon.getTipo1()) == 1D || movimento.tipo().obterEfetividadeBase(null, pokemon.getTipo2()) == 1D){
            return "";
        }else return " não é nada efetivo...";

    }
}