package backend.web.database.connectionpool;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.Enumeration;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public enum  ConnectionPool {

    INSTANCE;

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/schema1";
    private static final String USER="root";
    private static final String PASSWORD ="dastan11";
    private final int POOL_SIZE = 32;

    private BlockingQueue<ProxyConnection> freeConnections;
    //private BlockingQueue<ProxyConnection> givenAwayConnections;
    private Queue<ProxyConnection> givenAwayConnections;

    private ArrayBlockingQueue<ProxyConnection> connectionQueue;

    ConnectionPool(){
        try {
            Class.forName(DRIVER);
            freeConnections = new LinkedBlockingDeque<>(POOL_SIZE);
            //givenAwayConnections = new ArrayBlockingQueue<>(POOL_SIZE);
            givenAwayConnections = new ArrayDeque<>(POOL_SIZE);
            init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {

        try {
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = PropertiesConnection.getConnection(URL, USER, PASSWORD);
                ProxyConnection proxyConnection = new ProxyConnection(connection);
                freeConnections.add(proxyConnection);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection takeConnection() {

        ProxyConnection connection = null;
        try {
            connection = freeConnections.take();
            givenAwayConnections.offer(connection);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return connection;
    }

    public void releaseConnection(Connection connection) {

        if (connection instanceof ProxyConnection) {
            try {
                connection.setAutoCommit(true);
                givenAwayConnections.remove(connection);
                freeConnections.offer((ProxyConnection) connection);
                //connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void destroyPool() {
        int poolSize = freeConnections.size();

        for (int i = 0; i < poolSize; i++) {
            try {
                freeConnections.take().reallyClose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        deregisterDrivers();
    }

    private void deregisterDrivers() {
        Enumeration<Driver> enumDrivers = DriverManager.getDrivers();
        try {
            while (enumDrivers.hasMoreElements()) {
                Driver driver = enumDrivers.nextElement();
                DriverManager.deregisterDriver(driver);
            }
        } catch (SQLException e) {
            //LOGGER.log(Level.ERROR, e.getMessage());
            e.printStackTrace();
        }
    }

}