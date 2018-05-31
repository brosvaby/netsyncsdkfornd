package com.inka.netsync.common.utils;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.inka.netsync.R;
import com.inka.netsync.logs.FileLogUtil;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Created by birdgang on 2017. 4. 20..
 */
public class AndroidUtil {

    private static final String TAG = "AndroidUtil";

    private static final long SIZE_KB = 1024;
    private static final long SIZE_MB = (1024 * SIZE_KB);
    private static final long SIZE_GB = (1024 * SIZE_MB);

    private static String DEVICE_MODEL = StringUtils.EMPTY;

    public final static String getDeviceModel() {
        if (StringUtils.isNotBlank(DEVICE_MODEL)) {
            return DEVICE_MODEL;
        }
        return Build.MODEL.replace(" ", "_");
    }

    private static List<String> readMountsFile() {
        List<String> mMounts = new ArrayList<String>();
        try {
            Scanner scanner = new Scanner(new File("/proc/mounts"));
            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.startsWith("/dev/block/vold/")) {
                    String[] lineElements = line.split("[ \t]+");
                    String element = lineElements[1];

                    mMounts.add(element);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mMounts;
    }

    // for EXTERNAL SD
    private static List<String> readVoldFile() {
        List<String> mVold = new ArrayList<String>();

        try {
            Scanner scanner = new Scanner(new File("/system/etc/vold.fstab"));

            while (scanner.hasNext()) {
                String line = scanner.nextLine();

                if (line.startsWith("dev_mount")) {
                    String[] lineElements = line.split("[ \t]+");
                    String element = lineElements[2];

                    if (element.contains(":")) {
                        element = element.substring(0, element.indexOf(":"));
                    }

                    mVold.add(element);
                }
            }
        } catch (Exception e) {
            // Auto-generated catch block
            e.printStackTrace();
        }

        return mVold;
    }

    @SuppressLint("NewApi")
    public final static boolean isInternalSDCardMemory(Context context) {
        String dir = "";

        // 화이트 리스트 관리 기기
        if (isWhiteList()) {
            dir = "";
            // 그 외 기기
        } else {
            if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                //킷캣 이상
                File[] strSDPath = context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
                if (strSDPath[0] == null) {
                    dir = "";
                } else {
                    dir = strSDPath[0].getAbsolutePath();
                }
            } else {
                //킷캣 미만
                dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        }

        if (dir.equals("") == true) {
            return false;
        }

        return true;
    }


    @SuppressLint("NewApi")
    public final static boolean isExternalSDCardMemory(Context context) {
        String dir = "";

        try {
            List<String> mMounts = readMountsFile();
            List<String> mVold = readVoldFile();

            // ETOOSPAD
            if(getDeviceModel().equals("ETOOSPAD") == true) {
                File file = null;
                file = new File("/mnt/external_sd/Android/data" + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS);
                file.mkdirs();

                if(file.exists() == false) {
                    dir = "";
                } else {
                    dir = file.getAbsolutePath();
                }

                // MPGEO (ST-PAD)
            } else if(getDeviceModel().equals("ST-PAD") == true) {
                File file = null;
                file = new File("/storage/sdcard1/Android/data" + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS);
                file.mkdirs();

                if(file.exists() == false) {
                    dir = "";
                } else {
                    dir = file.getAbsolutePath();
                }

                // MPGEO (HSK08v3)
            } else if(getDeviceModel().equals("HSK08v3") == true) {
                File file = null;
                file = new File("/storage/sdcard1/Android/data" + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS);
                file.mkdirs();

                if(file.exists() == false) {
                    dir = "";
                } else {
                    dir = file.getAbsolutePath();
                }

                // MPGEO
            } else if(getDeviceModel().equals("LEGEND-M") == true) {
                File file = null;
                file = new File("/mnt/sdcard2/Android/data" + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS);
                file.mkdirs();

                if(file.exists() == false) {
                    dir = "";
                } else {
                    dir = file.getAbsolutePath();
                }

                // Venue8 3840
            } else if(getDeviceModel().equals("Venue8_3840") == true) {
                File file = null;
                file = new File("/storage/sdcard1/Android/data" + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS);
                file.mkdirs();

                if(file.exists() == false) {
                    dir = "";
                } else {
                    dir = file.getAbsolutePath();
                }

                // 화이트 리스트 관리 기기
            } else if (isWhiteList() == true) {
                File file = null;
                //file = context.getExternalFilesDir(null);
                file = Environment.getExternalStorageDirectory();

                if(file == null) {
                    dir = "";
                } else {
                    dir = file.getAbsolutePath();
                }

                // 그 외 기기
            } else {
                if (Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                    // 킷캣 이상
                    File[] strSDPath = context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
                    if (strSDPath.length < 2 || strSDPath[1] == null) {
                        dir = "";
                    } else {
                        dir = strSDPath[1].getAbsolutePath();
                    }
                } else {
                    // 킷캣 미만
                    for (int i = 0; i < mMounts.size(); i++) {
                        String mount = mMounts.get(i);

                        if (!mVold.contains(mount)) {
                            mMounts.remove(i--);
                            continue;
                        }

                        File root = new File(mount);
                        if (!root.exists() || !root.isDirectory()) {
                            mMounts.remove(i--);
                            continue;
                        }

                        if (!isAvailableFileSystem(mount)) {
                            mMounts.remove(i--);
                            continue;
                        }

                        if (!checkMicroSDCard(mount)) {
                            mMounts.remove(i--);
                        }
                    }

                    if (mMounts.size() == 1) {
                        dir = mMounts.get(0);
                    }
                }
            }

            if (dir.equals("") == true) {
                // 실패시 수동 저장 위치 확인
//                String manualExternalSDPath = PreferenceUtil.getPreferenceValue(context.getString(R.string.PREF_KEY_MANUAL_EXTERNAL_SD), "");
                String manualExternalSDPath = "";
                File file = null;
                if( Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                    file = new File(manualExternalSDPath + "/" + "Android/data" + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS);
                } else {
                    file = new File(manualExternalSDPath + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS);
                }

                file.mkdirs();

                if(file.exists() == false) {
                    return false;
                } else {
                    return true;
                }
            }

            return true;

        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @SuppressLint("NewApi")
    public final static String getInternalSDCardMemory(Context context) throws Exception {
        String dir = "";

        // 화이트 리스트 관리 기기
        if (isWhiteList() == true) {
            dir = "";
            // 그 외 기기
        } else {
            if( Build.VERSION_CODES.KITKAT <= Build.VERSION.SDK_INT) {
                //킷캣 이상
                File[] strSDPath = context.getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS);
                if( strSDPath[0] == null) {
                    dir = "";
                } else {
                    dir = strSDPath[0].getAbsolutePath();
                }
            } else {
                //킷캣 미만
                dir = Environment.getExternalStorageDirectory().getAbsolutePath();
            }
        }

        if (dir.equals("") == true) {
            throw new Exception(context.getString(R.string.exception_cannot_use_internal_sdcard));
        }

        return dir;
    }


    @SuppressLint("NewApi")
    public final static boolean isAdminUser(Context context) throws Exception {
        long serialNumber = -1;
        UserHandle uh = android.os.Process.myUserHandle();
        UserManager um = (UserManager) context.getSystemService(Context.USER_SERVICE);

        if (um != null) {
            serialNumber = um.getSerialNumberForUser(uh);
            if (serialNumber == 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public final static boolean hasExternalSDCard (String storageDirs[]) throws Exception {
        boolean result = false;
        String path = getExternalSDCardMemory(storageDirs);
        if (StringUtils.isNotBlank(path)) {
            result = true;
        }
        return result;
    }

    public final static String getExternalSdCardPath(Context context, String storageDirs[]) throws Exception {
        String externalPath = getExternalSDCardMemory(storageDirs);
        String oriReplace = "Android/data" + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS;
        String deviceIdPath = externalPath.replace(oriReplace, "");

        LogUtil.INSTANCE.info("birdgangsdcard" , "getExternalSDRoot > externalPath : " + externalPath);
        LogUtil.INSTANCE.info("birdgangsdcard" , "getExternalSDRoot > oriReplace : " + oriReplace);
        LogUtil.INSTANCE.info("birdgangsdcard" , "getExternalSDRoot > deviceIdPath : " + deviceIdPath);
        return externalPath;
    }

//    public final static String getExternalSDRoot(Context context) throws Exception {
//        String externalPath = getExternalSDCardMemory(context);
//        String oriReplace = "Android/data" + "/" + context.getPackageName() + "/files/" + Environment.DIRECTORY_DOWNLOADS;
//        String deviceIdPath = externalPath.replace(oriReplace, "");
//
//        LogUtil.INSTANCE.info("birdgangsdcard" , "getExternalSDRoot > externalPath : " + externalPath);
//        LogUtil.INSTANCE.info("birdgangsdcard" , "getExternalSDRoot > oriReplace : " + oriReplace);
//        LogUtil.INSTANCE.info("birdgangsdcard" , "getExternalSDRoot > deviceIdPath : " + deviceIdPath);
//        return externalPath;
//    }



    // EXTERNAL SD
    @SuppressLint("NewApi")
    public final static String getExternalSDCardMemory(String storageDirs[]) throws Exception {
        String externalsdcardPath = "";
        for (String dir: storageDirs) {
            File f = new File(dir);
            if (f.exists()) {
                externalsdcardPath = f.getPath();
            }
        }
        LogUtil.INSTANCE.info("birdgangsdcard" , "Build.MODEL : " + Build.MODEL + " , getExternalSDCardMemory > externalsdcardPath : " + externalsdcardPath);

        return externalsdcardPath;
    }


    // for EXTERNAL SD
    private static boolean checkMicroSDCard(String fileSystemName) {
        StatFs statFs = new StatFs(fileSystemName);
        long totalSize = (long)statFs.getBlockSize() * statFs.getBlockCount();
        if (totalSize < (100 * SIZE_MB)) {
            return false;
        }
        return true;
    }


    // for EXTERNAL SD
    private static boolean isAvailableFileSystem(String fileSystemName) {
        final String[]  unAvailableFileSystemList = {"/dev", "/mnt/asec", "/mnt/obb", "/system", "/data", "/cache", "/efs", "/firmware"};   // 알려진 File System List입니다.

        for (String name : unAvailableFileSystemList) {
            if (fileSystemName.contains(name) == true) {
                return false;
            }
        }

        if (Environment.getExternalStorageDirectory().getAbsolutePath().equals(fileSystemName) == true) {
            /** 안드로이드에서 제공되는 getExternalStorageDirectory() 경로와 같은 경로일 경우에는 추가로 삽입된 SDCard가 아니라고 판단하였습니다. **/
            //sdcard가 삽입되어 있는 경우에는 안드로이드에서 제공하는 기본 경로와 sdcard가 같아도 true로 반환한다.
	    	/*if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
	    		return true;
	    	} else{
	    		return false;
	    	}*/
            return false;
        }

        return true;
    }

    // for Internal and External SD
    private static boolean isWhiteList() {
        String[] WhiteListDevice = {"LG-SU370", "LG-KU3700", "LG-LU3700", "LG-SU640", "LG-LU6200",
                "IM-A690", "IM-A730", "IM-A740", "SHW-M100", "SK-S150",
                "HTC_X515E", "SANSATION_Z710e", "LT15i", "ST18i"};

        String deviceModel = getDeviceModel();

        for (int i=0; i < WhiteListDevice.length; i++) {
            if (deviceModel.contains( WhiteListDevice[i])) {
                return true;
            }
        }

        return false;
    }


    public static boolean inKeyguardRestrictedInputMode (Context context) {
        KeyguardManager myKeyManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isKeyguardInputMode = myKeyManager.inKeyguardRestrictedInputMode();
        LogUtil.INSTANCE.info("birdgangandroidutil", "inKeyguardRestrictedInputMode > isKeyguardInputMode : " + isKeyguardInputMode);
        return isKeyguardInputMode;
    }


    /********************************************************************************
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     ********************************************************************************/
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }

    /*****
     *
     * @param context
     * @return
     * @throws UnsupportedEncodingException
     */
    public final static String getDeviceIDForPallyCondSD(Context context) throws UnsupportedEncodingException {
        return "PALLYCONSD" + getDeviceID(context);
    }


    /********************************************************************************
     * 단말기의 유니크한 ID 값을 UUID 형식의 문자열값으로 돌려 줍니다.
     * <br>유니크한 ID값을 얻는 순서 : 전화모듈ID -> Wifi모듈 ID -> ANDROID_ID -> 임의의 값으로 생성
     * @param context
     * @return 장치의 유니크한 문자열 값
     * @throws UnsupportedEncodingException
     ********************************************************************************/
    public final static String getDeviceID(Context context) throws UnsupportedEncodingException {
        String strDeviceID = "";
        FileLogUtil.INSTANCE.write("preference cache device id >> " + strDeviceID);

        if (strDeviceID != null && strDeviceID.equals("") != true) {
            return strDeviceID;
        }

        synchronized (AndroidUtil.class) {
            strDeviceID = getTelephonyDeviceID(context);
            FileLogUtil.INSTANCE.write("telephony device id >> " + strDeviceID);

            if (strDeviceID == null) {
                strDeviceID = getWifiDeviceID(context);
                FileLogUtil.INSTANCE.write("wifi device id >> " + strDeviceID);

                if (strDeviceID == null) {
                    strDeviceID = getAndroidID(context);
                    FileLogUtil.INSTANCE.write("android id >> " + strDeviceID);

                    if (strDeviceID == null) {
                        strDeviceID = UUID.randomUUID().toString();
                        FileLogUtil.INSTANCE.write("random uuid >> " + strDeviceID);
                    }
                }
            }

            if (strDeviceID != null) {
                return strDeviceID;

            } else {
                Toast.makeText(context, context.getString(R.string.exception_fail_to_get_device_id), Toast.LENGTH_SHORT).show();
                return "";
            }
        }
    }


    /********************************************************************************
     * 전화모듈의 장치 ID을 얻어서 UUID 형식을 문자열로 돌려 줍니다.
     * @param context
     * @return UUID 형식의 전화모듈의 장치 ID 문자열 값, 오류 발생 시 null 리턴됨
     * @throws UnsupportedEncodingException
     ********************************************************************************/
    public final static String getTelephonyDeviceID( Context context ) throws UnsupportedEncodingException {
        if (isInvalidTelephonyDeviceId(context) == true) {
            return null;
        }

        final String deviceId = ((TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE )).getDeviceId();
        if (deviceId != null ) {
            LogUtil.INSTANCE.info("birdgangandroidutil", "getTelephonyDeviceID" + " - " + "selected ! : " + deviceId);
            return UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")).toString();
        }

        return null;
    }

    /********************************************************************************
     * WIFI 모듈의 장치 ID값을 UUID 형식의 문자열로 돌려 줍니다.
     * @param context
     * @return UUID 형식의 WIFI모듈의 장치 ID 문자열 값, 오류 발생 시 null 리턴됨
     * @throws UnsupportedEncodingException
     ********************************************************************************/
    public final static String getWifiDeviceID( Context context ) throws UnsupportedEncodingException {
        String macAddress = "";

//        if (Build.VERSION.SDK_INT >= 23 || BaseConfiguration.getInstance().isPreBuilt() == true) {
        if (Build.VERSION.SDK_INT >= 23) {
            LogUtil.INSTANCE.info("birdgangandroidutil", " getWifiDeviceID" + " - " + "method type : " 	+ "new wifi method");

            try {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0"))
                        continue;

                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return null;
                    }

                    StringBuilder strMacAddress = new StringBuilder();
                    for (byte b : macBytes) {
                        strMacAddress.append(String.format("%02x", b) + ":");
                    }

                    if (strMacAddress.length() > 0) {
                        strMacAddress.deleteCharAt(strMacAddress.length() - 1);
                    }

                    // for log
                    macAddress = strMacAddress.toString();

                    try {
                        strMacAddress.deleteCharAt(2);
                        strMacAddress.deleteCharAt(4);
                        strMacAddress.deleteCharAt(6);
                        strMacAddress.deleteCharAt(8);
                        strMacAddress.deleteCharAt(10);
                    } catch( Exception e ) {
                        e.printStackTrace();
                    }

                    return UUID.nameUUIDFromBytes(strMacAddress.toString().getBytes("utf8")).toString();
                }

            } catch(SocketException e) {
                e.printStackTrace();
            }

            return null;

        } else {
            WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            if (wifiMan != null) {
                WifiInfo wifiInf = wifiMan.getConnectionInfo();
                StringBuilder strMacAddress	= new StringBuilder();
                strMacAddress.append(wifiInf.getMacAddress());

                // for log
                macAddress = strMacAddress.toString();

                try {
                    strMacAddress.deleteCharAt(2);
                    strMacAddress.deleteCharAt(4);
                    strMacAddress.deleteCharAt(6);
                    strMacAddress.deleteCharAt(8);
                    strMacAddress.deleteCharAt(10);
                } catch( Exception e ) {
                    e.printStackTrace();
                }

                return UUID.nameUUIDFromBytes(strMacAddress.toString().getBytes("utf8")).toString();
            }

            return null;
        }
    }

    /********************************************************************************
     * Android에서 재공하는 ANDROID_ID 값을 UUID 형식의 문자열로 변환하여 돌려 줍니다.
     * @param context
     * @return ANDROID_ID값의 UUID 형식의 문자열, 오류 발생 시 null
     * @throws UnsupportedEncodingException
     ********************************************************************************/
    public final static String getAndroidID( Context context ) throws UnsupportedEncodingException {
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (androidId == null) {
            return null;
        }

        if (!StringUtils.equals("9774d56d682e549c", androidId)) {
            return UUID.nameUUIDFromBytes(androidId.getBytes("utf8")).toString();
        }

        return null;
    }

    /********************************************************************************
     * TelephonyDeviceId 가 유효한지 판단 합니다.
     * @param context
     * @return 유효하지 않음 true, 유효함 false
     ********************************************************************************/
    public final static boolean isInvalidTelephonyDeviceId(Context context) {
        TelephonyManager telephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (null == telephonyMgr) {
            return false;
        }

        String telephonyID = telephonyMgr.getDeviceId();
        if (hasTelephony(context) == false && telephonyID != null && telephonyID.length() > 0 ) {
            return true;
        }

        return false;
    }

    /********************************************************************************
     * 전화 기능이 있는지 확인 합니다.
     * @param context
     * @return 있음 true, 없음 false
     ********************************************************************************/
    private final static boolean hasTelephony(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm == null) {
            return false;
        }

        PackageManager pm = context.getPackageManager();
        if (pm == null) {
            return false;
        }

        int phoneType = tm.getPhoneType();
        if( phoneType != TelephonyManager.PHONE_TYPE_NONE && pm.hasSystemFeature("android.hardware.telephony")) {
            return true;
        }

        return false;
    }


    /***
     *
     * @param context
     * @return
     */
    private static boolean checkAirPlaneMode(Context context) throws Exception {
        Boolean isAirplaneMode;
        if (isJellyBeanPrior()) {
            isAirplaneMode = Settings.System.getInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0) == 1;
        } else {
            isAirplaneMode = Settings.Global.getInt(context.getContentResolver(), Settings.Global.AIRPLANE_MODE_ON, 0) == 1;
        }
        return isAirplaneMode;
    }

    public static void changeAirplaneMode (Context context, boolean force) {
        if (!force) {
            return;
        }

        try {
            boolean check = checkAirPlaneMode(context);
            LogUtil.INSTANCE.info("birdgangsetting" , "changeAirplaneMode > check : " + check + " , isJellyBeanPrior() : " + isJellyBeanPrior());

            if (isJellyBeanPrior()) {
                check = !check;
                Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
                intent.putExtra("state", !check);
                context.sendBroadcast(intent);
            } else {
                Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }


    public static boolean isFroyoOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean isGingerbreadOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean isHoneycombOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isHoneycombMr1OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean isICSOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isJellyBeanOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isJellyBeanPrior() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isJellyBeanMR1OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isJellyBeanMR2OrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isKitKatOrPrior() {
        return Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isKitKatOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean isLolliPopOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean isMarshMallowOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean isNougatOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

}
