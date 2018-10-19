package com.jsxl.selfexam.interfaces;

/**
 * Created by Charles on 2018/7/6.
 * 对做题进行监听 用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功

 */

public interface AnswerJudgeListener {
    //用来判断答案  键为当前题目 值为0表示未选择 1表示正确 2表示错误 3表示当前 4表示阅读填写成功
    void answerJudge(int type);
}
