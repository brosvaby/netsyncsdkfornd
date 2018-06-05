package com.inka.netsync.media;

import com.inka.ncg.nduniversal.ModuleConfig;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by birdgang on 2017. 4. 20..
 */
public class MediaItemFilter implements FileFilter {

    public final static HashSet<String> FOLDER_BLACKLIST;
    static {
        final String[] folder_blacklist = {
                "/alarms",
                "/notifications",
                "/ringtones",
                "/media/alarms",
                "/media/notifications",
                "/media/ringtones",
                "/media/audio/alarms",
                "/media/audio/notifications",
                "/media/audio/ringtones",
//                "/android/data",
                "/android/media",
        };

        FOLDER_BLACKLIST = new HashSet<String>();
        for (String item : folder_blacklist) {
            FOLDER_BLACKLIST.add(MediaStorage.INSTANCE.EXTERNAL_PUBLIC_DIRECTORY + item);
        }
    }

    public MediaItemFilter() {
    }

    @Override
    public boolean accept(File f) {
        boolean accepted = false;
        if (!f.isHidden()) {
            if (f.isDirectory() && !FOLDER_BLACKLIST.contains(f.getPath().toLowerCase(Locale.ENGLISH))) {
                accepted = true;
            } else {
                String fileName = f.getName().toLowerCase(Locale.ENGLISH);
                String filePath = f.getPath().toLowerCase(Locale.ENGLISH);

                if (ModuleConfig.ENABLE_SCAN_MEDIA_MP4 && !ModuleConfig.ENABLE_SCAN_MEDIA_MP4_ONLY) {
                    boolean isMedia = false;
                    int dotIndex = fileName.lastIndexOf(".");
                    if (dotIndex != -1) {
                        isMedia = Extensions.VIDEO.contains(fileName.substring(dotIndex));
                    }
                    boolean isNcg = StringUtils.contains(fileName, ".sd.ncg");
                    if (isMedia || isNcg) {
                        accepted = true;
                    }
                }
                else if (ModuleConfig.ENABLE_SCAN_MEDIA_MP4_ONLY) {
                    boolean isMedia = false;
                    int dotIndex = fileName.lastIndexOf(".");
                    if (dotIndex != -1) {
                        isMedia = Extensions.VIDEO.contains(fileName.substring(dotIndex));
                    }
                    if (isMedia) {
                        accepted = true;
                    }
                    LogUtil.INSTANCE.info("birdgangmediafilter",  "accept > ModuleConfig.ONLY_SCAN_MP4 > fileName : " + fileName + " , accepted : " + accepted + " , filePath : " + filePath);
                }
                else {
                    int dotIndex = fileName.lastIndexOf(".");
                    if (dotIndex <= -1) {
                        accepted = false;
                    }

                    boolean ncgAccepted = StringUtils.contains(fileName, ".sd.ncg");
                    boolean pdfAccepted = StringUtils.contains(fileName, ".sd.pdf");
                    if (ncgAccepted || pdfAccepted) {
                        accepted = true;
                    }
                    LogUtil.INSTANCE.info("birdgangmediafilter",  "accept > fileName : " + fileName + " , accepted : " + accepted + " , filePath : " + filePath);
                }
            }
        }
        return accepted;
    }


}