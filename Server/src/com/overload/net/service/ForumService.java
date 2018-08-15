package com.overload.net.service;


import java.sql.*;

import com.overload.Server;
import com.overload.game.GameConstants;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;



public final class ForumService {

    private HikariDataSource connectionPool;

    public ForumService() {}
    
    public void start() throws Exception {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.jdbc.Driver");
        config.setJdbcUrl(GameConstants.FORUM_DB_URL);
        config.setUsername(GameConstants.FORUM_DB_USER);
        config.setPassword(GameConstants.FORUM_DB_PASS);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(5_000);
        config.setIdleTimeout(0);
        config.setMaxLifetime(0);
        config.addDataSourceProperty("cachePrepStmts", "true");
        connectionPool = new HikariDataSource(config);
        Server.getLogger().info("Successfully connected to the Forum Database.");
    }

    public Connection getConnection() throws SQLException {
        if (connectionPool == null)
			try {
				start();
			} catch (Exception e) {
				System.out.println("CUNTING SQL");
				e.printStackTrace();
			}
		try {
	        return connectionPool.getConnection();
		} catch (Exception e) {
			System.out.println("CUNTING SQL2");
			e.printStackTrace();
		}
		return null;
    }

}
