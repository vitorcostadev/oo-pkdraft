package controller;

import view.InterfaceJogo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LogService {
    private final List<String> eventos;
    private final InterfaceJogo view;

    public LogService(InterfaceJogo view) {
        this.eventos = new ArrayList<>();
        this.view = view;
    }

    /**
     * Registra o evento na lista e realiza uma pausa automática na execução para permitir a leitura fluida.
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
            if(diretorio.mkdirs())
                System.out.println("O diretório '" + diretorio.getName() + "' foi criado.");
        }
        String dataHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File arquivo = new File(diretorio, "campanha_" + dataHora + ".pklog");
        try (BufferedWriter escritor = new BufferedWriter(new FileWriter(arquivo))) {
            for (String evento : this.eventos) {
                escritor.write(evento);
                escritor.newLine();
            }
        } catch (IOException e) {
            System.err.println("Falha critica ao guardar logs: " + e.getMessage());
        }
    }
}