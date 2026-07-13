package tests;

import domain.facade.JsonDataFacade;
import utils.JsonValue;
import utils.Parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JsonTest {
    static void main() throws IOException {
        JsonDataFacade json = new JsonDataFacade();
        List<String> strings = Parser.separarObjetos(
                new String(Files.readAllBytes(Paths.get("league_data.json"))))
                        .orElseThrow(RuntimeException::new);

        JsonValue.JObject astRaiz = Parser.parseLeagueToAst(strings);

        JsonValue jsonValue = astRaiz.asObject().get("1")
                .asObject()
                .get("time");

        System.out.println(jsonValue);
        JsonValue jsonValue1 = jsonValue.asObject()
                .get("1")
                .asObject()
                .get("t1");

        System.out.println(jsonValue1);
    }



}
