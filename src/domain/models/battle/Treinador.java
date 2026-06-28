package domain.models.battle;

import domain.commands.ComandoTurno;
import domain.strategy.EstrategiaDecisao;

import java.util.ArrayList;
import java.util.List;

public class Treinador {
    private final String nome;
    private final List<Pokemon> equipe;
    private final EstrategiaDecisao estrategia;

    public Treinador(String nome, EstrategiaDecisao estrategia) {
        this.nome = nome;
        this.estrategia = estrategia;
        this.equipe = new ArrayList<>();
    }

    /**
     * Insere um Pokemon na equipe.
     * @param p Pokemon a ser inserido.
     */
    public void adicionarPokemon(Pokemon p) {
        if (this.equipe.size() < 6 && !this.equipe.contains(p)) {
            this.equipe.add(p);
        }
    }

    /**
     * Retorna o Pokemon posicionado na primeira vaga da equipe, representando o Pokémon em campo.
     * @return Pokemon que esta na primeira posição da lista.
     */
    public Pokemon getPokemonAtivo() {
        if (this.equipe.isEmpty()) {
            return null;
        }
        return this.equipe.getFirst();
    }

    /**
     * Varre a lista de Pokémons para confirmar se ainda existe algum vivo.
     * @return True se houver pelo menos um Pokemon com pontos de vida.
     */
    public boolean temPokemonApto() {
        for (Pokemon p : this.equipe) {
            if (!p.isDesmaiado()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Move o Pokemon selecionado para a primeira posição da equipe.
     * @param p Pokemon que assumirá o combate.
     */
    public void colocarComoAtivo(Pokemon p) {
        if (this.equipe.contains(p) && !p.isDesmaiado()) {
            this.equipe.remove(p);
            this.equipe.addFirst(p);
        }
    }

    /**
     * Restaura os pontos de vida de todos os integrantes da equipe.
     */
    public void curarEquipe() {
        for (Pokemon p : this.equipe) {
            p.curarTotalmente();
        }
    }

    public ComandoTurno solicitarAcao(Treinador inimigo) {
        return this.estrategia.escolherAcao(this, inimigo);
    }

    public String getNome() {
        return nome;
    }

    public List<Pokemon> getEquipe() {
        return equipe;
    }

    public EstrategiaDecisao getEstrategia() {
        return estrategia;
    }


}