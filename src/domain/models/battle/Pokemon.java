package domain.models.battle;

import domain.models.pokemon.*;

import java.util.ArrayList;
import java.util.List;

public final class Pokemon {
    private final String name;
    private final int level;
    private final Nature nature;
    private final Type first;
    private final Type second;
    private final Estatisticas baseStats;
    private final Estatisticas ivs;
    private final Estatisticas evs;
    private Estatisticas stats;
    private final List<Move> moves;
    private int hpAtual;

    public Pokemon(Builder builder){
        this.name = builder.getName();
        this.nature = builder.getNature();
        this.first = builder.getTypeOne();
        this.second = builder.getTypeTwo();
        this.baseStats = builder.getBaseStats();
        this.moves = builder.getMovimentos();

        if(builder.getIvs() == null)
            this.ivs = new Estatisticas(31, 31, 31, 31, 31, 31);
        else{
            this.ivs = builder.getIvs();
        }
        if(builder.getEvs() == null)
            this.evs = new Estatisticas(0, 0, 0, 0, 0, 0);
        else{
            this.evs = builder.getEvs();
        }

        if(builder.getLevel() == 0) this.level = 100;
        else this.level = builder.getLevel();

        calculateStatsLevel();
        this.hpAtual = this.stats.getHp();
    }
    public static class Builder{
        private String name;
        private int level;
        private Nature nature;
        private Type typeOne;
        private Type typeTwo;
        private Estatisticas baseStats;
        private Estatisticas ivs;
        private Estatisticas evs;
        private final List<Move> moves = new ArrayList<>();

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setLevel(int level){
            this.level = level;
            return this;
        }

        public Builder setIvs(Estatisticas ivs){
            this.ivs = ivs;
            return this;
        }

        public Builder setEvs(Estatisticas evs){
            this.evs = evs;
            return this;
        }

        public Builder setNature(Nature nature){
            this.nature = nature;
            return this;
        }

        public Builder setTypes(Type first, Type two){
            if(first == null) throw new RuntimeException("The first type of Pokémon is null.");
            this.typeOne = first;
            this.typeTwo = two;
            return this;
        }

        public Builder setBaseStats(Estatisticas base){
            this.baseStats = base;
            return this;
        }

        public Builder addMove(Move move){
            if(this.moves.size() < 4 && !this.moves.contains(move))
                this.moves.add(move);
            return this;
        }

        public Pokemon build(){
            return new Pokemon(this);
        }

        public String getName() {
            return name;
        }

        public int getLevel() {
            return level;
        }

        public Nature getNature() {
            return nature;
        }

        public Type getTypeOne() {
            return typeOne;
        }

        public Type getTypeTwo() {
            return typeTwo;
        }

        public Estatisticas getBaseStats() {
            return baseStats;
        }

        public Estatisticas getIvs() {
            return ivs;
        }

        public Estatisticas getEvs() {
            return evs;
        }

        public List<Move> getMovimentos() {
            return moves;
        }
    }


    private void calculateStatsLevel() {
        int hp = calculateMaxHP();
        int ataque = calcStats(Attribute.ATK);
        int defesa = calcStats(Attribute.DEF);
        int ataqueEspecial = calcStats(Attribute.SPA);
        int defesaEspecial = calcStats(Attribute.SPD);
        int velocidade = calcStats(Attribute.SPE);

        this.stats = new Estatisticas(hp, ataque, defesa, ataqueEspecial, defesaEspecial, velocidade);
    }

    private int calculateMaxHP() {
        int base = this.baseStats.getHp();
        int iv = this.ivs.getHp();
        int ev = this.evs.getHp();
        return ((2 * base + iv + (ev / 4)) * this.level) / 100 + this.level + 10;
    }

    private int calcStats(Attribute attribute) {
        int base = this.baseStats.getValor(attribute);
        int iv = this.ivs.getValor(attribute);
        int ev = this.evs.getValor(attribute);
        double multiplicador = this.nature.getMultiplier(attribute);

        int calculoBase = ((2 * base + iv + (ev / 4)) * this.level) / 100 + 5;
        return (int) (calculoBase * multiplicador);
    }

    public String getName() {
        return name;
    }

    public Nature getNature() {
        return nature;
    }

    public Type getFirst() {
        return first;
    }

    public Type getSecond() {
        return second;
    }

    public Estatisticas getStats() {
        return stats;
    }

    public List<Move> getMovimentos() {
        return moves;
    }

    public int getHpAtual() {
        return hpAtual;
    }

    public boolean isFainted(){
        return hpAtual == 0;
    }

    public boolean hasType(Type type){
        return this.first.equals(type) ||
                this.second != null &&
                        this.second.equals(type);
    }

    public void receiveDamage(int dmg){
        this.hpAtual -= dmg;
        if (this.hpAtual < 0) {
            this.hpAtual = 0;
        }
    }

    public void healFully(){
        this.hpAtual = this.stats.getHp();
    }

    public int sumBst(){
        return (getHpAtual() +
                stats.getValor(Attribute.ATK) +
                stats.getValor(Attribute.DEF) +
                stats.getValor(Attribute.SPA) +
                stats.getValor(Attribute.SPD) +
                stats.getValor(Attribute.SPE)
        );
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public int getLevel() {
        return level;
    }
}