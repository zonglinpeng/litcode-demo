package com.bamboovir.litcode.litcode;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.metrics.MetricsOptions;
import io.vertx.core.tracing.TracingOptions;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Litcode {
    public static void main(String[] args) {
        final VertxOptions options = new VertxOptions()
                .setTracingOptions(new TracingOptions())
                .setMetricsOptions(new MetricsOptions().setEnabled(true));

        final Vertx v = Vertx.vertx();
        v.deployVerticle(new MainVerticle()).onComplete(res -> {
            if (res.succeeded()) {
                log.info("deployment id: " + res.result());
                log.info("create shutdown hook");
                Runtime.getRuntime().addShutdownHook(new Thread(v::close));
            } else {
                log.error("deployment failed!");
            }
        });
    }
}
