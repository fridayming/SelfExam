package com.jsxl.selfexam.interfaces;

import com.jsxl.selfexam.bean.ChapterEntity;
import com.jsxl.selfexam.bean.CollectSecondListEntity;
import com.jsxl.selfexam.bean.CollectFirstListEntity;
import com.jsxl.selfexam.bean.NoteEntity;
import com.jsxl.selfexam.bean.QuestionEntity;
import com.jsxl.selfexam.bean.SubjectEntity;
import com.jsxl.selfexam.bean.SystemMessageEntity;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Charles on 2018/6/12.
 */

public interface RetrofitService {
    //首页获取专业接口
    @GET("question/subject/type") // 1 专升本 2 高起本 3 高起专
    Call<SubjectEntity> getSubject(@Query("typeId") String typeId);

    //注册发送验证码
    @GET("sendCode")
    Call<String> sendCode(@Query("sendtype") String sendType, @Query("functiontype") String functiontype, @Query("time") String time, @Query("toSend") String toSend);

    //找回密码检查验证码
    @GET("checkCode")
    Call<String> checkCode(@Query("sendtype") String sendType, @Query("functiontype") String functiontype, @Query("time") String time, @Query("toSend") String toSend, @Query("code") String code);

    //注册
    @GET("register")
    Call<String> register(@Query("mobile") String mobile, @Query("pwd") String pwd, @Query("examinationtypeid") String examinationtypeid, @Query("code") String code);

    //登录
    @GET("login")
    Call<String> login(@Query("mobile") String mobile, @Query("pwd") String pwd);

    //修改密码
    @GET("updatepwd")
    Call<String> modifyPassword(@Query("userId") String userId, @Query("newpwd") String newpwd, @Query("oldpwd") String oldpwd);

    //找回密码
    @GET("retrievePwd")
    Call<String> retrievePassword(@Query("mobile") String mobile, @Query("newPwd") String newPwd, @Query("confirmPwd") String confirmPwd);

    //修改专业
    @GET("user/updateExamType")
    Call<String> updateExamType(@Query("userId") String userId, @Query("typeId") String typeId);

    //章节列表页 chapterPaperType   0表示练习 1表示试卷   http://ck.jsxlmed.com/app/question/chapterpaper/subject?subjectId=1&chapterPaperType=0
    @GET("question/chapterpaper/subject")
    Call<ChapterEntity> getChapter(@Query("subjectId") String subjectId,@Query("chapterPaperType") String chapterPaperType);

    //系统消息列表页
    @GET("sysmessage/querymessage")
    Call<SystemMessageEntity> getSystemMessage(@Query("userId") String userId);

    //修改个人信息
    @GET("user/update")
    Call<String> updateUserInfo(@Query("id") String id,@Query("stuName") String stuName,@Query("gender") String gender,@Query("age") String age);

    //获取题目信息 http://ck.jsxlmed.com/app/question/list/chapterpaper?chapterpaperId=21
    @GET("question/list/chapterpaper")
    Call<QuestionEntity> getQuestion(@Query("chapterpaperId") String subjectId,@Query("userId") String userId);

    //添加收藏
    @GET("collect/saveCollectCounts")
    Call<String> addCollect(@Query("userId") String userId,@Query("ChapterPaperId") String ChapterPaperId,@Query("questionId") String questionId);

    //删除收藏  /app/collect/cancelUserCollect?userId=26&ChapterPaperId=21&questionId=2
    @GET("collect/cancelUserCollect")
    Call<String> delCollect(@Query("userId") String userId,@Query("ChapterPaperId") String ChapterPaperId,@Query("questionId") String questionId);

    //收藏一级列表页
    @GET("collect/querySubjectCollectCounts")
    Call<CollectFirstListEntity> getCollectFirstList(@Query("userId") String userId, @Query("chapterPaperType") String chapterPaperType);

    //收藏二级列表页
    @GET("collect/queryChapterPaperCollectCounts")
    Call<CollectSecondListEntity> getCollectSecondList(@Query("userId") String userId, @Query("subjectId") String subjectId,@Query("chapterPaperType") String chapterPaperType);

    //收藏做题页面
    @GET("collect/queryChapterPaperCollectQuestion")
    Call<QuestionEntity> getCollectQuestion(@Query("userId") String userId,@Query("chapterpaperId") String chapterpaperId);

    //错题一级列表页
    @GET("errqst/querySubjectErrqstCounts")
    Call<CollectFirstListEntity> getErrorFirstList(@Query("userId") String userId, @Query("chapterPaperType") String chapterPaperType);

    //错题二级列表页
    @GET("errqst/queryChapterPaperErrqstCounts")
    Call<CollectSecondListEntity> getErrorSecondList(@Query("userId") String userId, @Query("subjectId") String subjectId,@Query("chapterPaperType") String chapterPaperType);

    //错题做题页面
    @GET("errqst/queryChapterPaperErrqstQuestion")
    Call<QuestionEntity> getErrorQuestion(@Query("userId") String userId,@Query("chapterpaperId") String chapterpaperId);

    //添加错题
    @GET("errqst/saveErrqstCounts")
    Call<String> addError(@Query("userId") String userId,@Query("ChapterPaperId") String ChapterPaperId,@Query("questionId") String questionId);

    //删除错题
    @GET("errqst/cancelUserErrqst")
    Call<String> delError(@Query("userId") String userId,@Query("ChapterPaperId") String ChapterPaperId,@Query("questionId") String questionId);

    //笔记一级列表页 http://ck.jsxlmed.com/app/note/querySubjectNoteCounts?userId=24&chapterPaperType=0
    @GET("note/querySubjectNoteCounts")
    Call<CollectFirstListEntity> getNoteFirstList(@Query("userId") String userId, @Query("chapterPaperType") String chapterPaperType);

    //笔记二级列表页 http://ck.jsxlmed.com/app/note/queryChapterPaperNoteCounts?userId=24&chapterPaperType=0&subjectId=1
    @GET("note/queryChapterPaperNoteCounts")
    Call<CollectSecondListEntity> getNoteSecondList(@Query("userId") String userId, @Query("subjectId") String subjectId,@Query("chapterPaperType") String chapterPaperType);

    //笔记详情页面 http://ck.jsxlmed.com/app/note/queryNoteDetail?userId=24&chapterPaperId=21
    @GET("note/queryNoteDetail")
    Call<NoteEntity> getNoteQuestion(@Query("userId") String userId, @Query("chapterPaperId") String chapterPaperId);

    //添加笔记
    @GET("note/addNote")//userId=&noteContent=&questionId=&chapterPaperId=
    Call<String> addNote(@Query("userId") String userId,@Query("noteContent") String noteContent,@Query("chapterPaperId") String ChapterPaperId,@Query("questionId") String questionId);

    //删除笔记
    @GET("note/delteNoteById")//userId=&Id=
    Call<String> delNote(@Query("userId") String userId,@Query("Id") String noteId);

    //修改笔记
    @GET("note/updateNoteById")//userId=&Id=&noteContent
    Call<String> updateNote(@Query("userId") String userId,@Query("Id") String noteId,@Query("noteContent") String noteContent);

    //查询笔记是否存在
    @GET("note/queryNoteByQuestionIdAndUserId")
    Call<String> queryNoteExist(@Query("userId") String userId,@Query("questionId") String questionId);

    //意见反馈
    @GET("opinionTickling/saveopinionTickling")
    Call<String> feedback(@Query("userId") String userId,@Query("ticklingContent") String ticklingContent);

}
