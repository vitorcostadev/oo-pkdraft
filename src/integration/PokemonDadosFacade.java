package integration;

import java.util.List;
import domain.PokemonBuilder;
import domain.models.Treinador;

/**
 * Contrato de fachada para isolar a complexidade da obtenção de dados externos.
 */
public interface PokemonDadosFacade {
    List<PokemonBuilder> obterTresOpcoesAleatorias();
    List<Treinador> carregarLigaDesafiantes(String regiao);
}