package domain.models;

/**
 * Representa uma ação ofensiva ou passiva a ser utilizada por um Pokemon.
 */
public class Movimento {
    private final String nome;
    private final Tipo tipo;
    private final int poder;
    private final CategoriaMovimento categoria;

    public Movimento(String nome, Tipo tipo, int poder, CategoriaMovimento categoria) {
        this.nome = nome;
        this.tipo = tipo;
        this.poder = poder;
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public int getPoder() {
        return poder;
    }

    public CategoriaMovimento getCategoria() {
        return categoria;
    }
}