package domain.facade;

import java.util.List;
import domain.builder.PokemonBuilder;
import domain.models.battle.Treinador;

/**
 * Contrato de fachada para isolar a complexidade da obtenção de dados externos.
 */
public interface PokemonDadosFacade {
    List<PokemonBuilder> obterTresOpcoesAleatorias();
    List<Treinador> carregarLigaDesafiantes(String regiao);
}