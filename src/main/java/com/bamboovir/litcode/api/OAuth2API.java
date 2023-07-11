package com.bamboovir.litcode.api;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.OAuth2AuthHandler;

public class OAuth2API {
    private final Vertx vertx;
    private final Router root;
    private final OAuth2AuthHandler oAuth2Handler;

    public OAuth2API(Vertx vertx, Router root, OAuth2AuthHandler oAuth2Handler) {
        this.vertx = vertx;
        this.root = root;
        this.oAuth2Handler = oAuth2Handler;
    }

    public void bind() {
    //    root.route("/").handler(oAuth2Handler);
    //    root.route("/api/*").handler(oAuth2Handler);
    //    root.route("/health").handler(oAuth2Handler);
    }
}
