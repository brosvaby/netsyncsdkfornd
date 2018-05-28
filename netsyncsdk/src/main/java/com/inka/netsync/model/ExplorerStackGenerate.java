package com.inka.netsync.model;

import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.data.cache.db.model.ContentCacheEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Created by birdgang on 2017. 6. 22..
 */

public class ExplorerStackGenerate {

    private Map<String, ContentCacheEntry> contentCacheMaps = null;

    public ExplorerStackGenerate (Map<String, ContentCacheEntry> contentCacheMaps) {
        this.contentCacheMaps = contentCacheMaps;
    }

    private ContentCacheEntry findContentCacheEntryByPath (String path) {
        return contentCacheMaps.get(path);
    }

    /****
     * 하나의 파일에 대한 depth 생성
     * @param contentEntryMemoryAware
     * @return
     */
    public ExplorerStackEntry generateStack (ContentCacheEntry contentEntryMemoryAware) {
        if (null == contentEntryMemoryAware) {
            return null;
        }

        String filePath = contentEntryMemoryAware.getContentFilePath();

        if (StringUtils.isBlank(filePath)) {
            return null;
        }

        String prefixPath = filePath;
        LogUtil.INSTANCE.info("birdganggenerate", "filePath : " + filePath);

        ExplorerStackEntry explorerStackEntry = new ExplorerStackEntry();

        while (true) {
            ContentCacheEntry newContentEntry = new ContentCacheEntry();
            newContentEntry.copyContent(contentEntryMemoryAware);

            String name = StringUtil.extractContentName(prefixPath);
            String parentsFilePath = StringUtil.extractContentDirctoryFullPath(prefixPath);
            LogUtil.INSTANCE.info("birdganggenerate", "name : " + name + ", parentsFilePath : " + parentsFilePath + " , prefixPath : " + prefixPath);

            ContentCacheEntry contentCacheMapDiskAware = findContentCacheEntryByPath(filePath);
            if (null != contentCacheMapDiskAware) {
                LogUtil.INSTANCE.info("birdganggenerate", "exist > contentCacheEntriesDiskAware : " + contentCacheMapDiskAware.toString());
                newContentEntry.copyContent(contentCacheMapDiskAware);
            }

            newContentEntry.setParentsFilePath(parentsFilePath);
            newContentEntry.setContentName(name);
            newContentEntry.setContentFilePath(prefixPath);
            newContentEntry.setContentDownloadDate(contentEntryMemoryAware.getContentDownloadDate());

            if (StringUtils.equals(prefixPath, filePath)) {
                newContentEntry.setHasMore(false);
            } else {
                newContentEntry.setHasMore(true);
            }

            explorerStackEntry.push(newContentEntry);

            prefixPath = parentsFilePath;

            if (StringUtils.isBlank(name) || StringUtils.isBlank(prefixPath)) {
                break;
            }
        }
        //LogUtil.INSTANCE.info("birdgangstack", "contentStack size : " + contentStack.size() + " , rootPath : " + rootPath);

        return explorerStackEntry;
    }




}
