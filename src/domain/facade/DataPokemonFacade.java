package domain.facade;

import java.util.List;
import domain.models.battle.Pokemon;
import domain.models.battle.Player;

public interface DataPokemonFacade {
    List<Pokemon.Builder> getOptions();
    List<Player> loadLeague(String region);
}