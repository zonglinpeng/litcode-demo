package com.zonglinpeng.litcode.api;

import com.zonglinpeng.litcode.config.Config;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.auth.oauth2.OAuth2Options;
import io.vertx.ext.auth.oauth2.providers.GoogleAuth;
import io.vertx.ext.auth.oauth2.providers.OpenIDConnectAuth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.OAuth2AuthHandler;
import lombok.val;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class OAuth2Provider {
    private final Vertx vertx;
    private final Router root;
    private OAuth2AuthHandler oAuth2AuthHandler;
    private OAuth2Auth oauth2;
    private String clientID;
    private String clientSecret;

    public OAuth2Provider(Vertx vertx, Router root) {
        this.vertx = vertx;
        this.root = root;
        val config = Config.instance(vertx).config();
        this.clientID = config.getString("LITCODE_OAUTH2_CLIENT_ID");
        this.clientSecret = config.getString("LITCODE_OAUTH2_CLIENT_SECRET");
    }

    public Future<OAuth2Auth> openID() {
        return GoogleAuth.discover(
                vertx,
                new OAuth2Options()
                        .setClientId(clientID)
                        .setClientSecret(clientSecret)
        );
    }

    public OAuth2Auth auth() {
        if (this.oauth2 != null) {
            return this.oauth2;
        }

        oauth2 = GoogleAuth.create(
                vertx,
                clientID,
                clientSecret
        );

        return oauth2;
    }

    public OAuth2AuthHandler handler() {
        if (this.oAuth2AuthHandler != null) {
            return oAuth2AuthHandler;
        }

        OAuth2Auth authProvider = auth();

        oAuth2AuthHandler = OAuth2AuthHandler
                .create(vertx, authProvider, "http://www.litcode.com:8080/auth/google/token")
                .setupCallback(root.route("/auth/google/token"))
                .prompt("consent")
                .withScopes(
                        List.of(
                                "openid",
                                "https://www.googleapis.com/auth/userinfo.email",
                                "https://www.googleapis.com/auth/userinfo.profile"
                        )
                );


        return oAuth2AuthHandler;
    }
}
