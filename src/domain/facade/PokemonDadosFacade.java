package domain.facade;

import java.util.List;
import domain.builder.PokemonBuilder;
import domain.models.battle.Treinador;

public interface PokemonDadosFacade {
    List<PokemonBuilder> obterTresOpcoesAleatorias();
    List<Treinador> carregarLigaDesafiantes(String regiao);
}