/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package avatar.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

public class DbManager {

    private static final Logger logger = Logger.getLogger(DbManager.class);

    private static DbManager instance = null;
    private DataSource dataSource;
    private boolean springManaged;

    public static synchronized DbManager getInstance() {
        if (instance == null) {
            instance = new DbManager();
        }
        return instance;
    }

    /**
     * Uses the DataSource created by Spring Boot. The pool is closed by Spring on shutdown.
     */
    public static synchronized void bind(DataSource dataSource) {
        if (dataSource == null) {
            throw new IllegalArgumentException("dataSource");
        }
        DbManager manager = getInstance();
        manager.dataSource = dataSource;
        manager.springManaged = true;
        logger.debug("DB Connection Pool is managed by Spring.");
    }

    private DbManager() {
    }

    public void start() {
        if (this.dataSource != null) {
            logger.debug("DB Connection Pool is ready.");
            return;
        }
        try {
            Properties props = new Properties();
            try (InputStreamReader reader = new InputStreamReader(
                    new FileInputStream("database.properties"), StandardCharsets.UTF_8)) {
                props.load(reader);
            }
            String driver = props.getProperty("driver");
            String host = props.getProperty("host");
            int port = Integer.parseInt(props.getProperty("port"));
            String dbname = props.getProperty("dbname");
            String username = props.getProperty("username");
            String password = props.containsKey("password")
                    ? props.getProperty("password")
                    : props.getProperty("passeword");
            int maxConnections = Integer.parseInt(props.getProperty("max_connection"));
            int minConnections = Integer.parseInt(props.getProperty("min_connection"));

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + dbname);
            config.setDriverClassName(driver);
            config.setUsername(username);
            config.setPassword(password);
            config.setMinimumIdle(minConnections);
            config.setMaximumPoolSize(maxConnections);
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            this.dataSource = new HikariDataSource(config);
            logger.debug("DB Connection Pool has created.");
        } catch (Exception e) {
            logger.error("DB Connection Pool Creation has failed.", e);
        }
    }

    public void shutdown() {
        if (springManaged) {
            logger.debug("DB Connection Pool stays open until Spring shuts down.");
            return;
        }
        try {
            if (this.dataSource instanceof HikariDataSource hikariDataSource) {
                hikariDataSource.close();
                logger.debug("DB Connection Pool is shutting down.");
            }
            this.dataSource = null;
        } catch (Exception e) {
            logger.warn("Error when shutting down DB Connection Pool", e);
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.dataSource == null) {
            throw new SQLException("DB Connection Pool is not started");
        }
        return this.dataSource.getConnection();
    }

    public int executeUpdate(String sqlStatement, Object... params) {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement)) {
            int i = 1;
            for (Object object : params) {
                preparedStatement.setObject(i++, object);
            }
            return preparedStatement.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }
}
