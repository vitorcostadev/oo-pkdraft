package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class Parser {

    public static int extrairInteiro(String bloco, String chave) {
        Optional<String> texto = extrairTexto(bloco, chave);
        texto.orElseThrow(RuntimeException::new);
        return Integer.parseInt(texto.get());
    }

    public static Optional<String> extrairTexto(String bloco, String chave) {
        int inicio = (int) make(chave, bloco)
                .orElseThrow(RuntimeException::new);

        int fimVirgula = bloco.indexOf(',', inicio);
        int fimChave = bloco.indexOf('}', inicio);

        int fim = bloco.length();

        if (fimVirgula != -1) {
            fim = Math.min(fim, fimVirgula);
        }

        if (fimChave != -1) {
            fim = Math.min(fim, fimChave);
        }

        String valor = bloco.substring(inicio, fim)
                .replace("\"", "")
                .trim();

        if ("null".equals(valor)) {
            return Optional.empty();
        }

        return Optional.of(valor);
    }

    public static Optional<List<String>> separarObjetos(String json) {
        List<String> objetos = new ArrayList<>();

        int nivel = 0;
        int inicio = -1;
        boolean dentroString = false;

        for (int i = 0; i < json.length(); i++) {
            char c = json.charAt(i);

            if (c == '"' && (i == 0 || json.charAt(i - 1) != '\\')) {
                dentroString = !dentroString;
            }

            if (dentroString) {
                continue;
            }

            if (c == '{') {
                if (nivel == 0) {
                    inicio = i;
                }
                nivel++;
            } else if (c == '}') {
                nivel--;

                if (nivel == 0 && inicio != -1) {
                    objetos.add(json.substring(inicio, i + 1));
                    inicio = -1;
                }
            }
        }

        return  !objetos.isEmpty() ? Optional.of(objetos) : Optional.empty();
    }

    private static Optional<Object> make(String chave, String bloco){
        String parametro = "\"" + chave + "\":";

        int inicio = bloco.indexOf(parametro);
        if (inicio == -1) {
            return Optional.empty();
        }

        inicio += parametro.length();

        while (inicio < bloco.length() && Character.isWhitespace(bloco.charAt(inicio))) {
            inicio++;
        }

        return Optional.of(inicio);
    }
    public static Optional<String> extrairObjeto(String bloco, String chave) {
        int inicio = (int) Parser.make(chave, bloco)
                .orElseThrow(RuntimeException::new);

        if (bloco.charAt(inicio) != '{') {
            return Optional.empty();
        }

        int nivel = 0;
        boolean dentroString = false;
        int fim = inicio;

        for (; fim < bloco.length(); fim++) {
            char c = bloco.charAt(fim);

            if (c == '"' && (fim == 0 || bloco.charAt(fim - 1) != '\\')) {
                dentroString = !dentroString;
            }

            if (dentroString) {
                continue;
            }

            if (c == '{') {
                nivel++;
            } else if (c == '}') {
                nivel--;

                if (nivel == 0) {
                    break;
                }
            }
        }

        return bloco.substring(inicio, fim + 1).describeConstable();
    }
}
