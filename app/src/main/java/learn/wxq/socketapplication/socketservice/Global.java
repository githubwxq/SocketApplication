package learn.wxq.socketapplication.socketservice;

import android.os.Environment;



import java.util.HashMap;

/**
 * Created by ztn on
 * 全局数据存放处 广播action 消息 url地址等等
 */
public class Global {
    public static final String ENCODING = "UTF-8";
    public static final String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/orange/";
    public static String requestURL = "http://api.juziwl.cn/UploadServer/phone/upload";

//            //正式库
//    public static String BoBoApi = "http://api.exiaoxin.com/";
//    public static String UrlApi = "http://api.exiaoxin.com/";
    //////      测试库
   public static String BoBoApi = "http://test.juziwl.com/exiaoxplatf/";
   public static String UrlApi = "http://test.juziwl.com/";

    public static String baseURL = UrlApi.contains("test.juziwl.com") ? "http://test.juziwl.com" : "http://exiaoxin.com";
    public static String ABOUTXIAOXIN = "http://api.juziwl.cn/data/file/public/source/html/guanyu.html";
    public static String requestURL1 = BoBoApi + "v1/upload";
    //报平安基地址
    public static final String REPORT_SAFETY = UrlApi + "api/v2/";
    public static String EschoolUrl = "http://v.juziwl.com/view/wap/index.html?";
    public static String IniUrl = "http://juziwl.com/file/xiaoxin.cnf";
    public static String XXClause = "http://52xiaoxin.com/data/file/termofservice-exue.html";
    public static final String APP_ID = "wxc0c00b4b0d24d50b";
    //    public static final String APPID = "wx3c9b90e0350de1b5";//测试用的
    public static Integer msg_Count = 0;//聊天记录的未读条数总数
    public static int onlinecount = 0;//在线学堂未读条数总数
    public static int gongzaicount = 0;//公仔未读条数总数
    public static int count = 0;//未处理的好友条数
    public static int count2 = 0;//未处理的好友条数
    public static HashMap<String, Integer> UpdateMsg = new HashMap<String, Integer>();//聊天记录的未读条数

    public static long date = 0;
    public static final String UPDATE_HEADER = "com.csbjstx.service.updateheader";
    public static String fid = "";
    public static final String ATTENDANCECANCEL = "com.csbjstx.service.attendancecancel";
    public static final String XX_NOTICECANCEL = "com.csbjstx.service.xxnoticecancel";
    public static final String SCHOOLCANCEL = "com.csbjstx.service.schoolcancel";
    public static int loginid = 0;
    public static int classInfor = 0;
    public static int homemsg = 0;
    public static int homeGroupmsg = 0;
    public static int homeGongZaimsg = 0;
    public static int homeworkFlag = 0;

    //公仔绑定的广播
    public static final String GZ_BANGDING = "com.exiaoxin.service.HASBINDDEVICE";
    public static final String GZ_NOBANGDING = "com.exiaoxin.service.HASUNBINDDEVICE";

    public static final String MSG_COMPLETED = "com.exiaoxin.service.msg";
    public static final String TAKEING_MSG_COMPLETED = "com.exiaoxin.service.takeing.msg";
    public static final String GROUP_TAKEING_MSG_COMPLETED = "com.exiaoxin.service.group.takeing.msg";
    public static final String GONGZAI_TAKEING_MSG_COMPLETED = "com.exiaoxin.service.gongzai.takeing.msg";
    public static final String LOGIN = "com.exiaoxin.service.login";
    public static final String LOGINSUCCESS = "com.exiaoxin.service.loginsuccess";
    public static final String LOGINFAIL = "com.exiaoxin.service.loginfail";
    public static final String CLEARMSG = "com.exiaoxin.service.clearmsg";
    public static final String UPDATEMSG = "com.exiaoxin.service.updatemsg";
    public static final String ADDFRIENDFRI = "com.exiaoxin.service.addFri";
    public static final String ONLINENUM = "com.exiaoxin.service.onlinenum";
    public static final String ADDFRIENDQ = "com.exiaoxin.service.add";
    public static final String NOCLAZZ = "com.exiaoxin.service.noclazz";
    public static final String BANCLAZZ = "com.exiaoxin.service.banclazz";
    public static final String TAKE_CHONGLIAN = "com.exiaoxin.service.take.chonglian";
    public static final String TAKE_SHIBAI = "com.exiaoxin.service.take.shibai";
    public static final String TAKE_OK = "com.exiaoxin.service.take.ok";
    public static final int TAKE_MSG = 1;
    public static final int TAKEING_MSG = 2;
    public static final int ADDFRIEND = 4;
    public static final int ADDFRIENDAG = 5;
    public static final int TAKEING_CHONGLIAN = 6;
    public static final int TAKEING_SHIBAI = 7;
    public static final int TAKEING_OK = 8;
    //服务页面要用
    public static final String SERVICE = "com.juzi.exiaoxin.service";
    public static final String CLEARDELETE = "com.juzi.exiaoxin.cleardeleteicon";
    public static final String SHOWNEWINFO = "com.juzi.exiaoxin.shownewinfo";
    public static final String CLICKNEWINFO = "com.juzi.exiaoxin.clicknewinfo";
    public static final String ALLRED = "com.juzi.exiaoxin.allred";
    public static final String XX_AD = "com.juzi.exiaoxin.xx_id";
    public static final String UPDATEPANEL = "com.juzi.exiaoxin.updatepanel";
    public static final String reportHTML = UrlApi.contains("test") ? UrlApi + "data/platform1/static/single/report.html" : UrlApi + "data/platform/static/single/report.html";
    public static final String ARTICLEDELETEHTML = UrlApi + "data/platform/static/single/notexist.html";
    public static final String jieshaoUrl = "http://api.juziwl.cn/data/file/public/source/html/Features_new.html";
    public static final String helpUrl = "http://api.exiaoxin.com/data/file/public/source/help/";
    public static final String ANTILOSTCANCEL = "com.csbjstx.service.antilostcancel";
    //悬赏答题界面广播
    public static final String HASITEMDELETE = "com.juzi.exiaoxin.hasItemDelete";
    //防拐防丢发布丢失与线索广播
    public static final String HasPublishLost = "com.juzi.exiaoxin.hasPublishLost";
    public static final String HasPublishClue = "com.juzi.exiaoxin.hasPublishClue";
    //红包要用到
    public static int ShareFlag = 0;
    public static String topicID = "";
    //统一图片压缩宽高
    public static final int imgWidth = 960;
    public static final int imgHeight = 1280;
    //语音存放路径
    public static final String VOICEPATH = filePath + "voice/";
    //图片保存
    public static final String SAVEPICTURE = filePath + "savepictures/";
    public static final String THETHIRDURL = UrlApi + "api/v2/openlogin/";
    public static String DELETECLASSID = "";
    public static boolean CANSTATISTIC = true;

    public static final String ASK_HIDE_RED_POINT = "com.juzi.exiaoxin.askhideredpoint";
    public static int VIEWTYPE = 0;

    public static boolean hasRedPoint = false;




    //小小q相关信息
    public static int xxqState = 0;   // 0未绑定 1 绑定
    public static String xxqId = "";  //xxqid
    public static String f_group_id="";//xxq 班级id
    public static String token="";//xxq token
    public static final String XXQ = "http://domain.gz.1251007304.clb.myqcloud.com/qrconnecter/";

}
