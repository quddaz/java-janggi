package dto;

import java.time.LocalDateTime;

public record GameRoomRow(
        long id,
        String name,
        String currentTurn,
        LocalDateTime createdAt
) {}