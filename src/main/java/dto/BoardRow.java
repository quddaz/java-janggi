package dto;

public record BoardRow(
        int row,
        int col,
        String side,
        String type
) {}