package config.transactional;

import java.sql.Connection;

public class TransactionContext {
    private static final ThreadLocal<Connection> holder = new ThreadLocal<>();

    public static void set(Connection conn) {
        holder.set(conn);
    }

    public static Connection get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}