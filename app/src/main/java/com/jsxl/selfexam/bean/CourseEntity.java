package com.jsxl.selfexam.bean;

import java.util.List;

/**
 * Created by Charles on 2018/6/12.
 */

public class CourseEntity {


    /**
     * message : 查询成功
     * ficPath : http://static.jsxlmed.com/upload/medm/image/temp
     * entity : [{"courseId":32723,"name":"2018中医执业VIP通关班(面授)","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904170458.png","lessionNum":310,"teachers":null,"mobileLogoFileId":15063,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C+快速抢分D；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材及易错考题+冲刺卷+考点速记辅导书（2017版）","isFree":0,"loseDaynum":null,"id":876,"addTime":1528282116000,"sourcePrice":8800,"currentPrice":7920},{"courseId":32726,"name":"2018中医执业笔试全程班","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904170843.png","lessionNum":180,"teachers":null,"mobileLogoFileId":15069,"sellType":1,"losetype":0,"courseInfoUrl":"含考点精讲A+冲刺预测C；送笔试教材+冲刺预测试卷辅导书（2017版）","isFree":0,"loseDaynum":null,"id":874,"addTime":1528278173000,"sourcePrice":1500,"currentPrice":1350},{"courseId":32721,"name":"2018中医执业精品全程班(面授)","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904170340.png","lessionNum":180,"teachers":null,"mobileLogoFileId":15059,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A；技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材（2017版）","isFree":0,"loseDaynum":null,"id":870,"addTime":1528278040000,"sourcePrice":4000,"currentPrice":3600},{"courseId":32722,"name":"2018中医执业无忧全程班(面授)","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904170427.png","lessionNum":300,"teachers":null,"mobileLogoFileId":15061,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材及易错考题+冲刺卷辅导书（2017版）","isFree":0,"loseDaynum":null,"id":867,"addTime":1528277822000,"sourcePrice":7800,"currentPrice":7020},{"courseId":32691,"name":"2018临床助理零风险集训营","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904164324.png","lessionNum":310,"teachers":null,"mobileLogoFileId":14996,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C+快速抢分D；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送同期网络课程；送笔试+技能教材及易错考题+冲刺卷+考点速记辅导书（2017版）","isFree":0,"loseDaynum":null,"id":850,"addTime":1528268517000,"sourcePrice":5800,"currentPrice":5220},{"courseId":32932,"name":"2018临床助理医师高频考点精讲A班","loseTime":1535688000000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904155217.png","lessionNum":160,"teachers":null,"mobileLogoFileId":14842,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":849,"addTime":1528267402000,"sourcePrice":1200,"currentPrice":1080},{"courseId":32692,"name":"2018临床助理无忧全程班(面授)","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904164352.png","lessionNum":300,"teachers":null,"mobileLogoFileId":14998,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材及易错考题+冲刺卷辅导书（2017版）","isFree":0,"loseDaynum":null,"id":848,"addTime":1528267370000,"sourcePrice":7800,"currentPrice":7020},{"courseId":32688,"name":"2018临床助理精品全程班","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904164141.png","lessionNum":180,"teachers":null,"mobileLogoFileId":14990,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A；技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材（2017版）","isFree":0,"loseDaynum":null,"id":847,"addTime":1528267231000,"sourcePrice":1500,"currentPrice":1350},{"courseId":32687,"name":"2018临床助理笔试全程班","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904164105.png","lessionNum":180,"teachers":null,"mobileLogoFileId":14988,"sellType":1,"losetype":0,"courseInfoUrl":"含考点精讲A+冲刺预测C；送笔试教材+冲刺预测试卷辅导书（2017版）","isFree":0,"loseDaynum":null,"id":845,"addTime":1528266278000,"sourcePrice":1500,"currentPrice":1350},{"courseId":32690,"name":"2018临床助理VIP通关班","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904164249.png","lessionNum":310,"teachers":null,"mobileLogoFileId":14994,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C+快速抢分D；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材及易错考题+冲刺卷+考点速记辅导书（2017版）","isFree":0,"loseDaynum":null,"id":783,"addTime":1527834194000,"sourcePrice":3200,"currentPrice":2880},{"courseId":32787,"name":"2018乡村全科执业助理医师零风险集训营","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20170325/1490434793388900156.png","lessionNum":300,"teachers":null,"mobileLogoFileId":13515,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C+快速抢分D；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送同期网络课程；送笔试+技能教材及易错考题+冲刺卷+考点速记辅导书（2017版）","isFree":0,"loseDaynum":null,"id":784,"addTime":1527834192000,"sourcePrice":2900,"currentPrice":2900},{"courseId":32788,"name":"2018乡村全科执业助理医师零风险集训营(面授)","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20170325/1490434793388900156.png","lessionNum":300,"teachers":null,"mobileLogoFileId":13517,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C+快速抢分D；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送同期网络课程；送笔试+技能教材及易错考题+冲刺卷+考点速记辅导书（2017版）","isFree":0,"loseDaynum":null,"id":776,"addTime":1527761247000,"sourcePrice":5800,"currentPrice":5800},{"courseId":32604,"name":"2018中药学(367)VIP通关班","loseTime":1528559700000,"logoFileId":null,"logoFilepath":"/course/temp/201828/20180208141757.png","lessionNum":200,"teachers":null,"mobileLogoFileId":15491,"sellType":1,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":588,"addTime":1523521214000,"sourcePrice":3600,"currentPrice":1800},{"courseId":32607,"name":"2018中药学(367)无忧全程班(面授)","loseTime":1527696000000,"logoFileId":null,"logoFilepath":"/course/temp/201828/20180208141943.png","lessionNum":200,"teachers":null,"mobileLogoFileId":15497,"sellType":1,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":587,"addTime":1523521210000,"sourcePrice":2400,"currentPrice":2400},{"courseId":32745,"name":"2018中西医执业VIP通关班(面授)","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904172343.png","lessionNum":310,"teachers":null,"mobileLogoFileId":15111,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C+快速抢分D；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材及易错考题+冲刺卷+考点速记辅导书（2017版）","isFree":0,"loseDaynum":null,"id":586,"addTime":1523512914000,"sourcePrice":8800,"currentPrice":7920},{"courseId":33156,"name":"2017年京师杏林高分交流庆功宴会","loseTime":1546271750000,"logoFileId":null,"logoFilepath":"/course/temp/20171226/20171226161116.jpg","lessionNum":0,"teachers":null,"mobileLogoFileId":15311,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":529,"addTime":1522719186000,"sourcePrice":0,"currentPrice":0},{"courseId":33174,"name":"2018执业中药师资格真题精讲B班(药事管理与法规)【新课9月更新】","loseTime":1543593300000,"logoFileId":null,"logoFilepath":"/course/temp/2017831/20170831171552.jpg","lessionNum":10,"teachers":null,"mobileLogoFileId":14357,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":0,"id":523,"addTime":1522650716000,"sourcePrice":300,"currentPrice":300},{"courseId":31971,"name":"口腔种植技术（中级）技术基础班","loseTime":1546271700000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20160531/1464676878061176508.jpg","lessionNum":80,"teachers":null,"mobileLogoFileId":12015,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":522,"addTime":1522650627000,"sourcePrice":8800,"currentPrice":4800},{"courseId":32157,"name":"2017内科护理学(369)职称考试全程班","loseTime":1496160000000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20161020/1476926031688647422.png","lessionNum":80,"teachers":null,"mobileLogoFileId":12333,"sellType":0,"losetype":0,"courseInfoUrl":null,"isFree":0,"loseDaynum":null,"id":469,"addTime":1522048596000,"sourcePrice":2400,"currentPrice":1200},{"courseId":32155,"name":"2017妇产科护理学(371)职称考试全程班","loseTime":1496160000000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20161020/1476925927492597286.png","lessionNum":80,"teachers":null,"mobileLogoFileId":12329,"sellType":0,"losetype":0,"courseInfoUrl":null,"isFree":0,"loseDaynum":null,"id":468,"addTime":1522048593000,"sourcePrice":13200,"currentPrice":1200},{"courseId":32500,"name":"2018京师考研(西医综合)全科套餐","loseTime":1514649600000,"logoFileId":null,"logoFilepath":"/course/temp/2017925/20170925141552.png","lessionNum":141,"teachers":null,"mobileLogoFileId":15233,"sellType":1,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":465,"addTime":1522046434000,"sourcePrice":5600,"currentPrice":3600},{"courseId":31759,"name":"2016口腔助理VIP通关班","loseTime":1475250900000,"logoFileId":null,"logoFilepath":"","lessionNum":299,"teachers":null,"mobileLogoFileId":11621,"sellType":1,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":455,"addTime":1521615186000,"sourcePrice":5800,"currentPrice":3200},{"courseId":31794,"name":"2016口腔助理医师全日制集训营","loseTime":1506700800000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20160421/1461226110918008464.png","lessionNum":150,"teachers":null,"mobileLogoFileId":11677,"sellType":0,"losetype":0,"courseInfoUrl":null,"isFree":0,"loseDaynum":null,"id":453,"addTime":1521615172000,"sourcePrice":15000,"currentPrice":5800},{"courseId":32817,"name":"2018心理咨询师二级精品全程班（面授）","loseTime":1543507200000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20170419/1492579990039826216.jpg","lessionNum":80,"teachers":null,"mobileLogoFileId":13573,"sellType":1,"losetype":0,"courseInfoUrl":null,"isFree":0,"loseDaynum":null,"id":439,"addTime":1521534670000,"sourcePrice":7340,"currentPrice":4680},{"courseId":32902,"name":"2018京师考研（冲刺班）-政治","loseTime":1514692800000,"logoFileId":null,"logoFilepath":"/course/temp/201791/20170901111750.jpg","lessionNum":80,"teachers":null,"mobileLogoFileId":14719,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":436,"addTime":1521525727000,"sourcePrice":40000,"currentPrice":800},{"courseId":31986,"name":"2017外科护理学(370)职称考试全程班","loseTime":1495296000000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20160628/1467093144559982024.png","lessionNum":80,"teachers":null,"mobileLogoFileId":12035,"sellType":0,"losetype":0,"courseInfoUrl":null,"isFree":0,"loseDaynum":null,"id":433,"addTime":1521525466000,"sourcePrice":2400,"currentPrice":1200},{"courseId":32903,"name":"2018京师考研（强化班）-政治","loseTime":1514692800000,"logoFileId":null,"logoFilepath":"/course/temp/201791/20170901111817.jpg","lessionNum":80,"teachers":null,"mobileLogoFileId":14721,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":432,"addTime":1521525457000,"sourcePrice":40000,"currentPrice":800},{"courseId":32706,"name":"2018口腔执业无忧全程班","loseTime":1538236800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904165210.png","lessionNum":300,"teachers":null,"mobileLogoFileId":15024,"sellType":1,"losetype":0,"courseInfoUrl":"含实践技能E+考点精讲A+题库精讲B+冲刺预测C；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材及易错考题+冲刺卷辅导书（2017版）","isFree":0,"loseDaynum":null,"id":428,"addTime":1521524765000,"sourcePrice":2400,"currentPrice":2160},{"courseId":32504,"name":"2017中医师承及确有专长精讲A班","loseTime":1504152000000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20170405/1491383803546390347.png","lessionNum":80,"teachers":null,"mobileLogoFileId":12973,"sellType":0,"losetype":0,"courseInfoUrl":null,"isFree":0,"loseDaynum":null,"id":422,"addTime":1521513144000,"sourcePrice":2400,"currentPrice":900},{"courseId":32888,"name":"【2017京师杏林高分学员的交流】","loseTime":1510502100000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20170823/1503474082568343108.png","lessionNum":1,"teachers":null,"mobileLogoFileId":13777,"sellType":2,"losetype":0,"courseInfoUrl":null,"isFree":0,"loseDaynum":null,"id":202,"addTime":1515131721000,"sourcePrice":0,"currentPrice":0},{"courseId":32889,"name":"京师考研·2018西医综合考前冲刺集训营","loseTime":1516088400000,"logoFileId":null,"logoFilepath":"/upload/mavendemo/course/20170823/1503474225672312640.png","lessionNum":1,"teachers":null,"mobileLogoFileId":13751,"sellType":2,"losetype":0,"courseInfoUrl":null,"isFree":0,"loseDaynum":null,"id":196,"addTime":1514971522000,"sourcePrice":1,"currentPrice":0.01},{"courseId":32900,"name":"2018京师考研（强化班）-英语","loseTime":1514692800000,"logoFileId":null,"logoFilepath":"/course/temp/201791/20170901111619.jpg","lessionNum":180,"teachers":null,"mobileLogoFileId":14715,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":194,"addTime":1514964201000,"sourcePrice":48000,"currentPrice":2880},{"courseId":33167,"name":"2018执业中药师资格考试高频考点精讲A班(中药学综合知识与技能)","loseTime":1543593300000,"logoFileId":null,"logoFilepath":"/course/temp/2017831/20170831171814.jpg","lessionNum":50,"teachers":null,"mobileLogoFileId":14367,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":null,"id":117,"addTime":1514518769000,"sourcePrice":500,"currentPrice":500},{"courseId":32905,"name":"2018临床执业医师真题题库精讲B班","loseTime":1535644800000,"logoFileId":null,"logoFilepath":"/course/temp/201794/20170904154625.png","lessionNum":80,"teachers":null,"mobileLogoFileId":14820,"sellType":0,"losetype":0,"courseInfoUrl":"","isFree":0,"loseDaynum":0,"id":112,"addTime":1514280239000,"sourcePrice":1200,"currentPrice":1080}]
     * success : true
     */

    private String message;
    private String ficPath;
    private boolean success;
    /**
     * courseId : 32723
     * name : 2018中医执业VIP通关班(面授)
     * loseTime : 1538236800000
     * logoFileId : null
     * logoFilepath : /course/temp/201794/20170904170458.png
     * lessionNum : 310
     * teachers : null
     * mobileLogoFileId : 15063
     * sellType : 1
     * losetype : 0
     * courseInfoUrl : 含实践技能E+考点精讲A+题库精讲B+冲刺预测C+快速抢分D；直播课+答疑+技能不过次年免费重修，笔试不过次年3折；送笔试+技能教材及易错考题+冲刺卷+考点速记辅导书（2017版）
     * isFree : 0
     * loseDaynum : null
     * id : 876
     * addTime : 1528282116000
     * sourcePrice : 8800.0
     * currentPrice : 7920.0
     */

    private List<EntityBean> entity;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFicPath() {
        return ficPath;
    }

    public void setFicPath(String ficPath) {
        this.ficPath = ficPath;
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
        private int courseId;
        private String name;
        private long loseTime;
        private Object logoFileId;
        private String logoFilepath;
        private int lessionNum;
        private Object teachers;
        private int mobileLogoFileId;
        private int sellType;
        private int losetype;
        private String courseInfoUrl;
        private int isFree;
        private Object loseDaynum;
        private int id;
        private long addTime;
        private double sourcePrice;
        private double currentPrice;

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getLoseTime() {
            return loseTime;
        }

        public void setLoseTime(long loseTime) {
            this.loseTime = loseTime;
        }

        public Object getLogoFileId() {
            return logoFileId;
        }

        public void setLogoFileId(Object logoFileId) {
            this.logoFileId = logoFileId;
        }

        public String getLogoFilepath() {
            return logoFilepath;
        }

        public void setLogoFilepath(String logoFilepath) {
            this.logoFilepath = logoFilepath;
        }

        public int getLessionNum() {
            return lessionNum;
        }

        public void setLessionNum(int lessionNum) {
            this.lessionNum = lessionNum;
        }

        public Object getTeachers() {
            return teachers;
        }

        public void setTeachers(Object teachers) {
            this.teachers = teachers;
        }

        public int getMobileLogoFileId() {
            return mobileLogoFileId;
        }

        public void setMobileLogoFileId(int mobileLogoFileId) {
            this.mobileLogoFileId = mobileLogoFileId;
        }

        public int getSellType() {
            return sellType;
        }

        public void setSellType(int sellType) {
            this.sellType = sellType;
        }

        public int getLosetype() {
            return losetype;
        }

        public void setLosetype(int losetype) {
            this.losetype = losetype;
        }

        public String getCourseInfoUrl() {
            return courseInfoUrl;
        }

        public void setCourseInfoUrl(String courseInfoUrl) {
            this.courseInfoUrl = courseInfoUrl;
        }

        public int getIsFree() {
            return isFree;
        }

        public void setIsFree(int isFree) {
            this.isFree = isFree;
        }

        public Object getLoseDaynum() {
            return loseDaynum;
        }

        public void setLoseDaynum(Object loseDaynum) {
            this.loseDaynum = loseDaynum;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public long getAddTime() {
            return addTime;
        }

        public void setAddTime(long addTime) {
            this.addTime = addTime;
        }

        public double getSourcePrice() {
            return sourcePrice;
        }

        public void setSourcePrice(double sourcePrice) {
            this.sourcePrice = sourcePrice;
        }

        public double getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(double currentPrice) {
            this.currentPrice = currentPrice;
        }
    }
}
