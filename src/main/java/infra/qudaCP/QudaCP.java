package infra.qudaCP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class QudaCP {

    private final BlockingQueue<Connection> pool;
    private final int maximumPoolSize;
    private final int connectionTimeout;

    private final String jdbcUrl;
    private final String username;
    private final String password;

    private final AtomicInteger activeCount = new AtomicInteger(0);
    private final AtomicInteger totalCount = new AtomicInteger(0);

    public QudaCP(String jdbcUrl, String username, String password, int maximumPoolSize, int connectionTimeout) {
        this.maximumPoolSize = maximumPoolSize;
        this.connectionTimeout = connectionTimeout;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.pool = new LinkedBlockingQueue<>(maximumPoolSize);
        initConnection();
    }

    private void initConnection() {
        try {
            for (int i = 0; i < maximumPoolSize; i++) {
                pool.add(createConnection());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection createConnection() throws SQLException {
        totalCount.incrementAndGet();
        return DriverManager.getConnection(jdbcUrl, username, password);
    }

    // 커넥션 풀에서 커넥션을 가져온다
    public Connection take() {
        try {
            Connection conn = pool.poll();

            if (conn == null) { //못 받았다면 검증을 진행
                if (totalCount.get() < maximumPoolSize) { // 토탈이 최대 사이즈보다 작으면 생성
                    conn = createConnection();
                } else { // 토탈이 최대 사이즈와 같거나 크면 타임 아웃 대기
                    conn = pool.poll(connectionTimeout, TimeUnit.MILLISECONDS);
                    if (conn == null) {
                        throw new RuntimeException("[ERROR] 커넥션 타임아웃");
                    }
                }
            }

            if (!isValid(conn)) {
                totalCount.decrementAndGet();
                return take();
            }

            activeCount.incrementAndGet();
            return new PooledConnection(conn, this);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean isValid(Connection conn) {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(2);
        } catch (SQLException e) {
            return false;
        }
    }

    // 반환된 커넥션을 초기화하여 다시 대기 커넥션에 넣기
    public void release(Connection conn) {
        if (conn == null) return;

        activeCount.decrementAndGet();
        try {
            if (!conn.getAutoCommit()) {
                conn.rollback();
            }
            conn.setAutoCommit(true);
            conn.setReadOnly(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            if (!pool.add(conn)) {
                conn.close();
            }
        } catch (SQLException e) {
            try {
                conn.close();
            } catch (SQLException ignore) {}

            try {
                pool.add(createConnection());
            } catch (SQLException ignore) {}
        }
    }
}
