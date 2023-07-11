package com.zonglinpeng.litcode.api;

import com.zonglinpeng.litcode.model.Question;
import com.zonglinpeng.litcode.service.QuestionService;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.validation.ValidationHandler;
import io.vertx.ext.web.validation.builder.Parameters;
import io.vertx.json.schema.SchemaParser;
import io.vertx.json.schema.SchemaRouter;
import io.vertx.json.schema.SchemaRouterOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.sqlclient.RowSet;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static io.vertx.json.schema.common.dsl.Schemas.intSchema;

@Slf4j
public class QuestionAPI {
    private final Vertx vertx;
    private final QuestionService questionService;

    public QuestionAPI(Vertx vertx, QuestionService questionService) {
        this.vertx = vertx;
        this.questionService = questionService;
    }

    public Router bind() {
        Router root = Router.router(vertx);
        SchemaRouter schemaRouter = SchemaRouter.create(vertx, new SchemaRouterOptions());
        SchemaParser schemaParser = SchemaParser.createDraft201909SchemaParser(schemaRouter);

        val listQuestionsValidator = ValidationHandler
                .builder(schemaParser)
                .queryParameter(Parameters.param("start", intSchema()))
                .queryParameter(Parameters.param("step", intSchema()))
                .build();

//        val reactToQuestionValidator = ValidationHandler
//                .builder(schemaParser)
//                .queryParameter(Parameters.param("userID", intSchema()))
//                .build();

        root.get("/").handler(listQuestionsValidator).handler(this::listQuestions);
        root.get("/popularTag").handler(this::AdvancedQuery1);
        root.delete("/:questionID/react").handler(this::deleteLikeQuestion);
        root.get("/search").handler(this::searchQuestions);
        root.get("/:questionID/react").handler(this::getReactToQuestion);
        root.get("/:questionID/reactCnt").handler(this::getReactCnt);
        root.post("/:questionID/react").handler(this::reactToQuestion); // user_id=[]&question_id=[]&react_type=like/dislike
        root.put("/:questionID").handler(this::updateQuestions);
        root.get("/:questionID").handler((this::searchQuestionID));
        root.post("/:questionID/createtag").handler(this::createTag);
        root.post("/:questionID/updatetag").handler(this::updateTag);
        return root;
    }

    private void getReactToQuestion(RoutingContext ctx){
        val req = ctx.request();
        int questionID = Integer.parseInt(ctx.pathParam("questionID"));
        System.out.printf("getReactToQuestion: questionID : %d\n", questionID);

        questionService.getReactToQuestion(questionID).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }
            ctx.json(rst.result().get(0));
        });
    }

    private void createTag(RoutingContext ctx) {
        val req = ctx.request();
        int questionID = Integer.parseInt(ctx.pathParam("questionID"));
        final JsonObject json = ctx.getBodyAsJson();
        String tagName = json.getString("tagName");
        System.out.printf("questionID=%d, tagName=%s", questionID, tagName);

        questionService.createTag(questionID, tagName).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }
            ctx.json(rst.result());
        });
    }

    private void updateTag(RoutingContext ctx) {
        val req = ctx.request();
        int questionID = Integer.parseInt(ctx.pathParam("questionID"));
        final JsonObject json = ctx.getBodyAsJson();
        String tagName = json.getString("tagName");
        System.out.printf("questionID=%d, tagName=%s", questionID, tagName);

        questionService.updateTag(questionID, tagName).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }

            ctx.json(rst.result());
        });
    }

    private void reactToQuestion(RoutingContext ctx) {
        /*
            response "Reaction inserted/updated" on success,
            response with status code on fail.
         */
        val req = ctx.request();
        int questionID = Integer.parseInt(ctx.pathParam("questionID"));
        final JsonObject json = ctx.getBodyAsJson();
        int userID = Integer.parseInt((json.getString("userID")));
        String reactType = json.getString("reactType");
        System.out.printf("questionID=%d, userID=%d, reactType=%s\n", questionID, userID, reactType);

        questionService.checkReactExists(userID, questionID, reactType).onComplete(rst -> { // rst is the return
            if (rst.failed()){
                System.out.println("checkReactExists failed!\n");
                ctx.fail(503, rst.cause());
                return;
            }
            if (rst.result().size()>0 && rst.result().get(0).reaction_type.equals(reactType)){
                // if already done that reaction
                System.out.println("Reaction has a conflict!\n");
                ctx.fail(409, rst.cause()); // conflict request with the server
                return;
            }
            if (rst.result().size()>0){
                // proposed reaction != previous reaction, Update the reaction type
                System.out.printf("In sql : reaction_type = %s\n", rst.result().get(0).reaction_type);
                questionService.reactToQuestion(userID, questionID, reactType, 1).onComplete(rst2 -> {
                    if (rst2.failed()) {
                        System.out.println("Update Transaction failed: ");
                        System.out.println(rst2.cause().getMessage());
                        ctx.fail(503, rst2.cause());
                    } else {
                        System.out.println("Update User React To Question Transaction succeeded");
                        ctx.json("success");
                    }
                });
            }
            else { // size() = 0
                // Insert UserReactionQuestion and Question table
                questionService.reactToQuestion(userID, questionID, reactType, 0).onComplete(rst2 -> {
                    if (rst2.failed()) {
                        System.out.println("Insert Transaction failed: ");
                        System.out.println(rst2.cause().getMessage());
                        ctx.fail(503, rst2.cause());
                    } else {
                        System.out.println("Insert User React To Question Transaction succeeded");
                        ctx.json("success");
                    }
                });
            }
        });
    }

    private void deleteLikeQuestion(RoutingContext ctx) {
        val req = ctx.request();
        final JsonObject json = ctx.getBodyAsJson();
        int userID = Integer.parseInt((json.getString("userID")));
        String reactType = json.getString("reactType");
        int questionID = Integer.parseInt(ctx.pathParam("questionID"));
        System.out.printf("questionID=%d, userID=%d, reactType=%s\n", questionID, userID, reactType);

        questionService.checkReactExists(userID, questionID, reactType).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }
            if (rst.result().size() > 0 && rst.result().get(0).reaction_type.equals(reactType)) {
                questionService.deleteLikeQuestion(reactType, questionID, userID).onComplete(rst2 -> {
                    if (rst2.failed()) {
                        System.out.println(rst2.cause().getMessage());
                        ctx.fail(503, rst2.cause());
                        return;
                    }
                });
            }
            else {
                System.out.println("Delete reaction Transaction failed: ");
                ctx.fail(503, rst.cause());
                return;
            }
            ctx.json("success");
        });
    }

    private void getReactCnt(RoutingContext ctx){
        val req = ctx.request();
        int questionID = Integer.parseInt(ctx.pathParam("questionID"));
        System.out.printf("getReactCnt: questionID : %d\n", questionID);
        questionService.getReactCnt(questionID).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }
            ctx.json(rst.result().get(0));
        });
    }

    private void searchQuestions(RoutingContext ctx) {
        val req = ctx.request();

        String keyWords = req.getParam("keyWords");

        questionService.searchQuestions(keyWords).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }

            ctx.json(rst.result());
        });
    }


    private void listQuestions(RoutingContext ctx) {
        val req = ctx.request();
        int start = Integer.parseInt(req.getParam("start"));
        int step = Integer.parseInt(req.getParam("step"));

        questionService.listQuestions(start, step).onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }
            ctx.json(rst.result());
        });
    }

    private void searchQuestionID(RoutingContext ctx) {
        val req = ctx.request();

        int questionID = Integer.parseInt(req.getParam("questionID"));

        questionService.searchQuestionID(questionID).onComplete(rst -> {
            if(rst.result().size()>0) {
                ctx.json(rst.result().get(0));
            }
        });
    }

    private void updateQuestions(RoutingContext ctx) {
        int questionID = Integer.parseInt(ctx.pathParam("questionID"));

        final Question question = Json.decodeValue(ctx.getBodyAsString(),
                Question.class);

        ctx.response()
                .setStatusCode(200)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(question));

        questionService.updateQuestions(questionID, question).onComplete(rst -> {
                if (rst.failed()) {
                    ctx.fail(503, rst.cause());
                }
        });
    }

    private void AdvancedQuery1(RoutingContext ctx) {
        val req = ctx.request();

        questionService.AdvancedQuery1().onComplete(rst -> {
            if (rst.failed()) {
                ctx.fail(503, rst.cause());
                return;
            }
            ctx.json(rst.result().get(0));
        });
    }

}

