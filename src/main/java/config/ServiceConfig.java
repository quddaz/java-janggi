package config;

import repository.BoardRepository;
import repository.GameRoomRepository;
import service.GameServiceImpl;

public class ServiceConfig {

    public GameServiceImpl gameService(
            BoardRepository boardRepository,
            GameRoomRepository gameRoomRepository
    ) {
        return new GameServiceImpl(
                boardRepository,
                gameRoomRepository
        );
    }
}
