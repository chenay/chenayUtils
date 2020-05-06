package com.chenay.platform.device;

import java.io.ObjectStreamException;

/**
 * @author Y.Chen5
 */
public class DevicesInfos {
    private String appVersion;
    private String languageTag;
    private UserInfo userInfo;
    private BuildInfo buildInfo;
    private WifiInfo wifiInfo;
    private TelephonyInfo telephonyInfo;
    private NetworkInfo networkInfo;

    public String getLanguageTag() {
        return languageTag;
    }

    public void setLanguageTag(String languageTag) {
        this.languageTag = languageTag;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public BuildInfo getBuildInfo() {
        return buildInfo;
    }

    public void setBuildInfo(BuildInfo buildInfo) {
        this.buildInfo = buildInfo;
    }


    public WifiInfo getWifiInfo() {
        return wifiInfo;
    }

    public void setWifiInfo(WifiInfo wifiInfo) {
        this.wifiInfo = wifiInfo;
    }

    public TelephonyInfo getTelephonyInfo() {
        return telephonyInfo;
    }

    public void setTelephonyInfo(TelephonyInfo telephonyInfo) {
        this.telephonyInfo = telephonyInfo;
    }

    public NetworkInfo getNetworkInfo() {
        return networkInfo;
    }

    public void setNetworkInfo(NetworkInfo networkInfo) {
        this.networkInfo = networkInfo;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public static class UserInfo {
        private String userName;
        private String staffId;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getStaffId() {
            return staffId;
        }

        public void setStaffId(String staffId) {
            this.staffId = staffId;
        }
    }


    public static class BuildInfo {
        private String serial;
        private String brand;
        private String device;
        private String release;

        public String getSerial() {
            return serial;
        }

        public void setSerial(String serial) {
            this.serial = serial;
        }

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        public String getRelease() {
            return release;
        }

        public void setRelease(String release) {
            this.release = release;
        }
    }




    public static class WifiInfo {

        private String bssid;
        private String ssid;
        private String signal;

        public String getSignal() {
            return signal;
        }

        public void setSignal(String signal) {
            this.signal = signal;
        }

        public String getSsid() {
            return ssid;
        }

        public void setSsid(String ssid) {
            this.ssid = ssid;
        }

        public String getBssid() {
            return bssid;
        }

        public void setBssid(String bssid) {
            this.bssid = bssid;
        }

    }

    /**
     * 电话信息
     */
    public static class TelephonyInfo {

        /**
         * Returns the unique device ID, for example, the IMEI for GSM and the MEID
         * or ESN for CDMA phones. Return null if device ID is not available.
         */
        private String deviceId;

        /**
         * Returns the phone number string for line 1, for example, the MSISDN
         * for a GSM phone. Return null if it is unavailable.
         */
        private String phoneNumber;
        /**
         * Returns the serial number of the SIM, if applicable. Return null if it is
         * unavailable.
         */
        private String simSerial;
        /**
         * 供应商名称 "中国电信"
         */
        private String simOperatorName;
        /**
         * Returns the ISO country code equivalent for the SIM provider's country code.
         */
        private String simCountryIso;
        /**
         * NETWORK_TYPE_UNKNOWN; // = 0
         * NETWORK_TYPE_GPRS; // = 1.
         * NETWORK_TYPE_EDGE; // = 2.
         * NETWORK_TYPE_UMTS; // = 3.
         * NETWORK_TYPE_CDMA; // = 4.
         * NETWORK_TYPE_EVDO_0; // = 5.
         * NETWORK_TYPE_EVDO_A; // = 6.
         * NETWORK_TYPE_1XRTT; // = 7.
         * NETWORK_TYPE_HSDPA; // = 8.
         * NETWORK_TYPE_HSUPA; // = 9.
         * NETWORK_TYPE_HSPA; // = 10.
         * NETWORK_TYPE_IDEN; // = 11.
         * NETWORK_TYPE_EVDO_B; // = 12
         * NETWORK_TYPE_LTE; // = 13.
         * NETWORK_TYPE_EHRPD; // = 14.
         * NETWORK_TYPE_HSPAP; // = 15.
         * NETWORK_TYPE_GSM; // = 16.
         * Return the NETWORK_TYPE_xxxx for current data connection.
         */
        private String networkType;

        /**
         * Returns the ISO country code equivalent of the MCC (Mobile Country Code) of the current
         * registered operator, or nearby cell information if not registered.
         */
        private String networkCountryIso;
        /**
         * Returns the numeric name (MCC+MNC) of current registered operator.
         */
        private String networkOperator;
        /**
         * Returns the alphabetic name of current registered operator.
         * example:CHN-CT
         */
        private String networkOperatorName;

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getSimSerial() {
            return simSerial;
        }

        public void setSimSerial(String simSerial) {
            this.simSerial = simSerial;
        }

        public String getSimOperatorName() {
            return simOperatorName;
        }

        public void setSimOperatorName(String simOperatorName) {
            this.simOperatorName = simOperatorName;
        }

        public String getSimCountryIso() {
            return simCountryIso;
        }

        public void setSimCountryIso(String simCountryIso) {
            this.simCountryIso = simCountryIso;
        }

        public String getNetworkType() {
            return networkType;
        }

        public void setNetworkType(String networkType) {
            this.networkType = networkType;
        }

        public String getNetworkCountryIso() {
            return networkCountryIso;
        }

        public void setNetworkCountryIso(String networkCountryIso) {
            this.networkCountryIso = networkCountryIso;
        }

        public String getNetworkOperator() {
            return networkOperator;
        }

        public void setNetworkOperator(String networkOperator) {
            this.networkOperator = networkOperator;
        }

        public String getNetworkOperatorName() {
            return networkOperatorName;
        }

        public void setNetworkOperatorName(String networkOperatorName) {
            this.networkOperatorName = networkOperatorName;
        }
    }

    /**
     * 网络信息
     */
    public static class NetworkInfo {
        private String mTypeName;
        private String mSubtypeName;
        private String mState;
        private String mDetailedState;
        private String mReason;
        private String mExtraInfo;
        private boolean mIsAvailable;

        private String mac;
        private String ipAddress;

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getmTypeName() {
            return mTypeName;
        }

        public void setmTypeName(String mTypeName) {
            this.mTypeName = mTypeName;
        }

        public String getmSubtypeName() {
            return mSubtypeName;
        }

        public void setmSubtypeName(String mSubtypeName) {
            this.mSubtypeName = mSubtypeName;
        }

        public String getmState() {
            return mState;
        }

        public void setmState(String mState) {
            this.mState = mState;
        }

        public String getmDetailedState() {
            return mDetailedState;
        }

        public void setmDetailedState(String mDetailedState) {
            this.mDetailedState = mDetailedState;
        }

        public String getmReason() {
            return mReason;
        }

        public void setmReason(String mReason) {
            this.mReason = mReason;
        }

        public String getmExtraInfo() {
            return mExtraInfo;
        }

        public void setmExtraInfo(String mExtraInfo) {
            this.mExtraInfo = mExtraInfo;
        }



        public boolean ismIsAvailable() {
            return mIsAvailable;
        }

        public void setmIsAvailable(boolean mIsAvailable) {
            this.mIsAvailable = mIsAvailable;
        }

    }


    /**
     * 获取单例内部类句柄
     *
     * @return Created by Chenay
     */
    private static class DevicesBeanHolder {
        private static final DevicesInfos SINGLETON = new DevicesInfos();
    }

    /**
     * 获取单例
     *
     * @return Created by Chenay
     */
    public static DevicesInfos newInstance() {
        return DevicesBeanHolder.SINGLETON;
    }

    /**
     * 防止反序列化操作破坏单例模式 (条件) implements Serializable
     *
     * @return Created by Chenay
     */
    private Object readResolve() throws ObjectStreamException {
        return DevicesBeanHolder.SINGLETON;
    }
}
