package com.chenay.platform;

import com.chenay.platform.utils.PreferenceUtil;

import java.io.ObjectStreamException;

/**
 * @author Y.Chen5
 */
public class ServerPreference {
    private static PreferenceUtil myPreference = PreferenceUtil.newInstance();


    public String getServerIp() {
        return myPreference.getString("serverIp", null);
    }

    public void setServerIp(String serverIp) {
        myPreference.commitString("serverIp", serverIp);
    }

    public String getServerPort() {
        return myPreference.getString("serverPort", null);
    }

    public void setServerPort(String serverPort) {
        myPreference.commitString("serverPort", serverPort);
    }

    /**
     *获取单例内部类句柄
     * @return
     *
     * Created by Chenay
     */
    private static class ServerPreferenceHolder {
            private static final ServerPreference SINGLETON = new ServerPreference();
        }
    /**
     *获取单例
     * @return
     *
     * Created by Chenay
     */
    public static ServerPreference newInstance() {
        return ServerPreferenceHolder.SINGLETON;
    }
    /**
     * 防止反序列化操作破坏单例模式 (条件) implements Serializable
     *@return
     * Created by Chenay
     */
    private Object readResolve() throws ObjectStreamException {
        return ServerPreferenceHolder.SINGLETON;
    }
}
