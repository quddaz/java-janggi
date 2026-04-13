package config;

import infra.DBExecutor;
import domain.board.BoardRepository;
import repository.GameRoomRepository;
import service.GameService;

public class ServiceConfig {

    public GameService gameService(
            BoardRepository boardRepository,
            GameRoomRepository gameRoomRepository,
            DBExecutor dbExecutor
    ) {
        return new GameService(
                boardRepository,
                gameRoomRepository,
                dbExecutor
        );
    }
}
