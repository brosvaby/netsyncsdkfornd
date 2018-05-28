//package com.inka.netsync.async;
//
//import android.app.Activity;
//
//import com.inka.netsync.common.bus.BaseMessageListener;
//import com.inka.netsync.data.network.ApiServiceClientManager;
//import com.inka.netsync.data.network.model.BaseResponseEntry;
//import com.inka.netsync.data.network.model.ResponseSerialAuthEntry;
//import com.inka.netsync.data.network.model.SerialAuthEntry;
//import com.inka.netsync.logs.LogUtil;
//
//import org.apache.commons.lang3.StringUtils;
//
///**
// * 시리얼 인증 요청 to 서버
// * Created by birdgang on 2018. 4. 25..
// */
//public class RequestSerialAuthTask extends BaseAsyncTask {
//
//    private final String TAG = "RequestSerialAuthTask";
//
//    private SerialAuthEntry requestSerialAuthEntry;
//
//    public RequestSerialAuthTask(Activity context, SerialAuthEntry requestSerialAuthEntry, BaseMessageListener baseMessageListener, boolean hasProgress) {
//        super(context, baseMessageListener, hasProgress);
//        this.requestSerialAuthEntry = requestSerialAuthEntry;
//    }
//
//    @Override
//    protected BaseResponseEntry doInBackground(String... params) {
//        ResponseSerialAuthEntry responseSerialAuthEntry = null;
//
//        try {
//            String key = requestSerialAuthEntry.getKey();
//            String iv = requestSerialAuthEntry.getIv();
//            String code = requestSerialAuthEntry.getEnterpriseCode();
//            String serialNumber = requestSerialAuthEntry.getSerialNumber();
//            String contentId = requestSerialAuthEntry.getContentId();
//            String requestContentId = StringUtils.substring(contentId, 4);
//
//            LogUtil.INSTANCE.info("AppApiHelper", "RequestSerialAuthTask > requestSerialAuthEntry : " + requestSerialAuthEntry.toString());
//
//            ApiServiceClientManager apiServiceClientManager = new ApiServiceClientManager(context, requestSerialAuthEntry);
//            // Request 전송
//            responseSerialAuthEntry = apiServiceClientManager.requestSerialAuthentication(requestSerialAuthEntry);
//
//            LogUtil.INSTANCE.info("AppApiHelper", "RequestSerialAuthTask > result : " + responseSerialAuthEntry.toString());
//        } catch (Exception e) {
//            LogUtil.INSTANCE.error(TAG, e);
//        }
//        return responseSerialAuthEntry;
//    }
//
//}