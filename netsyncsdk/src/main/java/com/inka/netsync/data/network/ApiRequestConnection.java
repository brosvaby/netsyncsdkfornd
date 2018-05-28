package com.inka.netsync.data.network;

import android.content.Context;

import com.inka.netsync.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by birdgang on 2017. 5. 2..
 */

public class ApiRequestConnection {

    private static volatile ApiRequestConnection defaultInstance;

    public static ApiRequestConnection getDefault() {
        if (defaultInstance == null) {
            synchronized (ApiRequestConnection.class) {
                if (defaultInstance == null) {
                    defaultInstance = new ApiRequestConnection();
                }
            }
        }
        return defaultInstance;
    }


    public String sendRequestByGetMethod (Context context, String strUrl, String encryptedParam) throws Exception {
        String strTemp 			= "";
        String responseData 	= "";

        try {
            // URL 생성
            //String safeUrl = SmartNetsyncUtil.safeUrlEncoder(strUrl);
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 설정
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            // send
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(os);
            ow.write(encryptedParam);
            ow.flush();
            ow.close();

            // read
            InputStreamReader ir = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            while((strTemp = br.readLine()) != null) {
                responseData += strTemp;
            }
            br.close();
            ir.close();

            responseData = responseData.trim();

            return responseData;

        } catch (Exception e) {
            throw new Exception(context.getString(R.string.exception_network_unknown_reason) + "\n" + e.getMessage());
            //Toast.makeText(context, context.getString(R.string.exception_network_unknown_reason), Toast.LENGTH_LONG).show();
            //throw e;
        }
    }

    // UrlConnection (Post)방식으로 통신
    public String sendRequestByPostMethod (Context context, String strUrl, String encryptedParam) throws Exception {
        String strTemp 			= "";
        String responseData 	= "";

        try {
            // URL 생성
            //String safeUrl = SmartNetsyncUtil.safeUrlEncoder(strUrl);
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            // 설정
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            // send
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter ow = new OutputStreamWriter(os);
            ow.write(encryptedParam);
            ow.flush();
            ow.close();

            // read
            InputStreamReader ir = new InputStreamReader(urlConnection.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            while((strTemp = br.readLine()) != null) {
                responseData += strTemp;
            }
            br.close();
            ir.close();

            responseData = responseData.trim();
        } catch (Exception e) {
            throw new Exception(context.getString(R.string.exception_network_unknown_reason) + "\n" + e.getMessage());
        }

        return responseData;
    }

}
