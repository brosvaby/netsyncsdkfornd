package com.inka.netsync.media;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.support.v4.content.ContextCompat;

import com.inka.netsync.collection.UniqueList;
import com.inka.netsync.common.utils.IoUtils;
import com.inka.netsync.common.utils.StringUtil;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Created by birdgang on 2018. 4. 25..
 */
public enum MediaStorage {
    INSTANCE;

    private final String TAG = "StroageUtil";

    public final String MOUNT = "mount";
    public final String EXTERNAL_STUNITAS_DIRECTORY_SYSTEM = "/system/st_contents";
    public final String EXTERNAL_STUNITAS_DIRECTORY_DATA = "/data/st_contents";
    public final String EXTERNAL_PUBLIC_DIRECTORY = Environment.getExternalStorageDirectory().getPath();

    public final String EXTERNAL_STORAGE_DIRECTORY = "/storage";
    public final String EXTERNAL_MOUNT_DIRECTORY = "/mnt";
    public final String USB_OTG = "otg:/";

    public int MemorySizeType = 0;
    public final int MEMORYTYPE_AVAILABLE = 0;
    public final int MEMORYTYPE_TOTAL = 1;

    public int CURRENT_TYPE = -1;
    public static final int ALL = 0;
    public static final int INTERNAL = 1;
    public static final int EXTERNAL = 2;
    public static final int USB = 3;

    public static final String TYPE_STORAGE = "TYPE_STORAGE";
    public static final String ROOT_INTERNAL = "ROOT_INTERNAL";
    public static final String ROOT_EXTERNAL = "ROOT_EXTERNAL";
    public static final String ROOT_USB = "ROOT_USB";

    public UniqueList storagePaths = new UniqueList();

    public UniqueList innerStoragePaths = new UniqueList();
    public UniqueList externalStoragePaths = new UniqueList();
    public UniqueList usbStoragePaths = new UniqueList();

    private MediaStorage () {
        innerStoragePaths.add("/storage/emulated");
        innerStoragePaths.add("/storage/emulated/legacy");
        innerStoragePaths.add("/storage/emulated/0");
        innerStoragePaths.add("/storage/sdcard0");
        innerStoragePaths.add(Environment.getExternalStorageDirectory().getPath());

        externalStoragePaths.add("/storage/sdcard1");
        externalStoragePaths.add("/storage/extSdCard");
    }


    public void setMediaStorageType (int type) {
        CURRENT_TYPE = type;
    }

    public int getMediaStorageType() {
        return CURRENT_TYPE;
    }

    public String[] getMediaDirectories() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(getStorageDirectories(ALL));
        return list.toArray(new String[list.size()]);
    }

    public String[] getMediaDirectoriesOfInternal() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(getStorageDirectories(INTERNAL));
        return list.toArray(new String[list.size()]);
    }

    public String[] getMediaDirectoriesOfExternal() {
        ArrayList<String> list = new ArrayList<String>();
        list.addAll(getStorageDirectories(EXTERNAL));
        return list.toArray(new String[list.size()]);
    }

    public ArrayList<String> getStorageDirectories(int storageType) {
        BufferedReader bufReader = null;
        ArrayList<String> list = new ArrayList<String>();
        LogUtil.INSTANCE.info("birdgangscanstorage" , "EXTERNAL_PUBLIC_DIRECTORY : " + EXTERNAL_PUBLIC_DIRECTORY + " , EXTERNAL_STUNITAS_DIRECTORY : " + EXTERNAL_STUNITAS_DIRECTORY_SYSTEM + " , EXTERNAL_STUNITAS_DIRECTORY_DATA : " + EXTERNAL_STUNITAS_DIRECTORY_DATA);

        if (storageType == ALL) {
            list.add(EXTERNAL_PUBLIC_DIRECTORY);
            list.add(EXTERNAL_STORAGE_DIRECTORY);
            list.add(EXTERNAL_MOUNT_DIRECTORY);
            list.add(USB_OTG);
        }

        List<String> typeWL = Arrays.asList("vfat", "exfat", "sdcardfs", "fuse", "ntfs", "fat32", "ext3", "ext4", "esdfs");
        List<String> typeBL = Arrays.asList("tmpfs");
        String[] mountWL = {"/mnt", "/Removable", "/storage"};
//        String[] mountWL = {"/Removable"};
        String[] mountBL = {
                "/mnt/secure",
                "/mnt/shell",
                "/mnt/asec",
                "/mnt/obb",
                "/mnt/media_rw/extSdCard",
                "/mnt/media_rw/sdcard",
                "/storage/emulated",
                "/mnt/runtime"};
        String[] deviceWL = {
                "/dev/block/vold",
                "/dev/fuse",
//                "/mnt/media_rw/extSdCard",
                "/mnt/media_rw" };

        try {
            bufReader = new BufferedReader(new FileReader("/proc/mounts"));
            String line;
            while ((line = bufReader.readLine()) != null) {
                StringTokenizer tokens = new StringTokenizer(line, " ");
                String device = tokens.nextToken();
                String mountpoint = tokens.nextToken();
                String type = tokens.nextToken();
                //LogUtil.INSTANCE.info("birdgangmediastorage" , "device : " + device + " , mountpoint : " + mountpoint + " , type : " + type);

                if (list.contains(mountpoint) || typeBL.contains(type) || StringUtil.startsWith(mountBL, mountpoint)) {
                    continue;
                }

                // check that device is in whitelist, and either type or mountpoint is in a whitelist
                if (StringUtil.startsWith(deviceWL, device) && (typeWL.contains(type) || StringUtil.startsWith(mountWL, mountpoint))) {
                    int position = StringUtil.containsName(list, StringUtil.getFileNameFromPath(mountpoint));
                    LogUtil.INSTANCE.info("birdgangmediastorage" , "device : " + device + " , mountpoint : " + mountpoint + " , type : " + type + " , position : " + position);
                    if (position > -1) {
                        list.remove(position);
                    }
                    list.add(mountpoint);
                }
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally {
            IoUtils.closeQuietly(bufReader);
        }
        return list;
    }


    public File getUsbDrive() {
        File parent = new File("/storage");

        try {
            for (File f : parent.listFiles()) {
                if (f.exists() && f.getName().toLowerCase().contains("usb") && f.canExecute()) {
                    return f;
                }
            }
        } catch (Exception e) {
        }
        parent = new File("/mnt/sdcard/usbStorage");
        if (parent.exists() && parent.canExecute()) {
            return (parent);
        }

        parent = new File("/mnt/sdcard/usb_storage");
        if (parent.exists() && parent.canExecute()) {
            return parent;
        }

        return null;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String[] getExtSdCardPaths(Context context) {
        List<String> paths = new ArrayList<String>();
        for (File file : context.getExternalFilesDirs("external")) {
            if (file != null) {
                int index = file.getAbsolutePath().lastIndexOf("/Android/data");
                if (index < 0) {
                    LogUtil.INSTANCE.debug(TAG, "Unexpected external file dir: " + file.getAbsolutePath());
                }
                else {
                    String path = file.getAbsolutePath().substring(0, index);
                    try {
                        path = new File(path).getCanonicalPath();
                    }
                    catch (IOException e) {
                        LogUtil.INSTANCE.error(TAG, e);
                    }
                    paths.add(path);
                }
            }
        }
        if (paths.isEmpty()) {
            paths.add("/storage/sdcard1");
        }
        return paths.toArray(new String[0]);
    }


    public boolean isUsbDeviceConnected(Context context) {
        UsbManager usbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
        if (usbManager.getDeviceList().size() != 0) {
//            Sp.edit().putString(KEY_PREF_OTG, VALUE_PREF_OTG_NULL).apply();
            return true;
        } else {
//            Sp.edit().putString(KEY_PREF_OTG, null).apply();
            return false;
        }
    }


    public String getSdcardPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }


    @SuppressLint("NewApi")
    public final String getStoragePath() throws Exception {
        String storagePath = "";

        String storageDirs[] = getMediaDirectories();
        LogUtil.INSTANCE.info("birdgangsdcard" , "storageDirs.length : " + storageDirs.length);

        for (String dir: storageDirs) {
            File f = new File(dir);
            if (f.exists()) {
                storagePath = f.getPath();
                LogUtil.INSTANCE.info("birdgangsdcard" , "storagePath : " + storagePath);
            }
        }
        return storagePath;
    }

    @SuppressLint("NewApi")
    public final String getExternalSDCardPath() throws Exception {
        String externalsdcardPath = "";

        String storageDirs[] = getMediaDirectories();
        LogUtil.INSTANCE.info("birdgangsdcard" , "storageDirs.length : " + storageDirs.length);

        if (storageDirs.length > 1 && storageDirs[1] != null) {
            File file = new File(storageDirs[1]);
            if (file.exists()) {
                externalsdcardPath = file.getPath();
                LogUtil.INSTANCE.info("birdgangsdcard" , "externalsdcardPath : " + externalsdcardPath);
            }
        }
        return externalsdcardPath;
    }


    public boolean hasExternalStorageSDCard(Context context) {
        boolean result = false;
        try {
            String externalPath = getExtSdcardPath(context);
            if (StringUtils.isNotBlank(externalPath)) {
                result = true;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return result;
    }


    @SuppressLint("NewApi")
    public final String getInternalPath() throws Exception {
        String internalPath = "";

        String storageDirs[] = getMediaDirectories();
        LogUtil.INSTANCE.info("birdgangsdcard" , "storageDirs.length : " + storageDirs.length);

        if (storageDirs.length > 0 && storageDirs[0] != null) {
            File file = new File(storageDirs[0]);
            if (file.exists()) {
                internalPath = file.getPath();
                LogUtil.INSTANCE.info("birdgangsdcard" , "internalPath : " + internalPath);
            }
        }
        return internalPath;
    }


    public boolean hasInternalStorage() {
        boolean result = false;
        try {
            String internalPath = getInternalPath();
            if (StringUtils.isNotBlank(internalPath)) {
                result = true;
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return result;
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public String getExtSdcardPath(Context context) {
        String extSdcard = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                String state = Environment.getExternalStorageState();

                File[] externalFiles = ContextCompat.getExternalFilesDirs(context, ""); // 외장

                LogUtil.INSTANCE.info("birdgangsdcard", "VERSION_CODES.KITKAT > externalFiles.length = " + externalFiles.length + " , state : " + state);

                if (externalFiles.length > 1 && externalFiles[1] != null) {
                    extSdcard = externalFiles[1].getAbsolutePath();

                    if (!StringUtils.isEmpty(extSdcard) && extSdcard.contains("/Android")) {
                        int indexed = extSdcard.indexOf("Android");
                        if (indexed > -1) {
                            extSdcard = extSdcard.substring(0, indexed);
                        }
                    }
                    // 추가
                    if (StringUtils.isBlank(extSdcard)) {
                        extSdcard = getInternalPath();
                    }
                }
                else {
                    extSdcard = getInternalPath();
                }
            } else {
                HashSet<String> mounts = getExternalMounts();

                for (String mount : mounts) {
                    if (mount.contains("extSdCard") || mount.contains("external_SD") || mount.contains("USBstorage")) {
                        extSdcard = mount;
                        break;
                    }
                }
            }

            LogUtil.INSTANCE.info("birdgangsdcard", "extSdcard = " + extSdcard);
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return extSdcard;
    }


    @SuppressLint("NewApi")
    public long getStorageSize(Context context, int storageType, int momorySizeType) {
        long result = 0;
        try {
            File file = null;
            String path = null;
            if (storageType == INTERNAL) {
                path = getInternalPath();
            } else if (storageType == EXTERNAL) {
                path = getExtSdcardPath(context);
            }
            if (StringUtils.isBlank(path)) {
                return -1;
            } else {
                file = new File(path);
            }
            result = checkStorageSize(file.getPath(), momorySizeType);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
        return result;
    }

    private long checkStorageSize (String path, int momorySizeType) throws Exception {
        if (momorySizeType == MEMORYTYPE_AVAILABLE) {
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else if (momorySizeType == MEMORYTYPE_TOTAL) {
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }


    private HashSet<String> getExternalMounts() {
        final HashSet<String> out = new HashSet<String>();

        String reg = "(?i).*vold.*(vfat|ntfs|exfat|fat32|ext3|ext4).*rw.*";
        String s = "";

        try {
            final Process process = new ProcessBuilder().command(MOUNT).redirectErrorStream(true).start();
            process.waitFor();

            final InputStream is = process.getInputStream();

            final byte[] buffer = new byte[1024];

            while (is.read(buffer) != -1) {
                s = s + new String(buffer);
            }
            is.close();
        } catch (final Exception e) {
            e.printStackTrace();
        }

        final String[] lines = s.split("\n");

        for (String line : lines) {
            if (!line.toLowerCase(Locale.US).contains("asec")){
                if (line.matches(reg)) {
                    String[] parts = line.split(" ");
                    for (String part : parts) {
                        if (part.startsWith("/"))
                            if (!part.toLowerCase(Locale.US).contains("vold")) {
                                out.add(part);
                            }
                    }
                }
            }
        }
        return out;
    }


    public List<String> getInnerStoragePaths() {
        return innerStoragePaths.getItems();
    }

    public void setInnerStoragePaths(List<String> innerStoragePaths) {
        this.innerStoragePaths.set(innerStoragePaths);
    }

    public List<String> getExternalStoragePaths() {
        return externalStoragePaths.getItems();
    }

    public void setExternalStoragePaths(List<String> externalStoragePaths) {
        this.externalStoragePaths.set(externalStoragePaths);
    }

    public void addExternalStoragePath (String path) {
        this.externalStoragePaths.add(path);
    }

    public List<String> getUsbStoragePaths() {
        return usbStoragePaths.getItems();
    }

    public void addUsbStoragePaths (String path) {
        this.usbStoragePaths.add(path);
    }

    public void setUsbStoragePaths(List<String> usbStoragePaths) {
        this.usbStoragePaths.set(usbStoragePaths);
    }


    public void addStorageForPath (String path) {
        if (StringUtils.isBlank(path)) {
            return;
        }

        try {
            int index = -1;
            boolean isStorage = StringUtils.startsWith(path, "/storage/");
            if (isStorage) {
                index = StringUtils.indexOf(path, "/", "/storage/".length());
                LogUtil.INSTANCE.info("birdgangcreatestorage" , "addStorageForPath > index : " + index + " , isStorage : " + " , path : " + path);
                if (index <= -1) {
                    return;
                }

                String result = StringUtils.substring(path, 0, index);
                storagePaths.add(result);
            }

        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
    }

    public List<String>  getStoragePaths() {
        return storagePaths.getItems();
    }
}