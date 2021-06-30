package dev.nest.cj.mysql;

import com.zaxxer.hikari.HikariConfig;

public class DataSource {

    private final HikariConfig hikariConfig, hikariConfig2;

    public DataSource() {

/*
        hikariConfig = new HikariConfig();
        hikariConfig.setUsername("root");
        hikariConfig.setPassword("");
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s/%s", "127.0.0.1", "centrixcore"));
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setMaximumPoolSize(3);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.addDataSourceProperty("cachePrepStmts" , "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize" , "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        hikariConfig.addDataSourceProperty("serverName", "127.0.0.1");
        hikariConfig.addDataSourceProperty("port", 3306);
        hikariConfig.addDataSourceProperty("databaseName", "centrixcore");

 */
/*
        hikariConfig = new HikariConfig();
        hikariConfig.setUsername("centrixcore");
        hikariConfig.setPassword("1iVRWB1n0rR4OYsaapPdBB");
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s/%s", "94.130.143.221", "centrixcore"));
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.MysqlDataSource");
        hikariConfig.setMaximumPoolSize(3);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.addDataSourceProperty("cachePrepStmts" , "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize" , "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        hikariConfig.addDataSourceProperty("serverName", "94.130.143.221");
        hikariConfig.addDataSourceProperty("port", 3306);
        hikariConfig.addDataSourceProperty("databaseName", "centrixcore");

 */




        hikariConfig = new HikariConfig();
        hikariConfig.setUsername("centrixcore");
        hikariConfig.setPassword("1iVRWB1n0rR4OYsaapPdBB");
        hikariConfig.setJdbcUrl(String.format("jdbc:mysql://%s/%s", "94.130.143.221", "centrixcore"));
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig.setMaximumPoolSize(5);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.addDataSourceProperty("cachePrepStmts" , "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize" , "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        hikariConfig.addDataSourceProperty("serverName", "94.130.143.221");
        hikariConfig.addDataSourceProperty("port", 3306);
        hikariConfig.addDataSourceProperty("databaseName", "centrixcore");

        hikariConfig2 = new HikariConfig();
        hikariConfig2.setUsername("luckperms");
        hikariConfig2.setPassword("zBFw2Fx3hYM4AjZitftwXEgWjDL8");
        hikariConfig2.setJdbcUrl(String.format("jdbc:mysql://%s/%s", "94.130.143.221", "LuckPerms"));
        hikariConfig2.setDriverClassName("com.mysql.jdbc.Driver");
        hikariConfig2.setMaximumPoolSize(5);
        hikariConfig2.setMinimumIdle(1);
        hikariConfig2.addDataSourceProperty("cachePrepStmts" , "true");
        hikariConfig2.addDataSourceProperty("prepStmtCacheSize" , "250");
        hikariConfig2.addDataSourceProperty("prepStmtCacheSqlLimit" , "2048");
        hikariConfig2.addDataSourceProperty("serverName", "94.130.143.221");
        hikariConfig2.addDataSourceProperty("port", 3306);
        hikariConfig2.addDataSourceProperty("databaseName", "LuckPerms");




    }

    public HikariConfig getHikariConfig() {
        return hikariConfig;
    }

    public HikariConfig getHikariConfig2() {
        return hikariConfig2;
    }

}
