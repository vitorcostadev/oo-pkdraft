package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import view.CLIView;

/**
 * Centraliza o armazenamento de logs e aplica uma temporizacao automatica para suavizar a leitura no terminal.
 */
public class LogService {
    private final List<String> eventos;
    private final CLIView view;

    public LogService(CLIView view) {
        this.eventos = new ArrayList<>();
        this.view = view;
    }

    /**
     * Registra o evento na lista e realiza uma pausa automatica na execucao para permitir a leitura fluida.
     * @param mensagem Texto do evento ocorrido.
     */
    public void registrarEvento(String mensagem) {
        this.eventos.add(mensagem);
        if (this.view != null) {
            this.view.exibirMensagem(mensagem);
            try {
                Thread.sleep(1200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void salvarLogEmArquivo() {
        File diretorio = new File("logs");
        if (!diretorio.exists()) {
            diretorio.mkdirs();
        }
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File arquivo = new File(diretorio, "campanha_" + dataHora + ".pklog");
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivo))) {
            for (String evento : this.eventos) {
                escritor.write(evento);
                escritor.newLine();
            }
        } catch (IOException falha) {
            System.err.println("Falha critica ao guardar logs: " + falha.getMessage());
        }
    }
}