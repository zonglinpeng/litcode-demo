package com.zonglinpeng.litcode.service;

import com.zonglinpeng.litcode.dao.QuestionDAOI;
import com.zonglinpeng.litcode.model.Question;
import com.zonglinpeng.litcode.model.UserReactionQuestion;
import com.zonglinpeng.litcode.model.TagHaveQuestion;
import com.zonglinpeng.litcode.util.Transform;
import io.vertx.core.Future;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.core.json.JsonObject;

import java.util.List;

public class QuestionService {
    private final QuestionDAOI questionDAO;
    public QuestionService(QuestionDAOI questionDAO) {
        this.questionDAO = questionDAO;
    }

    public Future<List<Question>> listQuestions(int start, int step) {
        return questionDAO.listQuestions(start, step).map(Transform::rowSetToList);
    }

    public Future<List<Row>> deleteLikeQuestion(String reactType, int questionsID, int userID) {
        return questionDAO.deleteLikeQuestion(reactType, questionsID, userID).map(Transform::rowSetToList);
    }

    public Future<List<UserReactionQuestion>> checkReactExists(int user_id, int question_id, String react_type){
        return questionDAO.checkReactExists(user_id, question_id, react_type).map(Transform::rowSetToList);
    }

    public Future<List<Question>> getReactCnt(int question_id){
        return questionDAO.getReactCnt(question_id).map(Transform::rowSetToList);
    }

    public Future<List<UserReactionQuestion>> getReactToQuestion(int question_id){
        return questionDAO.getReactToQuestion(question_id).map(Transform::rowSetToList);
    }

    public Future<RowSet<Row>> reactToQuestion(int user_id, int question_id, String react_type, int update){
        return questionDAO.reactToQuestion(user_id, question_id, react_type, update);
    }

    public Future<List<Question>> searchQuestions(String keyWords) {
        return questionDAO.searchQuestions(keyWords).map(Transform::rowSetToList);
    }

    public Future<List<Question>> searchQuestionID(int questionID) {
        return questionDAO.searchQuestionID(questionID).map(Transform::rowSetToList);
    }

    public  Future<RowSet<Question>> updateQuestions(int questionID, Question question){
        return questionDAO.updateQuestions(questionID, question);
    }

    public Future<List<JsonObject>> AdvancedQuery1() {
        return questionDAO.AdvancedQuery1().map(Transform::rowSetToList);
    }

    public Future<String> createTag(int questionID, String tagName) {
        return questionDAO.createTag(questionID, tagName);
    }

    public Future<String> updateTag(int questionID, String tagName) {
        return questionDAO.updateTag(questionID, tagName);
    }
}
