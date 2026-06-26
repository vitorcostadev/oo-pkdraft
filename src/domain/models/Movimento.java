package domain.models;

/**
 * Representa uma ação ofensiva ou passiva a ser utilizada por um Pokemon.
 */
public record Movimento(String nome, Tipo tipo, int poder, CategoriaMovimento categoria) {
}