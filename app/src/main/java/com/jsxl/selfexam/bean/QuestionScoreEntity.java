package com.jsxl.selfexam.bean;

/**
 * Created by Charles on 2018/7/12.
 */
//CHAPTERID,QUESTIONID,QUESTIONTYPE,ANSWER,ANSWERJUDGE
public class QuestionScoreEntity {
    private int chapterId;
    private int questionId;
    private int questionType;
    private String answer;
    private int answerJudge;

    public int getChapterId() {
        return chapterId;
    }

    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getAnswerJudge() {
        return answerJudge;
    }

    public void setAnswerJudge(int answerJudge) {
        this.answerJudge = answerJudge;
    }
}
