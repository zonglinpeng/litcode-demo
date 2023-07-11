package com.bamboovir.litcode.api;

import io.vertx.core.Vertx;
import io.vertx.ext.healthchecks.HealthCheckHandler;
import io.vertx.ext.healthchecks.Status;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.mysqlclient.MySQLPool;

public class HealthCheckAPI {
    private final Vertx vertx;
    private final MySQLPool mySQLClient;

    public HealthCheckAPI(Vertx vertx, MySQLPool mySQLClient) {
        this.vertx = vertx;
        this.mySQLClient = mySQLClient;
    }

    public Router bind() {
        Router root = Router.router(vertx);
        HealthCheckHandler healthCheckHandler = HealthCheckHandler.create(vertx);
        healthCheckHandler.register(
                "database",
                5000,
                promise -> mySQLClient.getConnection(connection -> {
                    if (connection.failed()) {
                        promise.fail(connection.cause());
                    } else {
                        connection.result().close();
                        promise.complete(Status.OK());
                    }
                }));
        root.get("/").handler(healthCheckHandler);
        return root;
    }

}
