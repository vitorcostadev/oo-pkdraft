package controller;

import domain.facade.PokemonDadosFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import domain.strategy.HeuristicStrategy;
import domain.models.pokemon.Atributo;
import domain.models.pokemon.Natureza;
import domain.models.battle.Pokemon;
import domain.builder.PokemonBuilder;
import domain.models.battle.Treinador;
import view.CLIView;

/**
 * Controla o laco de recrutamento restrito e formacao da equipe do utilizador.
 */
public class DraftController {

    public Treinador iniciarDraft(PokemonDadosFacade facade, CLIView view) {
        view.exibirMensagem("=== POKEMON DRAFT ===");
        view.exibirMensagem("Prepare-se para escolher uma equipe de 6 pokemons.\n");

        Treinador jogador = new Treinador("Usuario", new HeuristicStrategy());
        Random aleatorizador = new Random();
        Natureza[] catalogoNaturezas = Natureza.values();

        while (jogador.getEquipe().size() < 6) {
            List<PokemonBuilder> opcoesBuilders = facade.obterTresOpcoesAleatorias();
            List<Pokemon> opcoes = new ArrayList<>();

            for (PokemonBuilder b : opcoesBuilders) {
                Natureza nat = catalogoNaturezas[aleatorizador.nextInt(catalogoNaturezas.length)];
                b.setNatureza(nat);
                opcoes.add(b.build());
            }

            view.exibirMensagem("\nSelecione o pokemon n°" + (jogador.getEquipe().size() + 1) + ":");

            for (int i = 0; i < opcoes.size(); i++) {
                Pokemon p = opcoes.get(i);
                String linha = String.format("%d - %s | Natureza: %s | HP: %d | ATK: %d | DEF: %d | SPA: %d | SPD: %d | SPE: %d",
                        (i + 1),
                        p.getNome(),
                        p.getNatureza().name(),
                        p.getEstatisticas().getHp(),
                        p.getEstatisticas().getValor(Atributo.ATAQUE),
                        p.getEstatisticas().getValor(Atributo.DEFESA),
                        p.getEstatisticas().getValor(Atributo.ATAQUE_ESPECIAL),
                        p.getEstatisticas().getValor(Atributo.DEFESA_ESPECIAL),
                        p.getEstatisticas().getValor(Atributo.VELOCIDADE));
                view.exibirMensagem(linha);
            }

            int indiceEscolhido = view.lerInputInteiro(1, opcoes.size());
            Pokemon escolhido = opcoes.get(indiceEscolhido - 1);
            jogador.adicionarPokemon(escolhido);

            view.exibirMensagem("Escolha registrada: " + escolhido.getNome() + " | Natureza: " + escolhido.getNatureza().name() + " juntou-se a equipe.");
        }

        view.exibirMensagem("\nA sua equipe de pokemons esta fechada.");
        return jogador;
    }
}