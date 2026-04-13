package config;

import domain.board.BoardRepository;
import repository.BoardDao;
import repository.GameRoomDao;
import repository.GameRoomRepository;
import repository.impl.BoardRepositoryImpl;
import repository.impl.GameRoomRepositoryImpl;

public class RepositoryConfig {

    public BoardRepository boardRepository() {
        return new BoardRepositoryImpl(new BoardDao());
    }

    public GameRoomRepository gameRoomRepository() {
        return new GameRoomRepositoryImpl(new GameRoomDao());
    }
}
