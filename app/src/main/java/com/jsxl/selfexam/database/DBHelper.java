package com.jsxl.selfexam.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jsxl.selfexam.bean.QuestionEntity;
import com.jsxl.selfexam.bean.QuestionScoreEntity;
import com.jsxl.selfexam.bean.RecordEntity;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static android.R.attr.version;
import static android.icu.text.MessagePattern.ArgType.SELECT;
import static com.jsxl.selfexam.R.string.year;

/**
 * Created by Charles on 2018/7/12.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "my.db";
    public static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS EXAMRECORD(_ID INTEGER PRIMARY KEY AUTOINCREMENT ,CHAPTERID,QUESTIONID,QUESTIONTYPE,ANSWER,ANSWERJUDGE);");
        db.execSQL("CREATE TABLE IF NOT EXISTS RECORD(_ID INTEGER PRIMARY KEY AUTOINCREMENT ,chapterPaperType,SUBJECTID,CHAPTERID,CHAPTERNAME,QUESTIONNUMBER,CREATETIME);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            //删除旧表 创建新表
            db.execSQL("DROP TABLE IF EXISTS EXAMRECORD");
            db.execSQL("DROP TABLE IF EXISTS RECORD");
            //创建新表
            onCreate(db);
        }
    }

    //获取当前章节的已做题目数量
    public HashMap<Integer, Integer> getQuestionNumByChapterId() {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT CHAPTERID,COUNT(CHAPTERID) AS NUM FROM EXAMRECORD GROUP BY CHAPTERID";
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        HashMap<Integer, Integer> questionNumberByChapterId = new HashMap<>();
        if (count == 0) {
            return questionNumberByChapterId;
        }
        while (count > 0 && !cursor.isLast()) {
            cursor.moveToNext();
            int chapterId = cursor.getInt(cursor.getColumnIndex("CHAPTERID"));
            int num = cursor.getInt(cursor.getColumnIndex("NUM"));
            questionNumberByChapterId.put(chapterId, num);
        }
        cursor.close();
        return questionNumberByChapterId;
    }

    //获取某一科目某一类型的做题记录
    public List<RecordEntity> getRecordBySubjectId(int subjectId, int chapterPaperType) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM RECORD WHERE subjectId = " + subjectId + " AND chapterPaperType = " + chapterPaperType+" ORDER BY CREATETIME DESC;";
        Cursor cursor = db.rawQuery(sql, null);
        List<RecordEntity> records = new ArrayList<>();
        int count = cursor.getCount();
        if (count == 0) {
            return records;
        }
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RecordEntity record = new RecordEntity();
            record.setChapterName(cursor.getString(cursor.getColumnIndex("CHAPTERNAME")));
            record.setChapterId(cursor.getString(cursor.getColumnIndex("CHAPTERID")));
            record.setCreateTime(cursor.getLong(cursor.getColumnIndex("CREATETIME")));
            record.setChapterNumber(cursor.getString(cursor.getColumnIndex("QUESTIONNUMBER")));
            records.add(record);
            cursor.moveToNext();
        }
        cursor.close();
        return records;
    }

    //获取当前章节的已做题目信息
    public HashMap<Integer, QuestionScoreEntity> getQuestionByChapterId(int chapterId) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM EXAMRECORD WHERE CHAPTERID = " + chapterId;
        Cursor cursor = db.rawQuery(sql, null);
        HashMap<Integer, QuestionScoreEntity> questions = new HashMap<>();
        int count = cursor.getCount();
        if (count == 0) {
            return questions;
        }//CHAPTERID,QUESTIONID,QUESTIONTYPE,ANSWER,ANSWERJUDGE
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            QuestionScoreEntity question = new QuestionScoreEntity();
            // db.execSQL("create table if not exists ASSOCIATEDS(_id integer primary key autoincrement ,rid,qid,answer,choose,score)");
            int questionId = cursor.getInt(cursor.getColumnIndex("QUESTIONID"));
            question.setChapterId(cursor.getInt(cursor.getColumnIndex("CHAPTERID")));
            question.setQuestionId(questionId);
            question.setQuestionType(cursor.getInt(cursor.getColumnIndex("QUESTIONTYPE")));
            question.setAnswer(cursor.getString(cursor.getColumnIndex("ANSWER")));
            question.setAnswerJudge(cursor.getInt(cursor.getColumnIndex("ANSWERJUDGE")));
            questions.put(questionId, question);
            cursor.moveToNext();
        }
        cursor.close();
        return questions;
    }

    //删除某章节做题记录
    public void delQuestionByChapterId(int chapterId) {
        // TODO Auto-generated method stub
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM EXAMRECORD WHERE CHAPTERID =" + chapterId;
        db.execSQL(sql);
        db.close();
    }

    //添加做题数据 如果存在则修改 不存在则插入数据
    public void addQuestionRecord(int chapterid, int questionId, int questionType, String answer, int answerJudge) {
        //CHAPTERID,QUESTIONID,QUESTIONTYPE,ANSWER,ANSWERJUDGE
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM EXAMRECORD WHERE QUESTIONId =" + questionId + ";";
        String sql1 = null;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count > 0) {
            sql1 = "UPDATE EXAMRECORD SET ANSWER = '" + answer + "',ANSWERJUDGE = " + answerJudge
                    + " WHERE QUESTIONID = " + questionId;
        } else {
            sql1 = "INSERT INTO EXAMRECORD(CHAPTERID,QUESTIONID,QUESTIONTYPE,ANSWER,ANSWERJUDGE) values(" + chapterid
                    + "," + questionId + "," + questionType + ",'" + answer + "'," + answerJudge + ");";
        }
        db.execSQL(sql1);
        cursor.close();
        db.close();
    }
//    //删除做题数据 多选题使用
//    public void delQuestionRecord(int chapterid, int questionId, int questionType, String answer, int answerJudge) {
//        //CHAPTERID,QUESTIONID,QUESTIONTYPE,ANSWER,ANSWERJUDGE
//        SQLiteDatabase db = getWritableDatabase();
//        String sql = "DELETE FROM EXAMRECORD WHERE QUESTIONId =" + questionId + ";";
//        db.execSQL(sql);
//        db.close();
//    }
    //添加记录 如果存在则修改 不存在则插入数据
    public void addRecord(int chapterPaperType, int subjectId, int chapterId,String chapterName, String chapterNumber, Long createTime) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM RECORD WHERE CHAPTERID =" + chapterId + ";";
        String sql1 = null;
        Cursor cursor = db.rawQuery(sql, null);
        int count = cursor.getCount();
        if (count > 0) {
            sql1 = "UPDATE RECORD SET CREATETIME = " + createTime + " WHERE CHAPTERID = " + chapterId;
        } else {
            sql1 = "INSERT INTO RECORD(chapterPaperType,SUBJECTID,CHAPTERID,CHAPTERNAME,QUESTIONNUMBER,CREATETIME) values(" + chapterPaperType
                    + "," + subjectId  + "," + chapterId + ",'" + chapterName + "','" + chapterNumber + "'," + createTime+ ");";
        }
        db.execSQL(sql1);
        cursor.close();
        db.close();
    }
    //删除做题数据
    public void delQuestionRecord(int questionId) {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM EXAMRECORD WHERE QUESTIONID =" + questionId;
        db.execSQL(sql);
        db.close();
    }
}
