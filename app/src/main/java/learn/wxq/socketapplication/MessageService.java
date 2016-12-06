package learn.wxq.socketapplication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import learn.wxq.socketapplication.socketservice.DataParseListener;
import learn.wxq.socketapplication.socketservice.Global;
import learn.wxq.socketapplication.socketservice.PacketModel.RePacket;
import learn.wxq.socketapplication.socketservice.SavTask;
import learn.wxq.socketapplication.socketservice.ShakeAndVibrate;
import learn.wxq.socketapplication.socketservice.UserPreference;

public class MessageService extends Service {
    private SavTask savTask;
    public MessageService() {
    }

    private String testPhoneNumber="13222200760";
  //  private String testPhoneNumber="18862005675";

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

      //  ShakeAndVibrate.getInstance(MessageService.this).anewConnectSocker(0);
        ShakeAndVibrate.getInstance(this).setAccountAndKey(testPhoneNumber);
        ShakeAndVibrate.getInstance(this).addDataParseListener(new DataParseListener() {
            @Override
            public void connectionClosed(int socketState) {
                System.out.print("connectionClosed 异常");
                Intent intent = new Intent(Global.TAKE_SHIBAI);
                MessageService.this.sendBroadcast(intent);
                Intent fail = new Intent(Global.LOGINFAIL);
                MessageService.this.sendBroadcast(fail);
            }

            @Override
            public void loadingReconnect(int socketState) {
                System.out.print("loadingReconnect 异常");

                Intent intent1 = new Intent(Global.TAKE_CHONGLIAN);
                MessageService.this.sendBroadcast(intent1);
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                System.out.print("connectionClosedOnError 异常");

            }

            @Override
            public void reconnectionSuccessful(int socketState) {
                System.out.print("reconnectionSuccessful 异常");

                Intent intent1 = new Intent(Global.TAKE_OK);
                MessageService.this.sendBroadcast(intent1);
            }

            //获取所有的sock的消息并处理消息
            @Override
            public void dataReceiveSuccessful(RePacket rePacket) {
                System.out.println("接收到的数据rePacket.cmd=====" + rePacket.cmd + "-----rePacket.datastr=111=" + rePacket.datastr);


                if (rePacket != null && rePacket.cmd != null && !"".equals(rePacket.cmd)) {
                    JSONObject obj = null;
                    int dataState = 0;
                    try {
                        obj = new JSONObject(rePacket.datastr);
                        dataState = obj.getInt("dataState");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if ("Login".equals(rePacket.cmd)) {
                        if (dataState == 1) {
                            try {
                                JSONObject data = obj.getJSONObject("data");
                                String imuid = data.getString("GUid");
                                UserPreference.getInstance(MessageService.this).storeIMuid(imuid);
                                Intent intent = new Intent(Global.LOGINSUCCESS);
                                MessageService.this.sendBroadcast(intent);
                                ShakeAndVibrate.getInstance(MessageService.this).openTimetask();
//
//                                CommonStaticUtil.startReceiver(MessageService.this);
//                                CommonStaticUtil.startModifyFriendsReceiver(MessageService.this);
                            } catch (Exception e) {
                                Intent intent = new Intent(Global.LOGINFAIL);
                                MessageService.this.sendBroadcast(intent);
                                e.printStackTrace();
                            }
                        }
                    }
                }


//                if (rePacket != null && rePacket.cmd != null && !"".equals(rePacket.cmd)) {
//                    JSONObject obj = null;
//                    int dataState = 0;
//                    try {
//                        obj = new JSONObject(rePacket.datastr);
//                        dataState = obj.getInt("dataState");
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if ("Error".equals(rePacket.cmd)) {
//                        Intent intent = new Intent(Global.LOGINFAIL);
//                        MessageService.this.sendBroadcast(intent);
//                    } else if ("Login".equals(rePacket.cmd)) {
//                        if (dataState == 1) {
//                            try {
//                                JSONObject data = obj.getJSONObject("data");
//                                String imuid = data.getString("GUid");
//                                UserPreference.getInstance(MessageService.this).storeIMuid(imuid);
//                                Intent intent = new Intent(Global.LOGINSUCCESS);
//                                MessageService.this.sendBroadcast(intent);
//                                ShakeAndVibrate.getInstance(MessageService.this).openTimetask();
//                                CommonStaticUtil.startReceiver(MessageService.this);
//                                CommonStaticUtil.startModifyFriendsReceiver(MessageService.this);
//                            } catch (Exception e) {
//                                Intent intent = new Intent(Global.LOGINFAIL);
//                                MessageService.this.sendBroadcast(intent);
//                                e.printStackTrace();
//                            }
//                        } else if (dataState == 2) {
//                            try {
//                                String json = obj.getString("data");
//                                JSONObject jsonObject = new JSONObject(json);
//                                int id = jsonObject.getInt("id");
//                                if (id == 7 || id == 4) {
//                                    toLogin(jsonObject.getString("message"), "1");
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            Intent intent = new Intent(Global.LOGINFAIL);
//                            MessageService.this.sendBroadcast(intent);
//                        }
//                    } else if (rePacket.cmd.equals("ReConnect")) {
//                        if (dataState == 1) {
//                            try {
//                                ShakeAndVibrate.getInstance(MessageService.this).openTimetask();
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } else if (rePacket.cmd != null && "PrivateChat".equals(rePacket.cmd) && dataState == 1) {//单聊信息
//                        try {
//                            String data = obj.getString("data");
//                            String toUid = rePacket.toUid;
//                            String uids = rePacket.uid;
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);// 更新数据库服务器时间
//                            singleChat(data, "", toUid, "1", rePacket.time);//"1" 表示单聊
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else if (rePacket.cmd != null && "P_FriendList".equals(rePacket.cmd) && dataState == 1) {
//                        try {
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                            final StringBuffer stringBuffer = new StringBuffer();
//                            JSONArray jsonArray = obj.getJSONArray("data");
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject arrObj = jsonArray.getJSONObject(i);
//                                stringBuffer.append(arrObj.getString("guid") + ";");
//                            }
//                            UserPreference.getInstance(MessageService.this)
//                                    .storeFriendUids(
//                                            stringBuffer.toString());
//                            getList();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else if (rePacket.cmd != null && "GroupChat".equals(rePacket.cmd)) {
//                        NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                        if (dataState == 1) {
//                            try {
//                                JSONObject dataJson = obj.getJSONObject("data");
//                                String data = dataJson.getString("Content");
//                                String clazzId = dataJson.getString("GroupId");
//                                singleChat(data, clazzId, rePacket.toUid, "2", rePacket.time);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        } else if (dataState == 2) {
//                            try {
//                                String noclazz = obj.getString("data");
//                                JSONObject jsonObject = new JSONObject(noclazz);
//                                int id = jsonObject.getInt("id");
//                                if (id == 3) {
//                                    Msg msg = new Msg();
//                                    sendMsg(msg, Global.NOCLAZZ);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    } else if (rePacket.cmd != null && "P_NewFriend".equals(rePacket.cmd) && dataState == 1) {
//                        try {
//                            String puid = obj.getString("data");
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                            insertAdddb(puid.substring(0, puid.lastIndexOf(",")), puid.substring(puid.lastIndexOf(",") + 1));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else if (rePacket.cmd != null && "P_FriendRequestList".equals(rePacket.cmd) && dataState == 1) {
//                        try {
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                            HashMap<String, String> friReqmap = new HashMap<String, String>();
//                            final StringBuffer stringBuffer = new StringBuffer();
//                            JSONArray jsonArray = obj.getJSONArray("data");
//                            String friends = UserPreference.getInstance(MessageService.this).getFriendUids();
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                                if (!friends.contains(jsonObject.getString("Guid"))) {
//                                    stringBuffer.append(jsonObject.getString("Guid") + ";");
//                                    friReqmap.put(jsonObject.getString("Guid"), jsonObject.getString("id"));
//                                }
//                            }
//                            getFriendRequestList(stringBuffer.toString(), friReqmap);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else if (rePacket.cmd != null && "P_FriendAuditSuccess".equals(rePacket.cmd) && dataState == 1) {
//                        try {
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                            String puid = obj.getString("data");
//                            insertOneContact(puid);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else if (rePacket.cmd != null && "P_DeleteFriend".equals(rePacket.cmd) && dataState == 1) {
//                        try {
//                            String puid = obj.getString("data");
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                            deleteFri(puid);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else if (rePacket.cmd != null && "P_HistoryMessage".equals(rePacket.cmd) && dataState == 1) {
//                        try {
//                            ArrayList<HistoryOffline> historyOfflines = new ArrayList<HistoryOffline>();
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                            JSONObject dataJson = obj.getJSONObject("data");
//                            JSONArray jsonArrayPri = dataJson.getJSONArray("PrivateChat");
//                            for (int i = 0; i < jsonArrayPri.length(); i++) {
//                                HistoryOffline historyOffline = new HistoryOffline();
//                                JSONObject jsonObject = jsonArrayPri.getJSONObject(i);
//                                historyOffline.id = jsonObject.getInt("id");
//                                historyOffline.P_SendContent = jsonObject.getString("P_SendContent");
//                                historyOffline.P_SenderGuid = jsonObject.getString("P_SenderGuid");
//                                historyOffline.P_SaveTime = jsonObject.getString("P_SaveTime");
//                                historyOffline.P_Type = "1";
//                                historyOfflines.add(0, historyOffline);
//                            }
//                            JSONArray jsonArrayGroup = dataJson.getJSONArray("GroupChat");
//                            for (int i = 0; i < jsonArrayGroup.length(); i++) {
//                                HistoryOffline historyOffline = new HistoryOffline();
//                                JSONObject jsonObject = jsonArrayGroup.getJSONObject(i);
//                                historyOffline.P_SendContent = jsonObject.getString("P_SendContent");
//                                historyOffline.P_SenderGuid = jsonObject.getString("P_SenderGuid");
//                                historyOffline.P_GroupGuid = jsonObject.getString("P_GroupGuid");
//                                historyOffline.P_SaveTime = jsonObject.getString("P_SaveTime");
//                                historyOffline.P_Type = "2";
//                                historyOfflines.add(0, historyOffline);
//                            }
//                            Collections.sort(historyOfflines, new TimeComparatorList());
//                            for (int j = 0; j < historyOfflines.size(); j++) {
//                                if ("1".equals(historyOfflines.get(j).P_Type)) {
//                                    singleChat(historyOfflines.get(j).P_SendContent, "", historyOfflines.get(j).P_SenderGuid, historyOfflines.get(j).P_Type, historyOfflines.get(j).P_SaveTime);
//                                } else {
//                                    singleChat(historyOfflines.get(j).P_SendContent, historyOfflines.get(j).P_GroupGuid, historyOfflines.get(j).P_SenderGuid, historyOfflines.get(j).P_Type, historyOfflines.get(j).P_SaveTime);
//                                }
//                            }
//                            JSONArray jsonArrayRadio = dataJson.getJSONArray("Radio");
//                            for (int i = 0; i < jsonArrayRadio.length(); i++) {
//                                JSONObject jsonObject = jsonArrayRadio.getJSONObject(i);
//                                String content = jsonObject.getString("R_SendContent");
//                                pushRadio(content);
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    } else if (rePacket.cmd != null && "RemoteLogin".equals(rePacket.cmd) && dataState == 1) {
//                        try {
//                            String json = obj.getString("data");
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                            toLogin(json, "1");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } else if (rePacket.cmd != null && "Radio".equals(rePacket.cmd) && dataState == 1) {
//                        try {
//                            JSONObject data = obj.getJSONObject("data");
//                            String content = data.getString("Content");
//                            NewTimeManager.getInstance(MessageService.this).updateTime(uid, rePacket.time);
//                            pushRadio(content);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }



            }

            @Override
            public void cancleAsyncTask() {
                savTask.cancel(true);
            }
        });

        TelephonyManager TelephonyMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();
        String time=getNewCurrentTime();
        String type="0";//UserPreference.getInstance(this).getLoginType()  1deng  0 tuichu   867516023966903
        savTask = new SavTask(ShakeAndVibrate.getInstance(MessageService.this), MessageService.this, null, time, type, "867516023966902");
        savTask.execute();
        super.onCreate();

    }
    public static String getNewCurrentTime() {
        Date nowTime = new Date();
        nowTime.setTime(nowTime.getTime() + Global.date);
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss.SSS");
        String nowTimeStr = dateFormat.format(nowTime);
        return nowTimeStr;
    }
}
