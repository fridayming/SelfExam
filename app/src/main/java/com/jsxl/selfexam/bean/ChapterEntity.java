package com.jsxl.selfexam.bean;

import java.util.List;

/**
 * Created by Charles on 2018/6/20.
 */

public class ChapterEntity {

    /**
     * message : 查询成功
     * entity : [{"id":21,"chapterPaperName":"诗词赏析","chapterPaperType":0,"subjectId":1,"createUser":null,"createTime":null,"status":null,"questionNum":21,"subjectName":null},{"id":23,"chapterPaperName":"阅读理解","chapterPaperType":0,"subjectId":1,"createUser":null,"createTime":null,"status":null,"questionNum":0,"subjectName":null}]
     * success : true
     */

    private String message;
    private boolean success;
    /**
     * id : 21
     * chapterPaperName : 诗词赏析
     * chapterPaperType : 0
     * subjectId : 1
     * createUser : null
     * createTime : null
     * status : null
     * questionNum : 21
     * subjectName : null
     */

    private List<EntityBean> entity;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<EntityBean> getEntity() {
        return entity;
    }

    public void setEntity(List<EntityBean> entity) {
        this.entity = entity;
    }

    public static class EntityBean {
        private int id;
        private String chapterPaperName;
        private int chapterPaperType;
        private int subjectId;
        private Object createUser;
        private Object createTime;
        private Object status;
        private int questionNum;
        private Object subjectName;
        private int finishNum;
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getChapterPaperName() {
            return chapterPaperName;
        }

        public void setChapterPaperName(String chapterPaperName) {
            this.chapterPaperName = chapterPaperName;
        }

        public int getChapterPaperType() {
            return chapterPaperType;
        }

        public void setChapterPaperType(int chapterPaperType) {
            this.chapterPaperType = chapterPaperType;
        }

        public int getSubjectId() {
            return subjectId;
        }

        public void setSubjectId(int subjectId) {
            this.subjectId = subjectId;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public Object getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Object createTime) {
            this.createTime = createTime;
        }

        public Object getStatus() {
            return status;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public int getQuestionNum() {
            return questionNum;
        }

        public void setQuestionNum(int questionNum) {
            this.questionNum = questionNum;
        }

        public Object getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(Object subjectName) {
            this.subjectName = subjectName;
        }

        public int getFinishNum() {
            return finishNum;
        }

        public void setFinishNum(int finishNum) {
            this.finishNum = finishNum;
        }
    }
}
