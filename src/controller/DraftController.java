package controller;

import integration.PokemonDadosFacade;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import domain.strategy.IAHeuristicaStrategy;
import domain.models.Atributo;
import domain.models.Natureza;
import domain.Pokemon;
import domain.PokemonBuilder;
import domain.models.Treinador;
import view.CLIView;

/**
 * Controla o laço de repetição responsável pela seletividade restrita e formação da equipa inicial.
 */
public class DraftController {

    public Treinador iniciarDraft(PokemonDadosFacade facade, CLIView view) {
        view.exibirMensagem("=== POKEMON DRAFT ===");
        view.exibirMensagem("Prepare-se para escolher uma equipe de 6 pokémons.\n");

        Treinador jogador = new Treinador("Usuário", new IAHeuristicaStrategy());
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

            view.exibirMensagem("\nSelecione o pokémon n°" + (jogador.getEquipe().size() + 1) + ":");

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

            view.exibirMensagem("Escolha registrada: " + escolhido.getNome() + " de natureza " + escolhido.getNatureza().name() + " juntou-se a equipe.");
        }

        view.exibirMensagem("\nA sua equipe de combate esta fechada.");
        return jogador;
    }
}