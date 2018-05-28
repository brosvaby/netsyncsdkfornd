//package com.inka.netsync.async;
//
//import android.app.Activity;
//import android.support.v4.util.ArrayMap;
//
//import com.inka.netsync.common.bus.BaseMessageListener;
//import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
//import com.inka.netsync.data.network.model.BaseResponseEntry;
//import com.inka.netsync.logs.LogUtil;
//import com.inka.netsync.media.MediaStorage;
//import com.inka.netsync.model.ExplorerStackEntry;
//import com.inka.netsync.model.ExplorerStackGenerate;
//import com.inka.netsync.model.ListExplorerEntry;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//
//public class GenerateExplorerAsyncTask extends BaseAsyncTask {
//
//    private final String TAG = "GenerateExplorerAsyncTask";
//
//    private String rootType;
//    private List<ContentCacheEntry> contentCacheMemorAwareEntries = new ArrayList<>();
//    private Map<String, ContentCacheEntry> contentCacheDiskAwareMaps = new ArrayMap<>();
//
//    public GenerateExplorerAsyncTask(Activity context, BaseMessageListener messageListener, Map<String, List<ContentCacheEntry>>  contentCacheMemorAwareHashMap, Map<String, ContentCacheEntry> maps, String rootType, boolean hasProgress) {
//        super(context, messageListener, hasProgress);
//        this.contentCacheMemorAwareEntries.addAll(contentCacheMemorAwareHashMap.get(rootType));
//        this.contentCacheDiskAwareMaps = maps;
//        this.rootType = rootType;
//    }
//
//    private ArrayList<ContentCacheEntry> extractContentForStorage (String rootType) {
//        ArrayList<ContentCacheEntry> resultEntries = new ArrayList<>();
//
//        List<String> innerStoragePaths = MediaStorage.INSTANCE.getInnerStoragePaths();
//        List<String> externalStoragePaths = MediaStorage.INSTANCE.getExternalStoragePaths();
//        List<String> usbStoragePaths = MediaStorage.INSTANCE.getUsbStoragePaths();
//
//        LogUtil.INSTANCE.info(TAG, " innerStoragePaths : " + innerStoragePaths.toString());
//        LogUtil.INSTANCE.info(TAG, " externalStoragePaths : " + externalStoragePaths.toString());
//        LogUtil.INSTANCE.info(TAG, " usbStoragePaths : " + usbStoragePaths.toString());
//
//        for (ContentCacheEntry contentEntryMemorAwares : contentCacheMemorAwareEntries) {
//            String contentFilePath = contentEntryMemorAwares.getContentFilePath();
//
//            if (StringUtils.equals(MediaStorage.ROOT_INTERNAL, rootType)) {
//                for (String root : innerStoragePaths) {
//                    if (StringUtils.contains(contentFilePath, root)) {
//                        resultEntries.add(contentEntryMemorAwares);
//                        //LogUtil.INSTANCE.debug(TAG, "MediaStorage.INTERNAL > contentEntry.toString() : " + contentEntryMemorAwares.toString());
//                    }
//                }
//            }
//            else if (StringUtils.equals(MediaStorage.ROOT_EXTERNAL, rootType)) {
//                for (String root : externalStoragePaths) {
//                    if (StringUtils.contains(contentFilePath, root)) {
//                        resultEntries.add(contentEntryMemorAwares);
//                        //LogUtil.INSTANCE.debug(TAG, "MediaStorage.EXTERNAL > contentEntry.toString() : " + contentEntryMemorAwares.toString());
//                    }
//                }
//            }
//            else if (StringUtils.equals(MediaStorage.ROOT_USB, rootType)) {
//                for (String root : usbStoragePaths) {
//                    if (StringUtils.contains(contentFilePath, root)) {
//                        resultEntries.add(contentEntryMemorAwares);
//                        //LogUtil.INSTANCE.debug(TAG, "MediaStorage.USB > contentEntry.toString() : " + contentEntryMemorAwares.toString());
//                    }
//                }
//            }
//        }
//
//        LogUtil.INSTANCE.debug(TAG, " contentCacheMemorAwareEntries size : " + contentCacheMemorAwareEntries.size() + " , resultEntries size : " + resultEntries.size() + " , rootType : " + rootType);
//
//        return resultEntries;
//    }
//
//
//    @Override
//    protected BaseResponseEntry doInBackground(String... params) {
//        ListExplorerEntry listExplorerEntry = new ListExplorerEntry();
//        try {
//            ArrayList<ContentCacheEntry> contentCacheMemorAwareEntries = extractContentForStorage(rootType);
//            LogUtil.INSTANCE.debug(TAG, " contentCacheMemorAwareEntries : " + contentCacheMemorAwareEntries.size());
//
//            List<ExplorerStackEntry> explorerStackEntries = new ArrayList<>();
//            ExplorerStackGenerate explorerStackGenerate = new ExplorerStackGenerate(contentCacheDiskAwareMaps);
//
//            for (ContentCacheEntry contentEntryMemoryAwres : contentCacheMemorAwareEntries) {
//                ExplorerStackEntry explorerStackEntry = explorerStackGenerate.generateStack(contentEntryMemoryAwres);
//                explorerStackEntries.add(explorerStackEntry);
//            }
//
//            listExplorerEntry.setExplorerStackEntries(explorerStackEntries);
//        } catch (Exception e) {
//            LogUtil.INSTANCE.error(TAG, e);
//        }
//
//        return listExplorerEntry;
//    }
//
//
//    @Override
//    protected void onPostExecute(BaseResponseEntry result) {
//        super.onPostExecute(result);
////        List<String> innerStoragePaths = MediaStorage.INSTANCE.getInnerStoragePaths();
////        List<String> externalStoragePaths = MediaStorage.INSTANCE.getExternalStoragePaths();
////        List<String> usbStoragePaths = MediaStorage.INSTANCE.getUsbStoragePaths();
//    }
//
//
//}
