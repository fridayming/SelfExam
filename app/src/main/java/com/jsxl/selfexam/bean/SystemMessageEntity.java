package com.jsxl.selfexam.bean;

import java.util.List;

/**
 * Created by Charles on 2018/6/25.
 */

public class SystemMessageEntity {
    /**
     * message :
     * listSystemMessge : [{"id":21,"messgeContent":"123123","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":22,"messgeContent":"jiangweichao","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1529660401000,"status":1},{"id":23,"messgeContent":"老马1","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":24,"messgeContent":"老马2","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":25,"messgeContent":"老马3","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":26,"messgeContent":"老马4","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":27,"messgeContent":"老马5","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":28,"messgeContent":"老马6","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":29,"messgeContent":"老马7","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":30,"messgeContent":"老马8","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":31,"messgeContent":"老马9","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0},{"id":32,"messgeContent":"老马10","messgeType":1,"recipientUserid":23,"createUser":null,"createTime":1528960130000,"status":0}]
     * success : true
     */

    private String message;
    private String success;
    /**
     * id : 21
     * messgeContent : 123123
     * messgeType : 1
     * recipientUserid : 23
     * createUser : null
     * createTime : 1528960130000
     * status : 0
     */

    private List<ListSystemMessgeBean> listSystemMessge;

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

    public List<ListSystemMessgeBean> getListSystemMessge() {
        return listSystemMessge;
    }

    public void setListSystemMessge(List<ListSystemMessgeBean> listSystemMessge) {
        this.listSystemMessge = listSystemMessge;
    }

    public static class ListSystemMessgeBean {
        private int id;
        private String messgeContent;
        private int messgeType;
        private int recipientUserid;
        private Object createUser;
        private long createTime;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMessgeContent() {
            return messgeContent;
        }

        public void setMessgeContent(String messgeContent) {
            this.messgeContent = messgeContent;
        }

        public int getMessgeType() {
            return messgeType;
        }

        public void setMessgeType(int messgeType) {
            this.messgeType = messgeType;
        }

        public int getRecipientUserid() {
            return recipientUserid;
        }

        public void setRecipientUserid(int recipientUserid) {
            this.recipientUserid = recipientUserid;
        }

        public Object getCreateUser() {
            return createUser;
        }

        public void setCreateUser(Object createUser) {
            this.createUser = createUser;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
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
