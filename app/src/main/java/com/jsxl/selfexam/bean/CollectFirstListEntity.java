package com.jsxl.selfexam.bean;

import java.util.List;

/**
 * Created by Charles on 2018/7/9.
 */

public class CollectFirstListEntity {


    /**
     * message :
     * list : [{"id":1,"subjectName":"语文","counts":8}]
     * success : true
     */

    private String message;
    private String success;
    /**
     * id : 1
     * subjectName : 语文
     * counts : 8
     */

    private List<ListBean> list;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private int id;
        private String subjectName;
        private int counts;

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

        public int getCounts() {
            return counts;
        }

        public void setCounts(int counts) {
            this.counts = counts;
        }
    }
}
