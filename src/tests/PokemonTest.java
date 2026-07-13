package tests;

import domain.models.battle.Pokemon;
import domain.models.pokemon.Estatisticas;
import domain.models.pokemon.Nature;
import domain.models.pokemon.Type;

public class PokemonTest {
    static void main() {
        Pokemon pokemon = new Pokemon.Builder()
                .setNature(Nature.ADAMANT)
                .setEvs(new Estatisticas(0, 252, 0, 0, 0, 252))
                .setBaseStats(new Estatisticas(90, 150, 70, 50, 90, 145))
                .setLevel(100)
                .setTypes(Type.FIRE, Type.DRAGON)
                .build();

        System.out.println(pokemon.getStats());
    }
}
