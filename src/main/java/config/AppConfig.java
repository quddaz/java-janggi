package config;

import config.transactional.TransactionalProxy;
import infra.DatabaseInitializer;
import java.lang.reflect.Proxy;
import service.GameService;
import service.GameServiceImpl;

public class AppConfig {

    private final DatabaseConfig databaseConfig = new DatabaseConfig();
    private final RepositoryConfig repositoryConfig = new RepositoryConfig();
    private final ServiceConfig serviceConfig = new ServiceConfig();

    public GameService gameService() {
        return serviceConfig.gameService(
                repositoryConfig.boardRepository(),
                repositoryConfig.gameRoomRepository()
        );
    }

    public GameService proxyGameService() {
        return (GameService) Proxy.newProxyInstance(
                GameService.class.getClassLoader(),
                new Class[]{GameService.class},
                new TransactionalProxy(gameService(), databaseConfig.qudaCP())
        );
    }

    public DatabaseInitializer databaseInitializer() {
        return databaseConfig.databaseInitializer();
    }
}
