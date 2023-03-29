package comp5216.sydney.edu.au.finalproject;

import com.blankj.utilcode.util.LogUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class ToolManager {
    private int userId;
    private String sessionId;
    private static ToolManager INSTANCE = new ToolManager();
    private static OkHttpClient client;
    private ToolManager() {
        LogUtils.getConfig().setGlobalTag("yeake");
    }


    public static ToolManager getINSTANCE() {
        return INSTANCE;
    }

    public  int getUserId() {
        return userId;
    }

    public  void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        LogUtils.i("sessionId = " + sessionId);
        this.sessionId = sessionId;
    }
    private static long lastClickTime;
    public static boolean isFastDoubleClick(){
        long time = System.currentTimeMillis();
        if(time - lastClickTime < 1500){
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static OkHttpClient getClient() {
        if(client==null)
            synchronized (ToolManager.class){
                if(client==null){
                    client = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS).build();
                }
            }
        return client;
    }
}
