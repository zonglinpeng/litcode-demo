package com.bamboovir.litcode.litcode;

import com.bamboovir.litcode.api.RootAPI;
import com.bamboovir.litcode.config.Config;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class MainVerticle extends AbstractVerticle {
    private int port;
    private String host;
    private static final String PORT_KEY = "port";
    private static final String HOST_KEY = "host";
    private HttpServer httpServer;

    private void configServer(JsonObject config, Promise<Void> startPromise) {
        port = config.getJsonObject("litcode").getInteger(PORT_KEY);
        host = config.getJsonObject("litcode").getString(HOST_KEY);

        val root = new RootAPI(vertx).bind();
        HttpServerOptions httpServerOptions = new HttpServerOptions()
                .setPort(port)
                .setHost(host);

        httpServer = vertx.createHttpServer(httpServerOptions);

        httpServer.requestHandler(root)
                .listen(http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        log.info(String.format("litcode server started on %s:%d", host, port));
                    } else {
                        val cause = http.cause();
                        log.error(String.format("litcode server start failed on %s:%d", host, port), cause);
                        startPromise.fail(cause);
                    }
                });
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        val retriever = Config.instance(vertx).getRetriever();
        retriever.getConfig(handler -> {
            if (handler.failed()) {
                log.error("failed to retrieve the configuration", handler.cause());
            } else {
                JsonObject config = handler.result();
                log.info(config.encodePrettily());
                configServer(config, startPromise);
            }
        });
    }

    @Override
    public void stop(Promise<Void> stopPromise) throws Exception {
        log.info("stopping main verticle");
        vertx.close().onComplete(rst -> {
            if (rst.succeeded()) {
                log.info("stop main verticle success");
                stopPromise.complete();
            } else {
                String msg = "stop main verticle failed";
                log.error(msg, rst.cause());
                stopPromise.fail(rst.cause());
            }
        });
    }
}
