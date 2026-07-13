package view;

import java.util.Scanner;
import domain.models.battle.Battle;
import domain.models.battle.Pokemon;

public class CLIView implements InterfaceJogo {
    private final Scanner leitor;

    public CLIView() {
        this.leitor = new Scanner(System.in);
    }

    @Override
    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    @Override
    public void aguardarProximoDesafiante() {
        System.out.println("\n[Confronto encerrado. Pressione ENTER para avancar para o proximo oponente da Liga]");
        this.leitor.nextLine();
    }

    @Override
    public void exibirStatusBatalha(Battle battle) {
        Pokemon p1 = battle.getPlayer().getActivePokemon();
        Pokemon p2 = battle.getOpponent().getActivePokemon();

        System.out.println("\n--- STATUS DO COMBATE ---");
        if (p1 != null) {
            System.out.println(battle.getPlayer().getName() + " - " + p1.getName() + " [HP: " + p1.getHpAtual() + "/" + p1.getStats().getHp() + "]");
        }
        if (p2 != null) {
            System.out.println(battle.getOpponent().getName() + " - " + p2.getName() + " [HP: " + p2.getHpAtual() + "/" + p2.getStats().getHp() + "]");
        }
        System.out.println("-------------------------");
    }

    @Override
    public int lerInputInteiro(int min, int max) {
        int escolha;
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