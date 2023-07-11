package com.bamboovir.litcode.api;

import com.bamboovir.litcode.config.Config;
import com.bamboovir.litcode.dao.MySQLClient;
import com.bamboovir.litcode.dao.QuestionMySQL;
import com.bamboovir.litcode.dao.UserMySQL;
import com.bamboovir.litcode.service.QuestionService;
import com.bamboovir.litcode.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.*;
import io.vertx.ext.web.sstore.LocalSessionStore;
import lombok.extern.slf4j.Slf4j;
import lombok.val;


@Slf4j
public class RootAPI {
    private final Vertx vertx;

    public RootAPI(Vertx vertx) {
        this.vertx = vertx;
    }

    public Router bind() {
        Router root = Router.router(vertx);

        root.route().handler(CorsHandler.create(".*."));
        root.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        root.route().handler(BodyHandler.create());
        root.route().handler(LoggerHandler.create());

        OAuth2Provider oAuth2Provider = new OAuth2Provider(vertx, root);
        OAuth2AuthHandler oAuth2AuthHandler = oAuth2Provider.handler();
        OAuth2Auth oAuth2Auth = oAuth2Provider.auth();

        new OAuth2API(vertx, root, oAuth2AuthHandler).bind();

        val mysqlClient = new MySQLClient(vertx).client();
        root.mountSubRouter("/api/question",
                new QuestionAPI(
                        vertx,
                        new QuestionService(
                                new QuestionMySQL(
                                        mysqlClient
                                )
                        )
                ).bind());

        root.mountSubRouter("/api/user", new UserAPI(
                        vertx,
                        new UserService(vertx, new UserMySQL(
                                mysqlClient
                        )),
                        oAuth2Auth
                ).bind()
        );

        root.mountSubRouter("/health", new HealthCheckAPI(vertx, mysqlClient).bind());
        root.get("/login").handler(ctx -> ctx.redirect("/"));

        String staticRoot = Config
                .instance(vertx)
                .config()
                .getJsonObject("litcode")
                .getString("static");

        root.get().handler(
                StaticHandler
                        .create()
                        .setAllowRootFileSystemAccess(false)
                        .setCachingEnabled(false)
                        .setDirectoryListing(false)
                        .setWebRoot(staticRoot)
        );

        return root;
    }
}
