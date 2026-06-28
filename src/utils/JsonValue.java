package utils;

import java.util.List;
import java.util.Map;

public sealed interface JsonValue {

    record JString(String value) implements JsonValue {}
    record JNumber(int value) implements JsonValue {}
    record JNull() implements JsonValue {}
    record JObject(Map<String, JsonValue> fields) implements JsonValue {}
    record JArray(List<JsonValue> elements) implements JsonValue {}

    default String asString() {
        if (this instanceof JString(String s)) return s;
        throw new IllegalStateException("Esperado JString, obtido " + this.getClass().getSimpleName());
    }

    default int asInt() {
        if (this instanceof JNumber(int n)) return n;
        throw new IllegalStateException("Esperado JNumber, obtido " + this.getClass().getSimpleName());
    }

    default Map<String, JsonValue> asObject() {
        if (this instanceof JObject(var map)) return map;
        throw new IllegalStateException("Esperado JObject, obtido " + this.getClass().getSimpleName());
    }

    default int size() {
        return switch (this) {
            case JObject(var map)   -> map.size();
            case JArray(var list)   -> list.size();
            case JString(var s)     -> s.length();
            case JNumber _, JNull _ -> throw new UnsupportedOperationException(
                    "Nodes escalares (" + this.getClass().getSimpleName() + ") não possuem cardinalidade."
            );
        };
    }
}