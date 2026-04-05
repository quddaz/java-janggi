package config.transactional;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import infra.qudaCP.QudaCP;

public class TransactionalProxy implements InvocationHandler {

    private final Object target;
    private final QudaCP dataSource;

    public TransactionalProxy(Object target, QudaCP dataSource) {
        this.target = target;
        this.dataSource = dataSource;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.isAnnotationPresent(Transactional.class)){
            return method.invoke(target, args);
        }

        Connection conn = dataSource.take();

        try {
            conn.setAutoCommit(false);
            TransactionContext.set(conn);

            Object result = method.invoke(target, args);

            conn.commit();

            return result;
        } catch (Exception e){
            conn.rollback();
            throw new RuntimeException(e);
        } finally {
            TransactionContext.clear();
            conn.close();
        }
    }
}