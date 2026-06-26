package domain.models;

/**
 * Define as categorias de um movimento.
 */
public enum CategoriaMovimento {
    FISICO("PHYSICAL"),
    ESPECIAL("SPECIAL"),
    STATUS("STATUS");

    private final String englishName;
    CategoriaMovimento(String englishName) {this.englishName = englishName;}

    public String getEnglishName() {
        return englishName;
    }

    public static CategoriaMovimento fromString(String s) {
        if (s == null) {
            return STATUS;
        }

        s = s.trim().toUpperCase();

        return switch (s) {
            case "PHYSICAL" -> FISICO;
            case "SPECIAL" -> ESPECIAL;
            default -> STATUS;
        };
    }
}