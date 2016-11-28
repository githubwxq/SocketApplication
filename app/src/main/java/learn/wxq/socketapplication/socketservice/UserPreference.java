package learn.wxq.socketapplication.socketservice;

import android.content.Context;
import android.content.SharedPreferences;



/**
 * @author ztn
 * @version V_5.0.0
 * @date 2016年2月21日
 * @description 存储用户信息
 */
public class UserPreference {

    private static UserPreference pre = null;
    private Context context;
    private SharedPreferences settings;

    public void storePublishDate(String pdate) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("pdate", pdate);
        edit.commit();
    }

    public UserPreference(Context ctx) {
        if (ctx != null) {
            this.context = ctx;
            this.settings = context.getSharedPreferences("userinfo", 0);
        }
    }

    public String getPublishDate() {
        return settings.getString("pdate", "");
    }

    public static UserPreference getInstance(Context ctx) {
        if (pre == null) {
            pre = new UserPreference(ctx.getApplicationContext());
        }
        return pre;
    }

    public void storeXxqToken(String token) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("Token", token);
        edit.commit();
    }

    public String getXxqToken() {

        return settings.getString("Token", "");
    }

    public void storeUid(String uid) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("uid", uid);
        edit.commit();
    }

    public String getUid() {
        return settings.getString("uid", "");
    }

    public void storePhoneNo(String phoneno) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("phone_number", phoneno);
        edit.commit();
    }

    public String getPhoneNo() {
        return settings.getString("phone_number", "");
    }

    public void storeIsUpdate(String isUpdate) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("isUpdate", isUpdate);
        edit.commit();
    }

    public String getIsUpdate() {
        return settings.getString("isUpdate", "");
    }

    public void storeAutoLogin(int autologin) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt("auto_login", autologin);
        edit.commit();
    }

    public int getAutoLogin() {
        return settings.getInt("auto_login", 0);
    }

    public void storePassword(String Password) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("password", Password);
        edit.commit();
    }

    public String getPassword() {
        return settings.getString("password", "");
    }

    public void storeMiao(int miao) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt("miao", miao);
        edit.commit();
    }

    public int getMiao() {
        return settings.getInt("miao", 0);
    }

    public void storeToken(String accessToken) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("accessToken", accessToken);
        edit.commit();
    }

    public String getToken() {
        return settings.getString("accessToken", "");
    }

    public void storeUserName(String userName) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("fullName", userName);
        edit.commit();
    }

    public String getUserName() {
        return settings.getString("fullName", "");
    }

    public void storeFriendUids(String frienduids) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("frienduids", frienduids);
        edit.commit();
    }

    public String getFriendUids() {
        return settings.getString("frienduids", "");
    }

    public void storeServerTime(long date) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putLong("servertime", date);
        edit.commit();
    }

    public long getServerTime() {
        return settings.getLong("servertime", 0);
    }

    public void storeAvatar(String userImageUrl) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("userImageUrl", userImageUrl);
    //    log.i("storeAvatar  userImageUrl == " + userImageUrl);
        edit.commit();
    }

    public String getAvatar() {
        return settings.getString("userImageUrl", "");
    }

    public void storeRole(String totalRole) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("totalRole", totalRole);
        edit.commit();
    }

    public String getRole() {
        return settings.getString("totalRole", "");
    }

    public void storeIsIn(String isexist) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("isexist", isexist);
        edit.commit();
    }

    public String getIsIn() {
        return settings.getString("isexist", "");
    }

    public String getCodeNum() {
        return settings.getString("codenum", "");
    }

    public void storeCodeNum(String codeNum) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("codenum", codeNum);
        edit.commit();
    }

    public void storeXxnewsTime(String time) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("XxnewsTime", time);
        edit.commit();
    }

    public String getXxnewsTime() {
        return settings.getString("XxnewsTime", "");
    }

    public void storeAudio(String isin) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("audio", isin);
        edit.commit();
    }

    public String getAudio() {
        return settings.getString("audio", "2");
    }

    public void storeIMuid(String imuid) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("imuid", imuid);
        edit.commit();
    }

    public String getIMuid() {
        return settings.getString("imuid", "");
    }

    public void storeIMKey(String imkey) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("imkey", imkey);
        edit.commit();
    }

    public String getIMKey() {
        return settings.getString("imkey", "");
    }

    public void storeType(int type) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt("type", type);
        edit.commit();
    }

    public int getType() {
        return settings.getInt("type", 0);
    }

    public void storeOpenType(int openType) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putInt("openType", openType);
        edit.commit();
    }

    public int getOpenType() {
        return settings.getInt("openType", 0);
    }


    public void storeFunctionId(String id) {
        synchronized (settings) {
            SharedPreferences.Editor edit = settings.edit();
            edit.putString("id", id);
            edit.commit();
        }
    }

    public String getFunctionId() {
        return settings.getString("id", "0,0,0,0,0,0,0,0,0,0");
    }

    public void storeLoginType(String logintype) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("logintype", logintype);
        edit.commit();
    }

    public String getLoginType() {
        return settings.getString("logintype", "0");
    }

    public void storeFriendsTimeNow(String time) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("friendsTimeNow", time);
        edit.commit();
    }

    public String getFriendsTimeNow() {
        return settings.getString("friendsTimeNow", "");
    }

    public void saveAskTime(String uid, String time) {
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(uid, time);
        edit.commit();
    }

    public String getAskTime(String uid) {
        return settings.getString(uid, "");
    }

    public String getAllData() {
        StringBuffer sb = new StringBuffer();
        sb.append("\n----------------------------------UserPreference---------------------------------------\n");
        sb.append("friendsTimeNow='").append(settings.getString("friendsTimeNow", "")).append("',");
        sb.append("logintype='").append(settings.getString("logintype", "0")).append("',");
        sb.append("id='").append(settings.getString("id", "0,0,0,0,0,0,0,0,0,0")).append("',");
        sb.append("openType='").append(settings.getInt("openType", 0)).append("',");
        sb.append("type='").append(settings.getInt("type", 0)).append("',");
        sb.append("imkey='").append(settings.getString("imkey", "")).append("',");
        sb.append("imuid='").append(settings.getString("imuid", "")).append("',");
        sb.append("audio='").append(settings.getString("audio", "2")).append("',");
        sb.append("XxnewsTime='").append(settings.getString("XxnewsTime", "")).append("',");
        sb.append("codenum='").append(settings.getString("codenum", "")).append("',");
        sb.append("isexist='").append(settings.getString("isexist", "")).append("',");
        sb.append("totalRole='").append(settings.getString("totalRole", "")).append("',");
        sb.append("servertime='").append(settings.getLong("servertime", 0)).append("',");
        sb.append("frienduids='").append(settings.getString("frienduids", "")).append("',");
        sb.append("fullName='").append(settings.getString("fullName", "")).append("',");
        sb.append("uid='").append(settings.getString("uid", "")).append("'\n");
        return sb.toString();
    }

}
