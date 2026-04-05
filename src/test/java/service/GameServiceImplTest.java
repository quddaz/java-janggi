package service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import config.AppConfig;
import config.transactional.TransactionalProxy;
import infra.H2ConnectionManager;
import domain.place.Place;
import domain.place.piece.Side;
import domain.position.Position;
import domain.board.BoardFactory;
import infra.qudaCP.QudaCP;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import repository.impl.BoardRepositoryImpl;
import repository.impl.GameRoomRepositoryImpl;

public class GameServiceImplTest {

    private static final String URL = "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private GameService gameService;

    @BeforeEach
    void setUp() {

        QudaCP qudaCP = new QudaCP(URL, USER, PASSWORD, 5, 30000);
        H2ConnectionManager connectionManager = new H2ConnectionManager(URL, USER, PASSWORD);
        TestDatabaseInitializer testDatabaseInitializer = new TestDatabaseInitializer(connectionManager);
        testDatabaseInitializer.init();

        GameService gameServiceImpl = new GameServiceImpl(new BoardRepositoryImpl(),
                new GameRoomRepositoryImpl());
        gameService = (GameService) Proxy.newProxyInstance(
                GameService.class.getClassLoader(),
                new Class[]{GameService.class},
                new TransactionalProxy(gameServiceImpl, qudaCP)
        );
    }

    @Test
    @DisplayName("게임 저장 시 게임룸, 보드, 게임 상태가 함께 저장된다")
    void saveGame_shouldPersistRoomBoardAndState() {
        // given
        Map<Position, Place> board = BoardFactory.setUpEmpty();
        String name = "testName";
        Side side = Side.CHO;
        // when & then
        assertThatCode(() -> gameService.saveGame(board, name, side))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임 저장 시 게임룸, 보드, 게임 상태가 함께 저장된다")
    void updateGame_shouldPersistRoomBoardAndState() {
        // given
        Map<Position, Place> board = BoardFactory.setUpEmpty();
        Side side = Side.CHO;
        // when & then
        assertThatCode(() -> gameService.updateGame(board, side, 1))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임룸 ID로 보드를 조회할 수 있다")
    void findBoardByRoomId_shouldReturnBoard() {
        // given
        int roomId = 1;

        // when
        Map<Position, Place> board = gameService.findBoardByRoomId(roomId);

        //then
        assertThat(board).isNotEmpty();
    }

    @Test
    @DisplayName("존재하지 않는 게임룸 ID로 게임 보드 조회 시 예외가 발생한다")
    void findBoardByRoomId_shouldThrowException_whenRoomNotFound() {
        // given
        int roomId = 2;

        // when & then
        assertThatThrownBy(() -> gameService.findBoardByRoomId(roomId))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("전체 게임룸 목록을 조회할 수 있다")
    void findGameRoomAll_shouldReturnAllRooms() {
        // given & when & then
        assertThat(gameService.findGameRoomAll().size()).isEqualTo(1);
    }

}
