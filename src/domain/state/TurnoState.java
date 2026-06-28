package domain.state;

import controller.CampanhaController;
import controller.LogService;
import domain.commands.ComandoTurno;
import domain.models.battle.Batalha;
/**
 * Aplica a ordem de prioridades e resolve as ações preparadas para o turno atual.
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

    /**
     * Responsável por ditar as prioridades dos Pokémons ativos na Batalha atual, para depois
     * avançar para execução da ação no turno (atacar ou trocar).
     * @param batalha A batalha atual que está a ocorrer.
     * @param c1 A ação a ser tomada pelo primeiro treinador
     * @param c2 A ação a ser tomada pelo segundo treinador
     */
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

        /*
        Após decidir as prioridades no turno, ele chama o método
        executar() do primeiro e segundo, sempre garantido
        que o mais prioritário / rápido execute a sua ação primeiro.
         */
        primeiro.executar(batalha, this.log);
        segundo.executar(batalha, this.log);
    }
}