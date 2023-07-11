package com.zonglinpeng.litcode.dao;

import com.zonglinpeng.litcode.config.Config;
import io.vertx.core.Vertx;
import io.vertx.core.tracing.TracingPolicy;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;
import lombok.val;

public class MySQLClient {
    private MySQLPool pool;
    private Vertx vertx;

    public MySQLClient(Vertx vertx) {
        this.vertx = vertx;
    }

    public MySQLPool client() {
        if(pool != null) {
            return pool;
        }
        val config = Config.instance(vertx).config();
        val mysqlConfig = config.getJsonObject("litcode").getJsonObject("mysql");

        int port = mysqlConfig.getInteger("port");
        String host = mysqlConfig.getString("host");
        String database = mysqlConfig.getString("database");
        String username = config.getString("LITCODE_MYSQL_USERNAME");
        String password = config.getString("LITCODE_MYSQL_PASSWORD");

        MySQLConnectOptions connectOptions = new MySQLConnectOptions()
                .setTracingPolicy(TracingPolicy.ALWAYS)
                .setPort(port)
                .setHost(host)
                .setDatabase(database)
                .setUser(username)
                .setPassword(password);

        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        pool = MySQLPool.pool(vertx, connectOptions, poolOptions);
        return pool;
    }
}
