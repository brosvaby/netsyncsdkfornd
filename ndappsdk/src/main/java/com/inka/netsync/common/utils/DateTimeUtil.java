package com.inka.netsync.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by birdgang on 2017. 4. 18..
 */

public final class DateTimeUtil {

    public final static String DETAILED_DATE_FORMAT = "yyyyMMddHHmmss";
    public final static String SIMPLE_DATE_FORMAT 	= "yyyy-MM-dd";
    public final static String NCG_TIME_FORMAT 		= "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public final static String DATE_FROMAT_DIRECTORY = "yy.MM.dd";

    private static final long SIZE_KB = 1024;
    private static final long SIZE_MB = (1024 * SIZE_KB);
    private static final long SIZE_GB = (1024 * SIZE_MB);

    public static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(DETAILED_DATE_FORMAT);
    }

    public static SimpleDateFormat getDateFormatDirectory() {
        return new SimpleDateFormat(DATE_FROMAT_DIRECTORY);
    }

    /********************************************************************************
     * GMT 문자열 값을 LocalTime 문자열값으로 변경해 줍니다.
     * @param strGMT0	GMT 문자열 값
     * @param in_format	입력받은 GMT 문자열의 Date 포멧
     * @param out_format 결과를 얻고자 하는 Date 포멧
     * @return strGMT0의 Local 시간
     ********************************************************************************/
    public static String gmtToLocalTime( String strGMT0, String in_format, String out_format ) {
        DateFormat dateFormat = new SimpleDateFormat(in_format);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date gmt2local;
        try {
            gmt2local = dateFormat.parse( strGMT0 );
            return new SimpleDateFormat(out_format).format(gmt2local);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    /********************************************************************************
     * int 형 시간을 문자열로 바꿔 주는 함수 00:00:00
     * @param 	time 시간
     * @return 	String 00:00:00:00
     ********************************************************************************/
    public final static String changeTime(int time) {
        int nTemp 	= time / 1000;
        int nHour 	= nTemp / 3600; 	nTemp -= nHour * 3600;
        int nMin	= nTemp / 60;		nTemp -= nMin * 60;
        int nSec	= nTemp;

        String strTime = String.format("%02d:%02d:%02d", nHour, nMin, nSec );

        return strTime;
    }

}
