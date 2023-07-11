package com.zonglinpeng.litcode.config;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class Config {
    private static Config config = null;

    private final Vertx vertx;
    @Getter
    private final ConfigRetriever retriever;

    private Config(Vertx vertx) {
        this.vertx = vertx;
        this.retriever = createRetriever();
    }

    public static Config instance(Vertx vertx) {
        if (config == null) {
            config = new Config(vertx);
        }
        return config;
    }

    public JsonObject config() {
        return retriever.getCachedConfig();
    }

    private static ConfigRetrieverOptions options() {
        ConfigStoreOptions defaultStore = new ConfigStoreOptions()
                .setType("json")
                .setConfig(new JsonObject()
                        .put("litcode", new JsonObject()
                                .put("port", 8080)
                                .put("host", "127.0.0.1")
                                .put("static", "assets")
                                .put("mysql", new JsonObject()
                                        .put("port", 3306)
                                        .put("host", "127.0.0.1")
                                        .put("database", "litcode")
                                )
                        )
                );

        ConfigStoreOptions fileStore = new ConfigStoreOptions()
                .setType("file")
                .setOptional(true)
                .setConfig(new JsonObject().put("path", "config.json"));

        ConfigStoreOptions sysStore = new ConfigStoreOptions()
                .setType("sys")
                .setConfig(new JsonObject()
                        .put("cache", true)
                        .put("hierarchical", true)
                );

        ConfigStoreOptions envStore = new ConfigStoreOptions()
                .setType("env").setConfig(
                        new JsonObject()
                                .put("hierarchical", true)
                                .put("keys", new JsonArray()
                                        .add("LITCODE_MYSQL_USERNAME")
                                        .add("LITCODE_MYSQL_PASSWORD")
                                        .add("LITCODE_OAUTH2_CLIENT_ID")
                                        .add("LITCODE_OAUTH2_CLIENT_SECRET")
                                )
                );

        return new ConfigRetrieverOptions()
                .addStore(defaultStore)
                .addStore(fileStore)
                .addStore(sysStore)
                .addStore(envStore);
    }

    private ConfigRetriever createRetriever() {
        return ConfigRetriever.create(Objects.requireNonNull(vertx), options());
    }


}
