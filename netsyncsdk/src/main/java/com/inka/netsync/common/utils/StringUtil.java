package com.inka.netsync.common.utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by birdgang on 2017. 4. 20..
 */

public final class StringUtil {

    private static final String TAG = "StringUtil";

    public static final String EMPTY = StringUtils.EMPTY;

    public static boolean startsWith(String[] array, String text) {
        for (String item : array) {
            if (text.startsWith(item)) {
                return true;
            }
        }
        return false;
    }


    public static int containsName(List<String> array, String text) {
        for (int i = array.size()-1 ; i >= 0 ; --i) {
            if (array.get(i).endsWith(text)) {
                return i;
            }
        }
        return -1;
    }


    public static String getFileNameFromPath(String path) {
        if (path == null) {
            return "";
        }
        int index = path.lastIndexOf('/');
        if (index> -1) {
            return path.substring(index + 1);
        } else {
            return path;
        }
    }


    public static String getUriDataFromIntent(String url) {
        String uriData = null;
        try {
            if (StringUtils.equals(url, "null") || StringUtils.isBlank(url)) {
                return StringUtils.EMPTY;
            }

            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            Uri uri = intent.getData();
            if (null != uri) {
                uriData = uri.toString();
            }
        } catch (Exception e) {
        }
        return  uriData;
    }


    public static String getUriSchemeFromIntent(String url) {
        String uriScheme = null;
        try {
            if (StringUtils.equals(url, "null") || StringUtils.isBlank(url)) {
                return StringUtils.EMPTY;
            }

            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
            Uri uri = intent.getData();
            if (null != uri) {
                uriScheme = uri.getScheme();
            }
        } catch (Exception e) {
        }
        return  uriScheme;
    }


    /**
     * 파일의 전체 경로에서 파일명만을 추려 낸다.
     * @param filePath 전체 경로
     * @return 성공 시:파일 명, 실패 시:null
     */
    public static String getFilenameFromFilePath(String filePath) {
        filePath = filePath.replace( '\\', '/' );
        String[] token = filePath.split( "/" );
        if (token == null) {
            return null;
        }

        if (token.length < 1) {
            return filePath;
        }

        String ret = token[token.length - 1];
        return ret;
    }

    public final static String removeCharExceptNumber(String str) {
        return str.replaceAll("[^0-9]", "");
    }


    public final static String getEncodingFilePath (Context context, Uri uri) {
        String filepath = "";
        String uriPath  =  uri.toString();

        // Handle local file and remove url encoding
        if (uriPath.startsWith("file://")) {
            filepath = uriPath.replace("file://","");
            try {
                return URLDecoder.decode(filepath, "UTF-8");
            }
            catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        try {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.getCount() != 0) {
                int column_index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                filepath = cursor.getString(column_index);
            }
        }
        catch(Exception e) {
            Log.e("Path Error",e.toString());
        }
        return filepath;
    }

    public static String millisToString(long millis) {
        return millisToString(millis, false);
    }

    public static String millisToText(long millis) {
        return millisToString(millis, true);
    }

    static String millisToString(long millis, boolean text) {
        boolean negative = millis < 0;
        millis = Math.abs(millis);

        millis /= 1000;
        int sec = (int) (millis % 60);
        millis /= 60;
        int min = (int) (millis % 60);
        millis /= 60;
        int hours = (int) millis;

        String time;
        DecimalFormat format = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        format.applyPattern("00");
        if (text) {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + "h" + format.format(min) + "min";
            else if (min > 0)
                time = (negative ? "-" : "") + min + "min";
            else
                time = (negative ? "-" : "") + sec + "s";
        }
        else {
            if (millis > 0)
                time = (negative ? "-" : "") + hours + ":" + format.format(min) + ":" + format.format(sec);
            else
                time = (negative ? "-" : "") + min + ":" + format.format(sec);
        }
        return time;
    }


    public static int convertIntFromTokenizeredString (String value) {
        int version = 0;
        try {
            StringTokenizer stringTokenizer = new StringTokenizer(value,".");
            StringBuilder sb = new StringBuilder();
            while(stringTokenizer.hasMoreTokens()){
                sb.append(stringTokenizer.nextToken());
            }
            String str = sb.toString();
            if (StringUtils.isNotBlank(str)) {
                version = Integer.parseInt(str);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return version;
    }


    /********************************************************************************
     * 이미지 파일 url 로 부터 유니크한 local 파일 이름을 생성하여 리턴한다.
     ********************************************************************************/
    public final static String getFilenameFromFileUrl( String filePath ) {
        filePath = filePath.replace( "\\", "" );
        filePath = filePath.replace( "/", "" );
        filePath = filePath.replace( ":", "" );
        filePath = filePath.replace( "?", "" );
        filePath = filePath.replace( "&", "" );
        filePath = filePath.replace( "=", "" );
        filePath = filePath.replace( "\"", "" );
        filePath = filePath.replace( "'", "" );
        filePath = filePath.replace( "<", "" );
        filePath = filePath.replace( ">", "" );
        return filePath;
    }


    public final static String removeWrongCharacter(String srcString) {
        srcString = srcString.replace("'", "");
        srcString = srcString.replace("\"", "");
        srcString = srcString.replace("\\", "");
        srcString = srcString.replace("‘", "");
        srcString = srcString.replace("’", "");

        return srcString;
    }


    /**
     * 파일의 마지막 확장자를 얻습니다.<br>
     * mp4.ncg 이런식으로 끝나는 파일은 .ncg 를 돌려 줍니다.
     * @param fileStr 파일명 or 파일 전체 패스
     * @return 파일의 확장자
     */
    public static String getLastExtension(String fileStr) {
        String strExt = StringUtils.substring(fileStr, StringUtils.lastIndexOf(fileStr, ".") + 1, StringUtils.length(fileStr));
//        String strExt = fileStr.substring(fileStr.lastIndexOf(".")+1,fileStr.length());
        return strExt;
    }


    public static String extractMediaExtraPreNumber (String path) {
        return StringUtils.substring(path, 4);
    }

    public static String extractMediaExtraPreNumber (String path, int subStringCount) {
        return StringUtils.substring(path, subStringCount);
    }


    /**
     * 파일의 확장자를 제외한 파일이름을 얻습니다.<br>
     * sample.mp4.ncg 이런식으로 끝나는 파일은 sample 를 돌려 줍니다.
     * @param fileStr 파일명 or 파일 전체 패스
     * @return 파일의 확장자
     */
    public static String removeExtension(String fileStr) {
        String strFileName = fileStr.substring(0, fileStr.lastIndexOf("."));
        if (getLastExtension(fileStr).equals("ncg")) {
            strFileName = removeExtension(strFileName);
        }

        return strFileName;
    }


    public static String removeAllExtension(String fileStr) {
        int lastIndexOfComma = StringUtils.lastIndexOf(fileStr, ".");
        LogUtil.INSTANCE.info("birdgangutils" , "removeExtensionForPdf  > fileStr : " + fileStr + " , lastIndexOfComma : " + lastIndexOfComma);
        if (lastIndexOfComma < 0) {
            return fileStr;
        }

        String strFileName = StringUtils.substring(fileStr, 0, StringUtils.lastIndexOf(fileStr, "."));
        String lastExtension = getLastExtension(fileStr);
        LogUtil.INSTANCE.info("birdgangutils" , "removeAllExtension > lastExtension : " + lastExtension + " , fileStr : " + fileStr);
        if (StringUtils.equals(lastExtension, "ncg") || StringUtils.equals(lastExtension, "sd")) {
            strFileName = removeAllExtension(strFileName);
        }
        LogUtil.INSTANCE.info("birdgangutils" , "removeAllExtension > result  > strFileName : " + strFileName);
        return strFileName;
    }


    public static String removeExtensionForPdf(String fileStr) {
        int lastIndexOfComma = StringUtils.lastIndexOf(fileStr, ".");
        LogUtil.INSTANCE.info("birdgangutils" , "removeExtensionForPdf  > fileStr : " + fileStr + " , lastIndexOfComma : " + lastIndexOfComma);
        if (lastIndexOfComma < 0) {
            return fileStr;
        }

        String strFileName = StringUtils.substring(fileStr, 0, lastIndexOfComma);
        String lastExtension = getLastExtension(fileStr);
        if (StringUtils.equalsIgnoreCase(lastExtension, "sd") || StringUtils.equalsIgnoreCase(lastExtension, "pdf")) {
            LogUtil.INSTANCE.info("birdgangutils" , "removeExtensionForPdf  > process > strFileName : " + strFileName);
            strFileName = removeExtensionForPdf(strFileName);
        }
        LogUtil.INSTANCE.info("birdgangutils" , "removeExtensionForPdf  > result > strFileName : " + strFileName);
        return strFileName;
    }

    public static String extractMediaDirctory (String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        return StringUtils.substringAfterLast(StringUtils.substringBeforeLast(path, "/"), "/");
    }


    public static String extractMediaDirctoryFullPath (String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        return StringUtils.substringBeforeLast(path, "/");
    }

    public static String extractContentDirctoryFullPath (String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        return StringUtils.substringBeforeLast(path, "/");
    }

    public static String extractContentName (String path) {
        if (StringUtils.isBlank(path)) {
            return null;
        }
        return StringUtils.substringAfterLast(path, "/");
    }


    /***
     *
     * @param cs1
     * @param cs2
     * @return
     */
    public static boolean equals (CharSequence cs1, CharSequence cs2) {
        return cs1 == null ? cs2 == null : StringUtils.equals(cs1, cs2);
    }

    /**
     *
     * @param cs
     * @return
     */
    public static boolean isNotBlank (CharSequence cs) {
        return StringUtils.isNotBlank(cs);
    }




}
