//package com.inka.netsync.async;
//
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.support.v4.app.FragmentActivity;
//
//import com.inka.netsync.BaseConfigurationPerSite;
//import com.inka.netsync.R;
//import com.inka.netsync.common.utils.AndroidUtil;
//import com.inka.netsync.controler.ContentControler;
//import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
//import com.inka.netsync.logs.LogUtil;
//import com.inka.netsync.media.MediaStorage;
//import com.inka.netsync.model.ContentEntry;
//import com.inka.netsync.view.ProgressManagerClient;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//
///**
// * Created by birdgang on 2017. 5. 6..
// */
//
//public class SDPathUpdateAsyncTask extends AsyncTask<Void, String, Boolean> {
//
//    protected FragmentActivity mActivity = null;
//    private int convertedNum = 0;
//
//    public SDPathUpdateAsyncTask(FragmentActivity activity) {
//        this.mActivity = activity;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        ProgressManagerClient.getInstance().showProgress(mActivity, mActivity.getString(R.string.dialog_progress_please_wait));
//        super.onPreExecute();
//    }
//
//    @Override
//    protected Boolean doInBackground(Void... params) {
//        boolean result = false;
//        try {
//            ArrayList<ContentEntry> contentEntries = ContentControler.getDefault().loadContentList();
//            if (null == contentEntries || contentEntries.size() <= 0) {
//                LogUtil.INSTANCE.info("birdgangsdpathupdate", "null == contentEntries || contentEntries.size() < 0 ");
//                return result;
//            }
//
//            LogUtil.INSTANCE.info("birdgangsdpathupdate", "contentEntries size : " + contentEntries.size());
//
//            int size = contentEntries.size();
//
//            for (ContentEntry contentEntry : contentEntries) {
//                if (isCancelled()) {
//                    LogUtil.INSTANCE.info("birdgangsdpathupdate", "[contentpath update] stop converting!");
//                    break;
//                }
//
//                String filePath = contentEntry.getContentFilePath();
//
//                String sdOldRoot = "/" + BaseConfigurationPerSite.getInstance().getExternalSdPath() + "/";
//                LogUtil.INSTANCE.info("birdgangsdpathupdate",  "[contentpath update]" + "old sdRoot : " + sdOldRoot);
//
//                if (StringUtils.contains(filePath, sdOldRoot)) {
//                    String sdRoot = AndroidUtil.getExternalSdCardPath(mActivity, MediaStorage.INSTANCE.getMediaDirectories());
//                    LogUtil.INSTANCE.info("birdgangsdpathupdate",  "[contentpath update]" + "new sdRoot : " + sdRoot);
//                    contentEntry.setContentFilePath(StringUtils.replace(filePath, sdOldRoot, sdRoot));
//                    ContentControler.getDefault().updateContent(contentEntry);
//                }
//
//                LogUtil.INSTANCE.info("birdgangsdpathupdate",  "[contentpath update] filePath : " + filePath);
//                convertedNum++;
//                publishProgress(mActivity.getString(R.string.sd_business_logic_change_content_path) + " " + size +  " / " + convertedNum);
//            }
//
//            result = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return result;
//    }
//
//
//    @Override
//    protected void onPostExecute(Boolean result) {
//        if (result) {
//            LogUtil.INSTANCE.info("birdgangsdpathupdate", "[contentpath update] converting success!");
//            LogUtil.INSTANCE.info("birdgangsdpathupdate", "[contentpath update] converted file size is : " + convertedNum);
//            PreferencesCacheHelper.setPreferenceValue(mActivity.getString(R.string.PREF_KEY_IS_SDPATH_CONVERTED), "true");
//        } else {
//            LogUtil.INSTANCE.info("birdgangsdpathupdate", "[contentpath update] converting failure!");
//        }
//
//        ProgressManagerClient.getInstance().stopProgress(mActivity);
//        super.onPostExecute(result);
//    }
//
//
//    @Override
//    protected void onProgressUpdate(String... values) {
//        ProgressManagerClient.getInstance().setSize(15.0f);
//        ProgressManagerClient.getInstance().setBackgroundColor(Color.rgb(0, 64, 128));
//        ProgressManagerClient.getInstance().setTextColor(Color.WHITE);
//        ProgressManagerClient.getInstance().setText(values[0]);
//        super.onProgressUpdate(values);
//    }
//
//    @Override
//    protected void onCancelled() {
//        ProgressManagerClient.getInstance().stopProgress(mActivity);
//        super.onCancelled();
//    }
//
//}
