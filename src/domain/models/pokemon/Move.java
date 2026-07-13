package domain.models.pokemon;

public record Move(
        String name,
        Type type,
        int power,
        MovementCategory category) {}