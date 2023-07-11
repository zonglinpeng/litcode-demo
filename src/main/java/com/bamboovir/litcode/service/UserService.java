package com.zonglinpeng.litcode.service;

import com.zonglinpeng.litcode.dao.UserDAOI;
import com.zonglinpeng.litcode.model.Question;
import com.zonglinpeng.litcode.model.UserReactionQuestion;
import com.zonglinpeng.litcode.util.Transform;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.RowSet;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.oauth2.OAuth2Auth;
import java.util.List;

public class UserService {
    private final UserDAOI userDAO;
    private final Vertx vertx;

    public UserService(Vertx vertx, UserDAOI userDAOI) {
        this.vertx = vertx;
        this.userDAO = userDAOI;
    }

    public Future<JsonObject> profile(OAuth2Auth oauth2, User user) {
        return oauth2.userInfo(user);
    }
    public Future<RowSet<JsonObject>> createUpdateQuestion(JsonObject json) {
        return userDAO.createUpdateQuestion(json);
    }
    public Future<List<JsonObject>> getNewQuestionId(){
        return userDAO.getNewQuestionId().map(Transform::rowSetToList);
    }
    public Future<RowSet<Question>> userRelatedQuestion(int userID, String tagName) {
        return userDAO.userRelatedQuestion(userID, tagName);
    }

    public Future<List<Question>> deleteQuestion(int questionsID) {
        return userDAO.deleteQuestion(questionsID).map(Transform::rowSetToList);
    }


}
