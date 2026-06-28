package utils;

import domain.exceptions.BadJsonFormatException;

import java.util.*;

/**
 * Parser feito para facilitar as extrações de dados dos JSON utilizando a ‘interface’ JsonValue.
 */
public abstract class Parser {

    public static int extrairInteiro(String bloco, String chave) {
        return extrairTexto(bloco, chave)
                .map(Integer::parseInt)
                .orElseThrow(() -> new BadJsonFormatException("Chave numérica ausente: " + chave));
    }

    public static Optional<String> extrairTexto(String bloco, String chave) {
        OptionalInt posOpt = localizarChave(chave, bloco);
        if (posOpt.isEmpty()) {
            return Optional.empty();
        }

        int inicio = posOpt.getAsInt();
        int fim = bloco.length();
        int aspas = 0;

        for (int i = inicio; i < bloco.length(); i++) {
            char c = bloco.charAt(i);
            if (c == '"' && bloco.charAt(i - 1) != '\\') {
                aspas++;
            }
            if (aspas % 2 == 0 && (c == ',' || c == '}')) {
                fim = i;
                break;
            }
        }

        String valor = bloco.substring(inicio, fim).replace("\"", "").trim();
        return "null".equalsIgnoreCase(valor) ? Optional.empty() : Optional.of(valor);
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

        return objetos.isEmpty() ? Optional.empty() : Optional.of(objetos);
    }

    public static Optional<String> extrairObjeto(String bloco, String chave) {
        OptionalInt posOpt = localizarChave(chave, bloco);
        if (posOpt.isEmpty()) {
            return Optional.empty();
        }

        int inicio = posOpt.getAsInt();
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

        return Optional.of(bloco.substring(inicio, fim + 1));
    }

    public static JsonValue.JObject parsePokemonToAst(String rawPoke) {
        Map<String, JsonValue> map = new LinkedHashMap<>();
        map.put("nome", new JsonValue.JString(extrairTexto(rawPoke, "nome")
                .orElse("")));
        map.put("t1", new JsonValue.JString(extrairTexto(rawPoke, "t1")
                .orElse("NORMAL")));

        Optional<String> t2Opt = extrairTexto(rawPoke, "t2");
        map.put("t2", t2Opt.<JsonValue>map(JsonValue.JString::new)
                .orElseGet(JsonValue.JNull::new));

        for (String stat : List.of("hp", "atk", "def", "spa", "spd", "spe")) {
            map.put(stat, new JsonValue.JNumber(extrairInteiro(rawPoke, stat)));
        }

        for (int m = 1; m <= 4; m++) {
            String mKey = "m" + m;
            extrairObjeto(rawPoke, mKey).ifPresent(rawMove -> {
                Map<String, JsonValue> mFields = new LinkedHashMap<>();
                mFields.put("name", new JsonValue.JString(extrairTexto(rawMove, "name").orElse("Tackle")));
                mFields.put("type", new JsonValue.JString(extrairTexto(rawMove, "type").orElse("NORMAL")));
                mFields.put("power", new JsonValue.JNumber(extrairInteiro(rawMove, "power")));
                mFields.put("category", new JsonValue.JString(extrairTexto(rawMove, "category").orElse("ESPECIAL")));
                map.put(mKey, new JsonValue.JObject(mFields));
            });
        }

        return new JsonValue.JObject(map);
    }

    public static JsonValue.JObject parseLeagueToAst(List<String> jsonTrainers) {
        Map<String, JsonValue> leagueMap = new LinkedHashMap<>();

        for (int i = 0; i < jsonTrainers.size(); i++) {
            String rawTrainer = jsonTrainers.get(i);
            Map<String, JsonValue> trainerFields = new LinkedHashMap<>();
            trainerFields.put("nome", new JsonValue.JString(extrairTexto(rawTrainer, "nome")
                    .orElse("")));

            Map<String, JsonValue> timeFields = new LinkedHashMap<>();
            extrairObjeto(rawTrainer, "time").ifPresent(rawTime -> {
                for (int p = 1; p <= 6; p++) {
                    String slot = String.valueOf(p);
                    extrairObjeto(rawTime, slot)
                            .ifPresent(rawPoke -> timeFields.put(slot, parsePokemonToAst(rawPoke)));
                }
            });

            trainerFields.put("time", new JsonValue.JObject(timeFields));
            leagueMap.put(String.valueOf(i + 1), new JsonValue.JObject(trainerFields));
        }

        return new JsonValue.JObject(leagueMap);
    }

    private static OptionalInt localizarChave(String chave, String bloco) {
        String parametro = "\"" + chave + "\":";
        int inicio = bloco.indexOf(parametro);
        if (inicio == -1) {
            return OptionalInt.empty();
        }

        inicio += parametro.length();
        while (inicio < bloco.length() && Character.isWhitespace(bloco.charAt(inicio))) {
            inicio++;
        }

        return OptionalInt.of(inicio);
    }
}