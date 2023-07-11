package com.bamboovir.litcode.api;

import com.bamboovir.litcode.service.UserService;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Slf4j
public class UserAPI {
    private final Vertx vertx;
    private final UserService userService;
    private final OAuth2Auth oAuth2Auth;

    public UserAPI(Vertx vertx, UserService userService, OAuth2Auth oAuth2Auth) {
        this.vertx = vertx;
        this.userService = userService;
        this.oAuth2Auth = oAuth2Auth;
    }

    public Router bind() {
        Router root = Router.router(vertx);
        root.get("/profile").handler(this::profile);
        root.get("/logout").handler(this::logout);
        root.get("/login").handler(this::login);
        root.post("/user-related-questions").handler(this::userRelatedQuestion);
        root.post("/create-update").handler(this::createUpdateQuestion);
        root.delete("/:questionID").handler(this::deleteQuestion);

        return root;
    }

    private void login(RoutingContext ctx) {
        ctx.redirect("/");
    }

    private void logout(RoutingContext ctx) {
        User user = ctx.user();
        ctx.session().destroy();
        ctx.redirect("/");
    }

    private void profile(RoutingContext ctx) {
        userService.profile(oAuth2Auth, ctx.user()).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }

            ctx.json(rst.result());
        });
    }

    private void userRelatedQuestion(RoutingContext ctx) {
        final JsonObject json = ctx.getBodyAsJson();
        int userID = Integer.parseInt((json.getString("userID")));
        String tagName = json.getString("tagName");
        userService.userRelatedQuestion(userID, tagName).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }

            ctx.json(rst.result());
        });
    }


    private void createUpdateQuestion(RoutingContext ctx) {
        /*
        * title        varchar(200)  not null,
        description  text          not null,
        code_content text          not null,
        hint         text          not null,
        difficulty   varchar(50)   not null,
        */
        val req = ctx.request();
        JsonObject json = ctx.getBodyAsJson();
        boolean isUpdate = Boolean.parseBoolean(json.getString("isUpdate"));
        userService.createUpdateQuestion(json).onComplete(rst -> { // rst is the return
            if (rst.failed()) {
                System.out.println("createUpdateQuestion request failed!\n"+rst.cause());
                ctx.fail(503, rst.cause());
                return;
            }
            if (!isUpdate) { // if insert, return MaxQuestionId
                System.out.println("createUpdateQuestion success!\n");
                userService.getNewQuestionId().onComplete(rst2 -> {
                    if (rst2.failed()) {
                        ctx.fail(503, rst2.cause());
                        return;
                    }
                    ctx.json(rst2.result().get(0));
                    System.out.println("getMaxQuestionId success!\n");
                });
            }
            else{
                ctx.json(rst.result());
            }
        });
    }

    private void deleteQuestion(RoutingContext ctx) {
        int questionID = Integer.parseInt(ctx.pathParam("questionID"));

        userService.deleteQuestion(questionID).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }

            ctx.json(rst.result());
        });
    }


}
