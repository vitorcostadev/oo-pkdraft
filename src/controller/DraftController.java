package controller;

import domain.facade.DataPokemonFacade;
import domain.models.battle.Pokemon;
import domain.models.battle.Player;
import domain.models.pokemon.Attribute;
import domain.models.pokemon.Nature;
import domain.strategy.HeuristicStrategy;
import view.InterfaceJogo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DraftController {

    public Player iniciarDraft(DataPokemonFacade facade, InterfaceJogo view) {
        view.exibirMensagem("=== POKEMON DRAFT ===");
        view.exibirMensagem("Prepare-se para escolher uma equipe de 6 Pokémons.");

        Player jogador = new Player("Usuario", new HeuristicStrategy());
        Random aleatorizador = new Random();

        while (jogador.getTeam().size() < 6) {
            List<Pokemon.Builder> opcoesBuilders = facade.getOptions();
            List<Pokemon> opcoes = new ArrayList<>();

            for (Pokemon.Builder b : opcoesBuilders) {
                Nature nat = Nature.values()[aleatorizador.nextInt(Nature.values().length)];
                b.setNature(nat);
                opcoes.add(b.build());
            }

            view.exibirMensagem("\nSelecione o Pokémon n°" + (jogador.getTeam().size() + 1) + ":");

            for (int i = 0; i < opcoes.size(); i++) {
                Pokemon p = opcoes.get(i);
                String linha = String.format("%d - %s | Nature: %s | HP: %d | ATK: %d | DEF: %d | SPA: %d | SPD: %d | SPE: %d",
                        (i + 1),
                        p.getName(),
                        p.getNature().name(),
                        p.getStats().getHp(),
                        p.getStats().getValor(Attribute.ATK),
                        p.getStats().getValor(Attribute.DEF),
                        p.getStats().getValor(Attribute.SPA),
                        p.getStats().getValor(Attribute.SPD),
                        p.getStats().getValor(Attribute.SPE));
                view.exibirMensagem(linha);
            }

            int indiceEscolhido = view.lerInputInteiro(1, opcoes.size());
            Pokemon escolhido = opcoes.get(indiceEscolhido - 1);
            jogador.addPokemon(escolhido);

            view.exibirMensagem("POKÉMON ESCOLHIDO: " + escolhido.getName() + " | Nature: " + escolhido.getNature().name() + " juntou-se a equipe.");
        }

        view.exibirMensagem("\nA sua equipe de Pokémons esta fechada.");
        return jogador;
    }
}