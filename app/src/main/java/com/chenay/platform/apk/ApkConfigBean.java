package com.chenay.platform.apk;

import androidx.annotation.Keep;

import java.util.List;

/**
 * @author Y.Chen5
 */
public class ApkConfigBean {
    /**
     * [{"outputType":{"type":"APK"},
     * "apkData":{"type":"MAIN","splits":[],"versionCode":1,"versionName":"1.0","enabled":true,"outputFile":"app-release.apk","fullName":"release","baseName":"release"},
     * "path":"app-release.apk","properties":{}}]
     * outputType : {"type":"APK"}
     * apkInfo : {"type":"MAIN","splits":[],"versionCode":3}
     * path : eFactory-debug-1.0.03.apk
     * properties : {"packageId":"com.tti.ychen5.efactory","split":"","minSdkVersion":"15"}
     */

    private OutputTypeBean outputType;
    private ApkInfoBean apkInfo;
    private ApkDataBean apkData;
    private String path;
    private PropertiesBean properties;

    public OutputTypeBean getOutputType() {
        return outputType;
    }

    public void setOutputType(OutputTypeBean outputType) {
        this.outputType = outputType;
    }

    public ApkInfoBean getApkInfo() {
        return apkInfo;
    }

    public void setApkInfo(ApkInfoBean apkInfo) {
        this.apkInfo = apkInfo;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public PropertiesBean getProperties() {
        return properties;
    }

    public void setProperties(PropertiesBean properties) {
        this.properties = properties;
    }

    @Keep
    public static class OutputTypeBean {
        /**
         * type : APK
         */

        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @Keep
    public static class ApkInfoBean {
        /**
         * type : MAIN
         * splits : []
         * versionCode : 3
         */

        private String type;
        private int versionCode;
        private String versionName;
        private List<?> splits;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public List<?> getSplits() {
            return splits;
        }

        public void setSplits(List<?> splits) {
            this.splits = splits;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }
    }

    public static class ApkDataBean{
        /**
         * type : MAIN
         * splits : []
         * versionCode : 1
         * versionName : 1.0
         * enabled : true
         * outputFile : app-release.apk
         * fullName : release
         * baseName : release
         */

        private String type;
        private int versionCode;
        private String versionName;
        private boolean enabled;
        private String outputFile;
        private String fullName;
        private String baseName;
        private List<?> splits;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getOutputFile() {
            return outputFile;
        }

        public void setOutputFile(String outputFile) {
            this.outputFile = outputFile;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getBaseName() {
            return baseName;
        }

        public void setBaseName(String baseName) {
            this.baseName = baseName;
        }

        public List<?> getSplits() {
            return splits;
        }

        public void setSplits(List<?> splits) {
            this.splits = splits;
        }
    }
    @Keep
    public static class PropertiesBean {
        /**
         * packageId : com.tti.ychen5.efactory
         * split :
         * minSdkVersion : 15
         */

        private String packageId;
        private String split;
        private String minSdkVersion;

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getSplit() {
            return split;
        }

        public void setSplit(String split) {
            this.split = split;
        }

        public String getMinSdkVersion() {
            return minSdkVersion;
        }

        public void setMinSdkVersion(String minSdkVersion) {
            this.minSdkVersion = minSdkVersion;
        }
    }
}
