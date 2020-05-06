package com.chenay.platform.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.chenay.platform.ChyApplication;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.chenay.platform.ChyApplication.logger;

/**
 * @author Y.Chen5
 */
public class LocalAddressUtil {
    private static final String TAG = LocalAddressUtil.class.getName();

    //监听回调
    private LocationListener locationListener;
    private LocationManager locationManager;
    public static Location currentlocation;

    /**
     * 调用本地GPS来获取经纬度
     */
    public void getLocation() {

        //获取Location
        if (ActivityCompat.checkSelfPermission(ChyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ChyApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ToastUtil.showDefaultShortToast(ChyApplication.context, "需要开放位置权限");
            return;
        }

        String locationProvider = null;
        locationListener = new MyLocationListener();
        //获取地理位置管理器
        locationManager = (LocationManager) ChyApplication.context.getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
            Location location = locationManager.getLastKnownLocation(locationProvider);

            startRequestLocationUpdates(locationProvider, 0, 0);
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
            if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) &&
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                startRequestLocationUpdates(locationProvider, 0, 0);
            }
        } else {
            openLocalSetting(ChyApplication.context);
            Toast.makeText(ChyApplication.context, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }


    }

    private void openLocalSetting(Context context) {
        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    /**
     * 开始请求定位
     *
     * @param locationProvider 参数1，设备：有GPS_PROVIDER和NETWORK_PROVIDER两种，前者是GPS,后者是GPRS以及WIFI定位
     * @param minTime          位置信息更新周期.单位是毫秒
     * @param minDistance      位置变化最小距离：当位置距离变化超过此值时，将更新位置信息
     *                         备注：参数2和3，如果参数3不为0，则以参数3为准；参数3为0，则通过时间来定时更新；两者为0，则随时刷新
     */
    @SuppressLint("MissingPermission")
    private void startRequestLocationUpdates(String locationProvider, long minTime, float minDistance) {
        final Timer timer = new Timer("time");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                removeLocalListener();
                getCellIdentity();
                this.cancel();
                timer.cancel();
            }
        }, 30 * 1000);

        locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, locationListener);
    }

    private void removeLocalListener() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
        if (locationListener != null) {
            locationListener = null;
        }
    }


    public void getCellIdentity() { TelephonyManager mTelephonyManager = (TelephonyManager) ChyApplication.context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephonyManager == null) {
            return;
        }
        String operator = mTelephonyManager.getNetworkOperator();
        //获取的基站信息是
        if (operator == null || operator.length() < 5) {
            //获取基站信息有问题,可能是手机没插sim卡
            return;
        }
        int mcc = Integer.parseInt(operator.substring(0, 3));
        int mnc = Integer.parseInt(operator.substring(3));
        int lac;
        int cellId;
        if (ActivityCompat.checkSelfPermission(ChyApplication.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ChyApplication.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        CellLocation cellLocation = mTelephonyManager.getCellLocation();
        if (cellLocation == null) {
            //可能是手机没插sim卡之类的,返回获取基站失败
            return;
        }
        //因为移动联通电信基站的不同，所以需要区分基站类型
        //中国移动和中国联通获取LAC,CID的方式
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            //当前连接的是gsm基站
            GsmCellLocation location = (GsmCellLocation) cellLocation;
            lac = location.getLac();
            cellId = location.getCid();
            BaseStationInfo baseStationInfo = new BaseStationInfo();
            baseStationInfo.setMcc(mcc);
            baseStationInfo.setMnc(mnc);
            baseStationInfo.setLac(lac);
            baseStationInfo.setCid(cellId);
            baseStationInfo.setBaseType(0);
        }
        //中国电信获取LAC,CID的方式
        if (mTelephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            //当前连接是cdma基站
            CdmaCellLocation cdma = (CdmaCellLocation) mTelephonyManager.getCellLocation();
            BaseStationInfo baseStationInfo = new BaseStationInfo();
            baseStationInfo.setBaseType(1);
            baseStationInfo.setSid(cdma.getSystemId());
            baseStationInfo.setNid(cdma.getNetworkId());
            baseStationInfo.setBid(cdma.getBaseStationId());
        }
    }


    /**
     * 显示地理位置经度和纬度信息
     *
     * @param location
     */
    private void showLocation(Location location) {
        String locationStr = "纬度：" + location.getLatitude() + "\n"
                + "经度：" + location.getLongitude();
        logger.debug("", "showLocation:------------------------ " + "纬度：" + location.getLatitude() + "经度：" + location.getLongitude());
//        updateVersion(location.getLatitude() + "", location.getLongitude() + "");
        ToastUtil.showDefaultLongToast(ChyApplication.context, locationStr);
    }


//    public void updateVersion(String wd, String jd) {
//
//        String url="http://api.map.baidu.com/geocoder?output=json&location="+wd+","+jd+"&ak=esNPFDwwsXWtsQfw4NMNmur1";
//
//
//        OkhttpManager.getAsync(url, new OkhttpManager.DataCallBack() {
//            @Override
//            public void requestFailure(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void requestSuccess(String result) {
//                City city = JSON.parseObject(result, City.class);
//                if (city.getStatus().equals("OK")) {
//                    logger.debug("地址为", city.getResult().getAddressComponent().getCity());
//                    tv.setText(city.getResult().getFormatted_address());
//                }
//            }
//        });
////        OkHttpUtils
////                .get()
////                .url(path)
////                .build()
////                .execute(new StringCallback() {
////                    @Override
////                    public void onError(Call arg0, Exception arg1, int arg2) {
////                    }
////
////                    @Override
////                    public void onResponse(String arg0, int arg1) {
////                        Gson gson = new Gson();
////                        City response = gson.fromJson(arg0, City.class);
////                        CMlogger.debug(TAG, arg0);
////                        if (response.getStatus().equals("OK")) {
////                            CMlogger.debug("地址为", response.getResult().getAddressComponent().getCity());
////                        }
////                    }
////
////                });
//    }


    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {

            currentlocation = location;
            //位置信息变化时触发，一旦触发这个回调就证明GPS定位成功，这个方法是一直都会回调的，在一定的时间内，需要设置一个超时，以免耗电。
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    //当前GPS状态为可见状态
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    //当前GPS状态为服务区外状态
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    //当前GPS状态为暂停服务状态
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            //GPS开启时触发
        }

        @Override
        public void onProviderDisabled(String provider) {
            //GPS禁用时触发
        }
    }

}
