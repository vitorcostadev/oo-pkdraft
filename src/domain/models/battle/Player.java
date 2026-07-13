package domain.models.battle;

import domain.commands.TurnCommand;
import domain.strategy.DecisionStrategy;
import domain.strategy.HeuristicStrategy;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private final List<Pokemon> team;
    private final DecisionStrategy strategy;

    public Player(String name, DecisionStrategy strategy) {
        this.name = name;
        this.strategy = strategy;
        this.team = new ArrayList<>();
    }
    
    public Player(String name){
        this(name, new HeuristicStrategy());
    }

    /**
     * Insert a pokémon in a team.
     * @param p The inserted pokémon.
     */
    public void addPokemon(Pokemon p) {
        if (this.team.size() < 6 && !this.team.contains(p)) {
            this.team.add(p);
        }
    }

    /**
     * Retorna o Pokemon posicionado na primeira vaga da team, representando o Pokémon em campo.
     * @return Pokemon que esta na primeira posição da lista.
     */
    public Pokemon getActivePokemon() {
        if (this.team.isEmpty()) {
            return null;
        }
        return this.team.getFirst();
    }

    /**
     * Varre a lista de Pokémons para confirmar se ainda existe algum vivo.
     * @return True se houver pelo menos um Pokemon com pontos de vida.
     */
    public boolean hasAlivePokemon() {
        for (Pokemon p : this.team) {
            if (!p.isFainted()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Move o Pokemon selecionado para a primeira posição da team.
     * @param p Pokemon que assumirá o combate.
     */
    public void setActivePokemon(Pokemon p) {
        if (this.team.contains(p) && !p.isFainted()) {
            this.team.remove(p);
            this.team.addFirst(p);
        }
    }

    /**
     * Restore the hp of team.
     */
    public void healTeam() {
        for (Pokemon p : this.team) {
            p.healFully();
        }
    }

    public TurnCommand soliciteAction(Player opponent) {
        return this.strategy.chooseAction(this, opponent);
    }

    public String getName() {
        return name;
    }

    public List<Pokemon> getTeam() {
        return team;
    }

    public DecisionStrategy getStrategy() {
        return strategy;
    }


}