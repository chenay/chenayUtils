package com.chenay.platform.device;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.content.Context.TELEPHONY_SERVICE;
import static android.content.Context.WIFI_SERVICE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_EDGE;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GPRS;
import static android.telephony.TelephonyManager.NETWORK_TYPE_GSM;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSDPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_HSUPA;
import static android.telephony.TelephonyManager.NETWORK_TYPE_UMTS;

/**
 * @author Y.Chen5
 */
public class DevicesMessage {

    private static final String NETWORK_TYPE_NONE = "0_NONE";
    private static final String NETWORK_TYPE_NET = "1_MOBILE_";
    private static final String NETWORK_TYPE_WIFI = "2_WIFI";
    private static final String NETWORK_TYPE_WAP = "4_WAP";
    public static DevicesInfos DEVICES_INFOS;

    private static DevicesInfos initInfo() {
        if (DEVICES_INFOS == null) {
            DEVICES_INFOS = new DevicesInfos();
            updateBuildInfo();
        }
        return DEVICES_INFOS;
    }

    public static DevicesInfos update(Context context) {
        initInfo();
        DEVICES_INFOS.setLanguageTag(LanguageHelper.getLanguageTag(context));
        updateNetworkInfo(context);
        updateTelephonyInfo(context);
        updateWifiInfo(context);
        DEVICES_INFOS.setAppVersion(getAppVersionName(context));
        return DEVICES_INFOS;
    }

    private static void updateBuildInfo() {
        initInfo();
        String serial = Build.SERIAL;
        String brand = Build.BRAND;
        String tags = Build.TAGS;
        String device = Build.DEVICE;
        String fingerprint = Build.FINGERPRINT;
        String bootloader = Build.BOOTLOADER;
        String release = Build.VERSION.RELEASE;
        String sdk = Build.VERSION.SDK;
        String sdk_INT = Build.VERSION.SDK_INT + "";
        String codename = Build.VERSION.CODENAME;
        String incremental = Build.VERSION.INCREMENTAL;
        String cpuabi = Build.CPU_ABI;
        String cpuabi2 = Build.CPU_ABI2;
        String board = Build.BOARD;
        String model = Build.MODEL;
        String product = Build.PRODUCT;
        String type = Build.TYPE;
        String user = Build.USER;
        String disply = Build.DISPLAY;
        String hardware = Build.HARDWARE;
        String host = Build.HOST;
        String changshang = Build.MANUFACTURER;
        String b_id = Build.ID;
        String gjtime = Build.TIME + "";
        String radiovis = Build.getRadioVersion();


        final DevicesInfos.BuildInfo buildInfo = new DevicesInfos.BuildInfo();
        buildInfo.setBrand(Build.BRAND);
        buildInfo.setDevice(Build.DEVICE);
        buildInfo.setRelease(Build.VERSION.RELEASE);
        buildInfo.setSerial(Build.SERIAL);

        DEVICES_INFOS.setBuildInfo(buildInfo);
    }

    /**
     * 更新用户信息
     *
     * @param userName
     * @param staffId
     */
    public static void updateUserInfo(String userName, String staffId) {
        initInfo();
        DevicesInfos.UserInfo userInfo = new DevicesInfos.UserInfo();
        userInfo.setStaffId(staffId);
        userInfo.setUserName(userName);
        DEVICES_INFOS.setUserInfo(userInfo);
    }

    public static void updateWifiInfo(Context context) {
        initInfo();
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        String single = wifi.getConnectionInfo().getRssi() + "";
        String ssid = wifi.getConnectionInfo().getSSID();
        String bssid = wifi.getConnectionInfo().getBSSID();

        DevicesInfos.WifiInfo wifiInfo = new DevicesInfos.WifiInfo();
        wifiInfo.setBssid(bssid);
        wifiInfo.setSsid(ssid);
        wifiInfo.setSignal(single);
        DEVICES_INFOS.setWifiInfo(wifiInfo);
    }

    public static void updateNetworkInfo(Context context) {
        initInfo();
        //获取网络连接管理者
        ConnectivityManager connectionManager = (ConnectivityManager)
                context.getSystemService(CONNECTIVITY_SERVICE);
        //获取网络的状态信息，有下面三种方式
        NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();


        final DevicesInfos.NetworkInfo networkInfo1 = new DevicesInfos.NetworkInfo();
        if (networkInfo != null) {
            networkInfo1.setmTypeName(networkInfo.getTypeName());
            networkInfo1.setmExtraInfo(networkInfo.getExtraInfo());
            networkInfo1.setmIsAvailable(networkInfo.isConnected());
        }
        networkInfo1.setIpAddress(getLocalIpAddress());
        networkInfo1.setMac(getMacAddress());
        DEVICES_INFOS.setNetworkInfo(networkInfo1);
    }

    public static void updateTelephonyInfo(Context context) {
        initInfo();
        final DevicesInfos.TelephonyInfo telephonyInfo = new DevicesInfos.TelephonyInfo();
        TelephonyManager phone = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);

        String simoperator = phone.getSimOperator();
        String simoperatorname = phone.getSimOperatorName();
        String simcountryiso = phone.getSimCountryIso();
        String workType = phone.getNetworkType() + "";
        String netcountryiso = phone.getNetworkCountryIso();
        String netoperator = phone.getNetworkOperator();
        String netoperatorname = phone.getNetworkOperatorName();
        String phonetype = phone.getPhoneType() + "";
        String simstate = phone.getSimState() + "";
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            @SuppressLint("HardwareIds") String imei = phone.getDeviceId();
            @SuppressLint("HardwareIds") String imsi = phone.getSubscriberId();
            @SuppressLint("HardwareIds") String number = phone.getLine1Number();
            @SuppressLint("HardwareIds") String simserial = phone.getSimSerialNumber();
            telephonyInfo.setDeviceId(imei);
            telephonyInfo.setPhoneNumber(number);
            telephonyInfo.setSimSerial(simserial);
        }
        telephonyInfo.setSimOperatorName(simoperatorname);
        telephonyInfo.setNetworkType(workType);
        telephonyInfo.setNetworkCountryIso(netcountryiso);
        telephonyInfo.setNetworkOperatorName(netoperatorname);

        DEVICES_INFOS.setTelephonyInfo(telephonyInfo);
    }

    /**
     * 获取MAC地址
     *
     * @return
     */
    @SuppressLint("HardwareIds")
    public static String getMacAddress() {
        try {
            for (
                    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    networkInterfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                if ("wlan0".equals(networkInterface.getName())) {
                    byte[] hardwareAddress = networkInterface.getHardwareAddress();
                    if (hardwareAddress == null || hardwareAddress.length == 0) {
                        continue;
                    }
                    StringBuilder buf = new StringBuilder();
                    for (byte b : hardwareAddress) {
                        buf.append(String.format("%02X:", b));
                    }
                    if (buf.length() > 0) {
                        buf.deleteCharAt(buf.length() - 1);
                    }
                    String mac = buf.toString();
                    return mac;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 判断是否已连接到网络.
     */
    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return info.isAvailable();
                }
            }
        }
        return false;
    }

    /**
     * 检查网络接连类型.
     *
     * @param context
     * @return SysConstants.NETWORK_TYPE_NONE: 无网络连接;
     * SysConstants.NETWORK_TYPE_WIFI: 通过WIFI连接网络;
     * SysConstants.NETWORK_TYPE_WAP: 通过GPRS连接网络.
     */
    private static String checkNetWorkType(Context context) {
        if (isAirplaneModeOn(context)) {
            return NETWORK_TYPE_NONE;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return NETWORK_TYPE_WIFI;
        }

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
            String type = connectivityManager.getActiveNetworkInfo().getExtraInfo();
            if ("wap".equalsIgnoreCase(type.substring(type.length() - 3))) {
                return NETWORK_TYPE_WAP;
            } else {
                return NETWORK_TYPE_NET + type;
            }
        }

        return NETWORK_TYPE_NONE;
    }

    /**
     * 判断手机是否处于飞行模式.
     *
     * @param context
     * @return
     */
    private static boolean isAirplaneModeOn(Context context) {
        return Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) != 0;
    }


    /**
     * 获取当前App进程的Name
     *
     * @param context
     * @param processId
     * @return
     */
    private static String getAppProcessName(Context context, int processId) {
        String processName = null;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取所有运行App的进程集合
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = context.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == processId) {
                    CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                Log.e(DevicesMessage.class.getName(), e.getMessage(), e);
            }
        }
        return processName;
    }


    /**
     * 获取ICCID
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    private static String getIccid(Context context) {
        TelephonyManager telephonyMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "no permission";
        }
        return telephonyMgr.getSimSerialNumber();
    }

    /**
     * 获取IMSI
     *
     * @param context
     * @return
     */
    @SuppressLint("HardwareIds")
    private static String getIMSI(Context context) {
        TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "no permission";
        }
        return mTelephonyMgr.getSubscriberId();
    }

    /**
     * 获取当前的IP地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        try {
            for (
                    Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                    networkInterfaces.hasMoreElements(); ) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                for (Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                     inetAddresses.hasMoreElements(); ) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    //过滤Loopback address, Link-local address
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
        }
        return null;
    }

    /**
     * 获取手机网络类型
     *
     * @param context
     * @return
     */
    private static String getPhoneType(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = tm.getNetworkType();
        if (networkType == NETWORK_TYPE_GSM) {
            return "GSM";
        } else if (networkType == NETWORK_TYPE_GPRS) {
            return "GPRS";
        } else if (networkType == NETWORK_TYPE_EDGE) {
            return "EDGE";
        } else if (networkType == NETWORK_TYPE_HSUPA) {
            return "HSUPA";
        } else if (networkType == NETWORK_TYPE_HSDPA) {
            return "HSDPA";
        } else if (networkType == NETWORK_TYPE_UMTS) {
            return "WCDMA";
        }
        return "";
    }

    //获取电池电量和充电状态
    private static int[] getBattery(Context context) {
        int battery = 0, chargeState = 0;
        Intent intent = context.registerReceiver(null,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (intent != null && intent.getExtras() != null) {
            int level = intent.getIntExtra("level", 0);
            //电量的总刻度
            int scale = intent.getIntExtra("scale", 100);
            battery = (level * 100) / scale;
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                    status == BatteryManager.BATTERY_STATUS_FULL;
            chargeState = isCharging ? 1 : 0;
        }
        return new int[]{battery, chargeState};
    }

    public static String getAppVersionName(Context context) {
        String appVersionName = "";
        try {
            PackageInfo packageInfo = context.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            appVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("", e.getMessage());
        }
        return appVersionName;
    }
}
