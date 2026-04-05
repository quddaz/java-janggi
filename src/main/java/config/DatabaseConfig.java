package config;

import infra.ConnectionManager;
import infra.DatabaseInitializer;
import infra.H2ConnectionManager;
import infra.qudaCP.QudaCP;

public class DatabaseConfig {

    private static final String URL = "jdbc:h2:file:./data/testdb";
    private static final String USER = "sa";
    private static final String PASSWORD = "";
    private static final int MAXIMUM_POOL_SIZE = 5;
    private static final int CONNECTION_TIME_OUT = 30000;

    private final ConnectionManager connectionManager;

    public DatabaseConfig() {
        this.connectionManager = new H2ConnectionManager(URL, USER, PASSWORD);
    }

    public QudaCP qudaCP() { return  new QudaCP(URL, USER, PASSWORD, MAXIMUM_POOL_SIZE, CONNECTION_TIME_OUT); }

    public DatabaseInitializer databaseInitializer() {
        return new DatabaseInitializer(connectionManager);
    }
}
