package com.jsxl.selfexam.bean;

import java.util.List;

/**
 * Created by Charles on 2018/7/13.
 */

public class NoteEntity {

    /**
     * message :
     * listnote : [{"mqustionid":77,"questionContent":"下列各句中，有语病的一项是","questionArticle":null,"questionAnswer":"A:士者国之宝，人才尤其是高端人才，各地都争相延聘，呈现出越来越高的流动。   ","questionType":1,"questionSolution":"A项中\u201c越来越高\u201d和\u201c流动\u201d不搭配，可改为\u201c越来越频繁的流动\u201d。","listQueryObjectfile":[],"questionItemList":[{"id":179,"questionItemContent":"以智能化为核心的又一次工业革命正席卷而来，改变着人类生活的方方面面。","questionItemType":"D","questionId":179,"createUser":null,"createTime":null,"status":null},{"id":180,"questionItemContent":"改善民生是个动态过程，随着社会保障水平的提高，老百姓的要求也越来越高。","questionItemType":"C","questionId":180,"createUser":null,"createTime":null,"status":null},{"id":181,"questionItemContent":"这一报告显示，高等职业教育就业率持续走高，毕业生对经济发展的贡献颇大。","questionItemType":"B","questionId":181,"createUser":null,"createTime":null,"status":null},{"id":182,"questionItemContent":"士者国之宝，人才尤其是高端人才，各地都争相延聘，呈现出越来越高的流动。","questionItemType":"A","questionId":182,"createUser":null,"createTime":null,"status":null}],"userNoteId":22,"userNoteContent":"546456456","createTime":1531451385000}]
     * success : true
     */

    private String message;
    private String success;
    /**
     * mqustionid : 77
     * questionContent : 下列各句中，有语病的一项是
     * questionArticle : null
     * questionAnswer : A:士者国之宝，人才尤其是高端人才，各地都争相延聘，呈现出越来越高的流动。
     * questionType : 1
     * questionSolution : A项中“越来越高”和“流动”不搭配，可改为“越来越频繁的流动”。
     * listQueryObjectfile : []
     * questionItemList : [{"id":179,"questionItemContent":"以智能化为核心的又一次工业革命正席卷而来，改变着人类生活的方方面面。","questionItemType":"D","questionId":179,"createUser":null,"createTime":null,"status":null},{"id":180,"questionItemContent":"改善民生是个动态过程，随着社会保障水平的提高，老百姓的要求也越来越高。","questionItemType":"C","questionId":180,"createUser":null,"createTime":null,"status":null},{"id":181,"questionItemContent":"这一报告显示，高等职业教育就业率持续走高，毕业生对经济发展的贡献颇大。","questionItemType":"B","questionId":181,"createUser":null,"createTime":null,"status":null},{"id":182,"questionItemContent":"士者国之宝，人才尤其是高端人才，各地都争相延聘，呈现出越来越高的流动。","questionItemType":"A","questionId":182,"createUser":null,"createTime":null,"status":null}]
     * userNoteId : 22
     * userNoteContent : 546456456
     * createTime : 1531451385000
     */

    private List<ListnoteBean> listnote;

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

    public List<ListnoteBean> getListnote() {
        return listnote;
    }

    public void setListnote(List<ListnoteBean> listnote) {
        this.listnote = listnote;
    }

    public static class ListnoteBean {
        private int mqustionid;
        private String questionContent;
        private Object questionArticle;
        private String questionAnswer;
        private int questionType;
        private String questionSolution;
        private int userNoteId;
        private String userNoteContent;
        private long createTime;
        private List<?> listQueryObjectfile;
        /**
         * id : 179
         * questionItemContent : 以智能化为核心的又一次工业革命正席卷而来，改变着人类生活的方方面面。
         * questionItemType : D
         * questionId : 179
         * createUser : null
         * createTime : null
         * status : null
         */

        private List<QuestionItemListBean> questionItemList;

        public int getMqustionid() {
            return mqustionid;
        }

        public void setMqustionid(int mqustionid) {
            this.mqustionid = mqustionid;
        }

        public String getQuestionContent() {
            return questionContent;
        }

        public void setQuestionContent(String questionContent) {
            this.questionContent = questionContent;
        }

        public Object getQuestionArticle() {
            return questionArticle;
        }

        public void setQuestionArticle(Object questionArticle) {
            this.questionArticle = questionArticle;
        }

        public String getQuestionAnswer() {
            return questionAnswer;
        }

        public void setQuestionAnswer(String questionAnswer) {
            this.questionAnswer = questionAnswer;
        }

        public int getQuestionType() {
            return questionType;
        }

        public void setQuestionType(int questionType) {
            this.questionType = questionType;
        }

        public String getQuestionSolution() {
            return questionSolution;
        }

        public void setQuestionSolution(String questionSolution) {
            this.questionSolution = questionSolution;
        }

        public int getUserNoteId() {
            return userNoteId;
        }

        public void setUserNoteId(int userNoteId) {
            this.userNoteId = userNoteId;
        }

        public String getUserNoteContent() {
            return userNoteContent;
        }

        public void setUserNoteContent(String userNoteContent) {
            this.userNoteContent = userNoteContent;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public List<?> getListQueryObjectfile() {
            return listQueryObjectfile;
        }

        public void setListQueryObjectfile(List<?> listQueryObjectfile) {
            this.listQueryObjectfile = listQueryObjectfile;
        }

        public List<QuestionItemListBean> getQuestionItemList() {
            return questionItemList;
        }

        public void setQuestionItemList(List<QuestionItemListBean> questionItemList) {
            this.questionItemList = questionItemList;
        }

        public static class QuestionItemListBean {
            private int id;
            private String questionItemContent;
            private String questionItemType;
            private int questionId;
            private Object createUser;
            private Object createTime;
            private Object status;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getQuestionItemContent() {
                return questionItemContent;
            }

            public void setQuestionItemContent(String questionItemContent) {
                this.questionItemContent = questionItemContent;
            }

            public String getQuestionItemType() {
                return questionItemType;
            }

            public void setQuestionItemType(String questionItemType) {
                this.questionItemType = questionItemType;
            }

            public int getQuestionId() {
                return questionId;
            }

            public void setQuestionId(int questionId) {
                this.questionId = questionId;
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
        }
    }
}
