package domain.models.battle;

import controller.CampanhaController;
import controller.LogService;
import domain.state.BatalhaState;

/**
 * Controla as regras de execução dos turnos e gerenciamento dos treinadores envolvidos.
 */
public class Batalha {
    private final Treinador jogador;
    private final Treinador oponente;
    private BatalhaState estadoAtual;

    public Batalha(Treinador jogador, Treinador oponente) {
        this.jogador = jogador;
        this.oponente = oponente;
    }

    /**
     * Identifica se o Pokemon ativo de algum treinador desmaiou e forca a entrada imediata do substituto.
     * @param log Para registrar a substituição forçada.
     */
    public void verificarEForcarSubstituicoes(LogService log) {
        if (this.jogador.getPokemonAtivo() != null && this.jogador.getPokemonAtivo().isDesmaiado()) {
            Pokemon sub = this.jogador.getEstrategia().escolherSubstituto(this.jogador, this.oponente.getPokemonAtivo());
            if (sub != null) {
                this.jogador.colocarComoAtivo(sub);
                log.registrarEvento(this.jogador.getNome() + " enviou " + sub.getNome() + " para o lugar do Pokemon desmaiado.");
            }
        }
        if (this.oponente.getPokemonAtivo() != null && this.oponente.getPokemonAtivo().isDesmaiado()) {
            Pokemon sub = this.oponente.getEstrategia().escolherSubstituto(this.oponente, this.jogador.getPokemonAtivo());
            if (sub != null) {
                this.oponente.colocarComoAtivo(sub);
                log.registrarEvento(this.oponente.getNome() + " enviou " + sub.getNome() + " para o lugar do Pokemon desmaiado.");
            }
        }
    }

    public void setEstado(BatalhaState estado) {
        this.estadoAtual = estado;
    }

    public void executarTurno(CampanhaController context) {
        if (this.estadoAtual != null) {
            this.estadoAtual.executar(this, context);
        }
    }

    public Treinador getJogador() {
        return jogador;
    }

    public Treinador getOponente() {
        return oponente;
    }

    public Treinador getOponenteDe(Treinador t) {
        return t == this.jogador ? this.oponente : this.jogador;
    }
}