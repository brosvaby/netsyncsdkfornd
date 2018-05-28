package com.inka.netsync.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by birdgang on 2017. 4. 14..
 */

public final class NetworkUtils {

    public static final int NETWORK_NONE = -1;
    public static final int NETWORK_WIFI = 0;
    public static final int NETWORK_DATA = 1;
    public static final int NETWORK_WIMAX = 2;

    private NetworkUtils() {
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


    public static int getNetworkConnectionType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null == activeNetwork) {
            return -1;
        }
        return activeNetwork.getType();
    }


    public boolean check3GAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiAvail = networkWifi.isAvailable();
        boolean isWifiConn = networkWifi.isConnected();

        NetworkInfo networkMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileAvail = false ;
        boolean isMobileConn = false;

        if (null != networkMobile) {              // 갤럭시 탭 10.1 wifi 버전 적용
            isMobileAvail = networkMobile.isAvailable();
            isMobileConn = networkMobile.isConnected();
        }

        if (!isWifiAvail && !isWifiConn && isMobileAvail && isMobileConn) {  // 와이파이 불가능에 3G는 가능하고 연결도 되어있으면 (3G 사용가능)
            return true;
        } else if(isWifiAvail && isWifiConn) { //와이파이일 경우
            return false;
        } else { // 와이파이는 확실히 안되는 경우
            return true;
        }
    }

    public boolean is3GConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkMobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileAvail = false;
        boolean isMobileConn = false;

        if (null != networkMobile) { 	// 갤럭시 탭 10.1 wifi 버전 적용
            isMobileAvail = networkMobile.isAvailable();
            isMobileConn = networkMobile.isConnected();
        }

        if (isMobileAvail && isMobileConn) { 	//3g일 경우
            return true;
        } else { // 3g는 확실히 안되는 경우
            return false;
        }
    }


    /**
     * 와이파이가 켜져있을경우 true
     * @return
     */
    public boolean isWifiConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiAvail = networkWifi.isAvailable();
        boolean isWifiConn = networkWifi.isConnected();

        if(isWifiAvail && isWifiConn) { //와이파이일 경우
            return true;
        } else { // 와이파이는 확실히 안되는 경우
            return false;
        }
    }

    /**
     * // 와이브로 지원 기기 일 경우 처리
     * @return
     */
    public boolean isNetworkWimaxAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService (Context.CONNECTIVITY_SERVICE);
        boolean isWimax = false;
        if (manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX) != null){
            isWimax = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX).isConnectedOrConnecting();
        }

        if(!isWimax){
            return false;
        }
        return true;
    }


    public boolean isNetworkAvailable(Context context) {
        if (check3GAvailable(context) && is3GConnection(context)) {
            return true;
        } else if (isWifiConnection(context)) {
            return true;
        } else if (isNetworkWimaxAvailable(context)) {
            return true;
        }
        return false;
    }



}