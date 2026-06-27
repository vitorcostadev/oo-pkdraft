package domain.state;

import controller.CampanhaController;
import controller.LogService;
import domain.commands.ComandoTurno;
import domain.models.battle.Batalha;
/**
 * Aplica a ordem de prioridades e resolve as ações preparadas para o turno corrente.
 */
public class TurnoState implements BatalhaState {
    private final LogService log;

    public TurnoState(LogService log) {
        this.log = log;
    }

    /**
     * Decide a ordem de prioridades dos Pokémons ativos e solicita a ação dos Pokémons
     * dos treinadores.
     * @param batalha A instância do objeto Batalha atual.
     * @param context O contexto da campanha (quem ele está enfrentando).
     */
    @Override
    public void executar(Batalha batalha, CampanhaController context) {
        batalha.verificarEForcarSubstituicoes(this.log);

        if (!batalha.getJogador().temPokemonApto() || !batalha.getOponente().temPokemonApto()) {
            return;
        }

        ComandoTurno acaoJogador = batalha.getJogador().solicitarAcao(batalha.getOponente());
        ComandoTurno acaoOponente = batalha.getOponente().solicitarAcao(batalha.getJogador());

        resolverAcoes(batalha, acaoJogador, acaoOponente);
    }

    private void resolverAcoes(Batalha batalha, ComandoTurno c1, ComandoTurno c2) {
        ComandoTurno primeiro = c1;
        ComandoTurno segundo = c2;

        if (c2.getPrioridade() > c1.getPrioridade()) {
            primeiro = c2;
            segundo = c1;
        } else if (c2.getPrioridade() == c1.getPrioridade()) {
            if (c2.getVelocidadeAtor() > c1.getVelocidadeAtor()) {
                primeiro = c2;
                segundo = c1;
            }
        }

        primeiro.executar(batalha, this.log);
        segundo.executar(batalha, this.log);
    }
}