package view;

import java.util.Scanner;
import domain.models.battle.Batalha;
import domain.models.battle.Pokemon;

/**
 * Controla as interações de exibição e leitura de dados no terminal de comandos.
 */
public class CLIView {
    private final Scanner leitor;

    public CLIView() {
        this.leitor = new Scanner(System.in);
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    /**
     * Bloqueia o andamento do software ate que o usuario pressione a tecla Enter para mudar de oponente.
     */
    public void aguardarProximoDesafiante() {
        System.out.println("\n[Confronto encerrado. Pressione ENTER para avancar para o proximo oponente da Liga]");
        this.leitor.nextLine();
    }

    public void exibirStatusBatalha(Batalha batalha) {
        Pokemon p1 = batalha.getJogador().getPokemonAtivo();
        Pokemon p2 = batalha.getOponente().getPokemonAtivo();

        System.out.println("\n--- STATUS DO COMBATE ---");
        if (p1 != null) {
            System.out.println(batalha.getJogador().getNome() + " - " + p1.getNome() + " [HP: " + p1.getHpAtual() + "/" + p1.getEstatisticas().getHp() + "]");
        }
        if (p2 != null) {
            System.out.println(batalha.getOponente().getNome() + " - " + p2.getNome() + " [HP: " + p2.getHpAtual() + "/" + p2.getEstatisticas().getHp() + "]");
        }
        System.out.println("-------------------------");
    }

    public int lerInputInteiro(int min, int max) {
        int escolha = -1;
        while (true) {
            System.out.print("Escolha uma opcao (" + min + " a " + max + "): ");
            String entrada = this.leitor.nextLine();
            try {
                escolha = Integer.parseInt(entrada);
                if (escolha >= min && escolha <= max) {
                    break;
                }
                System.out.println("Erro: O valor inserido encontra-se fora do intervalo permitido.");
            } catch (NumberFormatException falha) {
                System.out.println("Erro: Entrada invalida. Por favor, digite apenas numeros inteiros.");
            }
        }
        return escolha;
    }
}