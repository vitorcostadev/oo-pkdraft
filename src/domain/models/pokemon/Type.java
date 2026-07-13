package domain.models.pokemon;

import java.util.List;

public enum Type {
    NORMAL{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.ROCK, Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of();
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of(Type.GHOST);
        }
    },

    FIRE{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.FIRE, Type.WATER, Type.ROCK, Type.DRAGON);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.GRASS, Type.ICE, Type.BUG, Type.STEEL);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    WATER{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.WATER, Type.GRASS, Type.DRAGON);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.FIRE, Type.GROUND, Type.ROCK);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    GRASS{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.FIRE, Type.GRASS, Type.POISON, Type.FLYING,
                    Type.BUG, Type.DRAGON, Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.WATER, Type.GROUND, Type.ROCK);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    ELECTRIC{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.ELECTRIC, Type.GRASS, Type.DRAGON);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.WATER, Type.FLYING);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    ICE{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.FIRE, Type.WATER, Type.ICE, Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.GRASS, Type.GROUND, Type.FLYING, Type.DRAGON);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    FIGHTING{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.POISON, Type.FLYING, Type.PSYCHIC,
                    Type.BUG, Type.FAIRY);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.NORMAL, Type.ICE, Type.ROCK,
                    Type.DARK, Type.STEEL);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    POISON{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.POISON, Type.GROUND, Type.ROCK, Type.GHOST);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.GRASS, Type.FAIRY);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    GROUND{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.GRASS, Type.BUG);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.FIRE, Type.ELECTRIC, Type.POISON,
                    Type.ROCK, Type.STEEL);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of(Type.ELECTRIC);
        }
    },

    FLYING{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.ELECTRIC, Type.ROCK, Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.GRASS, Type.FIGHTING, Type.BUG);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of(Type.GROUND);
        }
    },

    PSYCHIC{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.PSYCHIC, Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.FIGHTING, Type.POISON);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    BUG{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.FIRE, Type.FIGHTING, Type.POISON,
                    Type.FLYING, Type.GHOST, Type.STEEL, Type.FAIRY);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.GRASS, Type.PSYCHIC, Type.DARK);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    ROCK{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.FIGHTING, Type.GROUND, Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.FIRE, Type.ICE, Type.FLYING, Type.BUG);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    GHOST{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.DARK);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.PSYCHIC, Type.GHOST);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of(Type.NORMAL, Type.FIGHTING);
        }
    },

    DRAGON{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.DRAGON);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of();
        }
    },

    DARK{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.FIGHTING, Type.DARK, Type.FAIRY);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.PSYCHIC, Type.GHOST);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of(Type.PSYCHIC);
        }
    },

    STEEL{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.FIRE, Type.WATER, Type.ELECTRIC, Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.ICE, Type.ROCK, Type.FAIRY);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of(Type.POISON);
        }
    },

    FAIRY{
        @Override
        public List<Type> getNonEffectivesTypes(){
            return List.of(Type.FIRE, Type.POISON, Type.STEEL);
        }

        @Override
        public List<Type> getEffectivesTypes(){
            return List.of(Type.FIGHTING, Type.DRAGON, Type.DARK);
        }

        @Override
        public List<Type> getNoEffectTypes() {
            return List.of(Type.DRAGON);
        }
    };

    public abstract List<Type> getNonEffectivesTypes();
    public abstract List<Type> getEffectivesTypes();
    public abstract List<Type> getNoEffectTypes();

    public double getBaseEffectiveness(Type first, Type second){
        if(second == null) return 1D;

        if(first == null) first = this;

        if(first.getEffectivesTypes().contains(second)) return 2D;
        if(first.getNonEffectivesTypes().contains(second)) return 0.5;
        if(first.getNoEffectTypes().contains(second)) return 0D;
        return 1D;
    }

    public double calcEffectiveness(Type t1, Type t2){
        double e1 = getBaseEffectiveness(this, t1);
        double e2 = (t2 != null) ? getBaseEffectiveness(this, t2) : 1D;

        return e1 * e2;
    }


}