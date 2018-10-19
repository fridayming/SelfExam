package com.jsxl.selfexam.bean;

import java.util.List;

/**
 * Created by Charles on 2018/6/13.
 */

public class SubjectEntity {
    //记录中使用 判断选中位置
    private int pos;
    /**
     * message : 查询成功
     * entity : [{"id":4,"subjectName":"政治","examinationTypeId":1,"createUser":null,"createTime":null,"status":0}]
     * success : true
     */

    private String message;
    private boolean success;

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    /**
     * id : 4
     * subjectName : 政治
     * examinationTypeId : 1
     * createUser : null
     * createTime : null
     * status : 0
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
        private String subjectName;
        private int examinationTypeId;
        private Object createUser;
        private Object createTime;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public int getExaminationTypeId() {
            return examinationTypeId;
        }

        public void setExaminationTypeId(int examinationTypeId) {
            this.examinationTypeId = examinationTypeId;
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
