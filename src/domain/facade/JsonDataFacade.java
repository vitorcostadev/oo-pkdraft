package domain.facade;

import domain.exceptions.BadJsonFormatException;
import domain.models.battle.Pokemon;
import domain.models.battle.Player;
import domain.models.pokemon.*;
import domain.strategy.HeuristicStrategy;
import utils.JsonValue;
import utils.Parser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Facade que cria uma ‘interface’ única para ser utilizada pelo restante do programa.
 */
public class JsonDataFacade implements DataPokemonFacade {

    private final List<Pokemon.Builder> baseRepository;

    public JsonDataFacade() {
        this.baseRepository = new ArrayList<>();
        loadDataFromDisk();
    }

    private void loadDataFromDisk(){
        processJson(readArchive("pokemons_base.json"));
    }

    private String readArchive(String pathname) {
        try {
            return new String(Files.readAllBytes(Paths.get(pathname)));
        } catch (IOException e) {
            System.err.println("Não foi possível localizar o arquivo: " + pathname + " carregando dados de contingency.");
            loadDefaultData();
            return "";
        }
    }

    private void processJson(String json) throws BadJsonFormatException{
        if(json.isEmpty()) return;

        this.baseRepository.clear();
        List<String> blocos = Parser.separarObjetos(json)
                .orElseThrow(() -> new BadJsonFormatException("Não foi possível separar os objetos do json."));

        for (String bloco : blocos) {
            JsonValue pokeAst = Parser.parsePokemonToAst(bloco);
            this.baseRepository.add(astToBuilder(pokeAst));
        }
    }

    private Pokemon.Builder astToBuilder(JsonValue noPokemon) {
        Map<String, JsonValue> mapaPoke = noPokemon.asObject();
        String name = mapaPoke.get("name").asString();
        Type typeOne = Type.valueOf(mapaPoke.get("t1").asString());
        int anInt = new Random().nextInt(0,
                Nature.values().length - 1);

        JsonValue typeTwoNo = mapaPoke.get("t2");
        Type typeTwo = (typeTwoNo == null || typeTwoNo instanceof JsonValue.JNull || typeTwoNo.asString().isEmpty() || "null".equalsIgnoreCase(typeTwoNo.asString()))
                ? null
                : Type.valueOf(typeTwoNo.asString());

        Estatisticas stats = new Estatisticas(
                mapaPoke.get("hp").asInt(),
                mapaPoke.get("atk").asInt(),
                mapaPoke.get("def").asInt(),
                mapaPoke.get("spa").asInt(),
                mapaPoke.get("spd").asInt(),
                mapaPoke.get("spe").asInt()
        );

        Pokemon.Builder builder = createModel(name, typeOne, typeTwo, stats)
                .setNature(Nature.values()[anInt]);

        builder.setLevel(100);

        for (int mSlot = 1; mSlot <= 4; mSlot++) {
            JsonValue moveNo = mapaPoke.get("m" + mSlot);
            if (moveNo != null && !(moveNo instanceof JsonValue.JNull)) {
                Map<String, JsonValue> mFields = moveNo.asObject();
                builder.addMove(new Move(
                        mFields.get("name").asString(),
                        Type.valueOf(mFields.get("type").asString()),
                        mFields.get("power").asInt(),
                        MovementCategory.valueOf(mFields.get("category").asString())
                ));
            }
        }

        return builder;
    }

    @Override
    public List<Pokemon.Builder> getOptions() {
        List<Pokemon.Builder> copia = new ArrayList<>(this.baseRepository);
        Collections.shuffle(copia, new Random());


        List<Pokemon.Builder> selecionados = new ArrayList<>();
        for (int i = 0; i < 3 && i < copia.size(); i++) {
            //selecionados.add(copia.get(i));
            int temp = new Pokemon(copia.get(i))
                    .sumBst();

            if(temp >= 700) selecionados.add(copia.get(i));
        }

        return selecionados;
    }

    @Override
    public List<Player> loadLeague(String region) {
        List<Player> liga = new ArrayList<>();

        try {
            String m = readArchive("league_data.json");

            List<String> separar = Parser.separarObjetos(m)
                    .orElseThrow(() -> new BadJsonFormatException("Um erro ocorreu na tentativa de separar os objetos do json."));

            JsonValue astRaiz = Parser.parseLeagueToAst(separar);
            Map<String, JsonValue> mapaLiga = astRaiz.asObject();

            for (int i = 0; i < astRaiz.size(); i++) {
                JsonValue trainerVal = mapaLiga.get(String.valueOf(i + 1));
                Map<String, JsonValue> trainerMap = trainerVal.asObject();
                Player player = new Player(trainerMap.get("name").asString(), new HeuristicStrategy());
                Map<String, JsonValue> timeMap = trainerMap.get("time").asObject();

                for (int pSlot = 1; pSlot <= 6; pSlot++) {
                    JsonValue pokeNo = timeMap.get(String.valueOf(pSlot));
                    if (pokeNo != null && !(pokeNo instanceof JsonValue.JNull)) {
                        player.addPokemon(astToBuilder(pokeNo).build());
                    }
                }
                liga.add(player);
            }
        } finally{
            if(liga.isEmpty()) liga = getContigenceLeague();
        }

        return liga;
    }

    private List<Player> getContigenceLeague() {
        List<Player> demergencia = new ArrayList<>();
        Player t = new Player("Blue", new HeuristicStrategy());
        t.addPokemon(createModel("Blastoise", Type.WATER, null, new Estatisticas(79, 83, 100, 85, 105, 78))
                .addMove(new Move("Hydro Pump", Type.WATER, 110, MovementCategory.SPECIAL))
                        .setNature(Nature.QUIRKY)
                .build());
        demergencia.add(t);
        return demergencia;
    }

    private void loadDefaultData() {
        this.baseRepository.clear();
        this.baseRepository.add(createModel("Charizard", Type.FIRE, Type.FLYING, new Estatisticas(78, 84, 78, 109, 85, 100))
                .addMove(new Move("Flamethrower", Type.FIRE, 90, MovementCategory.SPECIAL)));
    }

    private Pokemon.Builder createModel(String name, Type typeOne, Type typeTwo, Estatisticas base) {
        Pokemon.Builder builder = Pokemon.getBuilder();
        builder.setName(name)
                .setTypes(typeOne, typeTwo)
                .setBaseStats(base);

        return builder;
    }
}