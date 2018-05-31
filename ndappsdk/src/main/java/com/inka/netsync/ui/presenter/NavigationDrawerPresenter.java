package com.inka.netsync.ui.presenter;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.inka.netsync.R;
import com.inka.netsync.common.bus.EventBus;
import com.inka.netsync.common.utils.FileUtil;
import com.inka.netsync.data.DataManager;
import com.inka.netsync.data.cache.pref.PreferencesCacheHelper;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.media.MediaStorage;
import com.inka.netsync.model.DrawerMenuEntry;
import com.inka.netsync.model.ListDrawerMenuEntry;
import com.inka.netsync.ui.mvppresenter.NavigationDrawerMvpPresenter;
import com.inka.netsync.ui.mvpview.NavigationDrawerMvpView;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by birdgang on 2017. 4. 21..
 */

public class NavigationDrawerPresenter<V extends NavigationDrawerMvpView> extends BasePresenter<V> implements NavigationDrawerMvpPresenter<V> {

    private final String TAG = "NavigationDrawerPresenter";

    final Pattern DIR_SEPARATOR = Pattern.compile("/");

    @Inject
    public NavigationDrawerPresenter(DataManager dataManager, CompositeDisposable compositeDisposable) {
        super(dataManager, compositeDisposable);
    }

    @Override
    public void onViewInitialized() {
    }

    @Override
    public boolean isFirstRun() {
        return PreferencesCacheHelper.getPreferenceValueForBol(PreferencesCacheHelper.HAS_RUN_INIT_FIRST, false);
    }

    @Override
    public void setFirstRun(boolean firstRun) {
        PreferencesCacheHelper.setPreferenceValueForBol(PreferencesCacheHelper.HAS_RUN_INIT_FIRST, firstRun);
    }

    @Override
    public List<DrawerMenuEntry> getListStorage() {
        ListDrawerMenuEntry drawerMenuEntries = new ListDrawerMenuEntry();
        List<String> pathFromFiles = MediaStorage.INSTANCE.getStoragePaths();
        List<String> volums = getStorageDirectories();
        int storage_count = 0;

        LogUtil.INSTANCE.info("birdgangdrawerstorage" , "pathFromFiles.size() : " + pathFromFiles.size() + " pathFromFiles : " + pathFromFiles.toString());
        LogUtil.INSTANCE.info("birdgangdrawerstorage" , "volums.size() : " + volums.size() + " volums : " + volums.toString());

        for (String file : volums) {
            File f = new File(file);
            LogUtil.INSTANCE.info("birdgangdrawerstorage" , "file : " + file);

            String name;
            String menuName = DrawerMenuEntry.TAG_INTERNAL_STORAGE;

            int icon1 = R.drawable.img_drawer_external_sd;

            if (StringUtils.contains("/storage/emulated/legacy", file) || StringUtils.contains("/storage/emulated/0", file) || StringUtils.contains("/storage/sdcard0", file)) {
                LogUtil.INSTANCE.info("birdgangdrawerstorage" , "emulated : " + file);
                name = getDataManager().getStringResource(R.string.internal_memory);
                icon1 = R.drawable.img_drawer_storage;
                menuName = DrawerMenuEntry.TAG_INTERNAL_STORAGE;
            }
            else if (StringUtils.contains("/storage/sdcard1", file) || StringUtils.contains("/storage/extSdCard", file)) {
                LogUtil.INSTANCE.info("birdgangdrawerstorage" , "sdcard1 : " + file);
                name = getDataManager().getStringResource(R.string.external_memory);
                icon1 = R.drawable.img_drawer_external_sd;
                menuName = DrawerMenuEntry.TAG_EXTERNAL_STORAGE;
            }
            else if (StringUtils.contains(file, "otg:/")) {
                LogUtil.INSTANCE.info("birdgangdrawerstorage" , "otg : " + file);
                name = "OTG";
                icon1 = R.drawable.img_drawer_usb;
                menuName = DrawerMenuEntry.TAG_OTG_STORAGE;

                List<String> exist = new ArrayList<>();
                exist.addAll(MediaStorage.INSTANCE.getExternalStoragePaths());
                exist.addAll(MediaStorage.INSTANCE.getInnerStoragePaths());

                LogUtil.INSTANCE.info("birdgangdrawerstorage" , "sdcard1 : " + file);

                for (String val : pathFromFiles) {
                    LogUtil.INSTANCE.info("birdgangdrawerstorage" , "val : " + val);

                    boolean contain = exist.contains(val);
                    if (!contain) {
                        LogUtil.INSTANCE.info("birdgangdrawerstorage" , "insert > pathFromFiles : " + val);
                        MediaStorage.INSTANCE.addUsbStoragePaths(val);
                    }
                }
            }
            else {
                MediaStorage.INSTANCE.addExternalStoragePath(file);
                name = getDataManager().getStringResource(R.string.external_memory);
                icon1 = R.drawable.img_drawer_external_sd;
                menuName = DrawerMenuEntry.TAG_EXTERNAL_STORAGE;

                List<String> exist = new ArrayList<>();
                exist.addAll(MediaStorage.INSTANCE.getExternalStoragePaths());
                exist.addAll(MediaStorage.INSTANCE.getInnerStoragePaths());

                LogUtil.INSTANCE.info("birdgangdrawerstorage" , "sdcard1 : " + file);

                for (String val : pathFromFiles) {
                    LogUtil.INSTANCE.info("birdgangdrawerstorage" , "val : " + val);

                    boolean contain = exist.contains(val);
                    if (!contain) {
                        LogUtil.INSTANCE.info("birdgangdrawerstorage" , "insert > pathFromFiles : " + val);
                        MediaStorage.INSTANCE.addUsbStoragePaths(val);
                    }
                }
            }

            if (!f.isDirectory() || f.canExecute()) {
                DrawerMenuEntry drawerMenuEntry = new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), menuName, name, icon1, DrawerMenuEntry.STATE_ENABLE);
                boolean hasDrawer = drawerMenuEntries.hasDrawerMenuName(drawerMenuEntry);
                if (hasDrawer) {
                    continue;
                }

                storage_count++;
                drawerMenuEntries.add(drawerMenuEntry);
            }

            LogUtil.INSTANCE.info(TAG, " updateDrawer , name : " + name + " , storage_count : " + storage_count + " , file : " + file);
        }
        return drawerMenuEntries.getItems();
    }

    @Override
    public List<DrawerMenuEntry> getListStorageForSD() {
        ListDrawerMenuEntry drawerMenuEntries = new ListDrawerMenuEntry();
        List<String> pathFromFiles = MediaStorage.INSTANCE.getStoragePaths();
        List<String> volums = getStorageDirectories();
        int storage_count = 0;

        LogUtil.INSTANCE.info("birdgangdrawerstorage" , "pathFromFiles.size() : " + pathFromFiles.size() + " pathFromFiles : " + pathFromFiles.toString());
        LogUtil.INSTANCE.info("birdgangdrawerstorage" , "volums.size() : " + volums.size() + " volums : " + volums.toString());

        for (String file : volums) {
            File f = new File(file);
            LogUtil.INSTANCE.info("birdgangdrawerstorage" , "file : " + file);

            String name;
            String menuName = DrawerMenuEntry.TAG_INTERNAL_STORAGE;

            int icon1 = R.drawable.img_drawer_external_sd;

            if (StringUtils.contains("/storage/emulated/legacy", file)
                    || StringUtils.contains("/storage/emulated/0", file)
                    || StringUtils.contains("/storage/sdcard0", file)
                    || StringUtils.contains(file, "otg:/")) {
                continue;
            }
            else if (StringUtils.contains("/storage/sdcard1", file) || StringUtils.contains("/storage/extSdCard", file)) {
                LogUtil.INSTANCE.info("birdgangdrawerstorage" , "sdcard1 : " + file);
                name = getDataManager().getStringResource(R.string.external_memory);
                icon1 = R.drawable.img_drawer_external_sd;
                menuName = DrawerMenuEntry.TAG_EXTERNAL_STORAGE;
            }
            else {
                MediaStorage.INSTANCE.addExternalStoragePath(file);
                name = getDataManager().getStringResource(R.string.external_memory);
                icon1 = R.drawable.img_drawer_external_sd;
                menuName = DrawerMenuEntry.TAG_EXTERNAL_STORAGE;
            }

            if (!f.isDirectory() || f.canExecute()) {
                DrawerMenuEntry drawerMenuEntry = new DrawerMenuEntry(DrawerMenuEntry.DrawerMenuType.ITEM.ordinal(), menuName, name, icon1, DrawerMenuEntry.STATE_ENABLE);
                boolean hasDrawer = drawerMenuEntries.hasDrawerMenuName(drawerMenuEntry);
                if (hasDrawer) {
                    continue;
                }

                storage_count++;
                drawerMenuEntries.add(drawerMenuEntry);
            }

            LogUtil.INSTANCE.info(TAG, " updateDrawer , name : " + name + " , storage_count : " + storage_count + " , file : " + file);
        }
        return drawerMenuEntries.getItems();
    }


    @Override
    public void setCurrentDrawerMenu(String selectedMenu, ArrayList<DrawerMenuEntry> drawerMenuEntries) {
        int selectedPosition = 0;

        for (int i = 0; i < drawerMenuEntries.size(); i++) {
            if (drawerMenuEntries.get(i).mTabTag.equals(selectedMenu) == true) {
                selectedPosition = i;
            }
        }

        getMvpView().lastSelectedDrawerPosition(selectedPosition);
    }

    @Override
    public void changeDrawerMenuState(String selectedMenu, ArrayList<DrawerMenuEntry> drawerMenuEntries) {
        int selectedPosition = 0;
        DrawerMenuEntry selectedDrawerMenu = null;
        
        for (int i=0; i<drawerMenuEntries.size(); i++) {
            DrawerMenuEntry drawerMenuEntry = drawerMenuEntries.get(i);
            if (selectedMenu == drawerMenuEntry.mTabTag) {
                selectedDrawerMenu = drawerMenuEntry;
                selectedPosition = i;
            }
        }

        if (null != selectedDrawerMenu) {
            int state = selectedDrawerMenu.getStateMenu();
            if (DrawerMenuEntry.STATE_DISABLE != state) {
                EventBus.getDefault().notifyEventSelectedMenu(selectedDrawerMenu.mTabTag);
                boolean hasRunInit = isFirstRun();
                if (!hasRunInit) {
                    setFirstRun(true);
                    for (int i=0; i<drawerMenuEntries.size(); i++) {
                        DrawerMenuEntry drawerMenuEntry = drawerMenuEntries.get(i);
                        drawerMenuEntry.mStateMenu = drawerMenuEntry.STATE_ENABLE;
                    }
                }
                LogUtil.INSTANCE.info("birdgangfirstrun", "DrawerMenuEntry.STATE_DISABLE != state");
            }

            LogUtil.INSTANCE.info(TAG, "ContentItemClickListener > selectedMenu : " + selectedMenu + " , selectedDrawerMenu.mTitle : " + selectedDrawerMenu.mTitle + " ,  selectedPosition : " + selectedPosition + " , state : " + state);
        }

        getMvpView().lastSelectedDrawerPosition(selectedPosition);
    }


    private List<String> getStorageDirectories() {
        Context context = getDataManager().getContext();
        // Final set of paths
        final ArrayList<String> storageDirectories = new ArrayList<>();
        // Primary physical SD-CARD (not emulated)
        final String rawExternalStorage = System.getenv("EXTERNAL_STORAGE");
        LogUtil.INSTANCE.info("birdgangdrawerstorage", "rawExternalStorage : " + rawExternalStorage);

        // All Secondary SD-CARDs (all exclude primary) separated by ":"
        final String rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE");
        LogUtil.INSTANCE.info("birdgangdrawerstorage", "rawSecondaryStoragesStr : " + rawSecondaryStoragesStr);

        // Primary emulated SD-CARD
        final String rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET");
        LogUtil.INSTANCE.info("birdgangdrawerstorage", "rawEmulatedStorageTarget : " + rawEmulatedStorageTarget);

        if (StringUtils.isBlank(rawEmulatedStorageTarget)) {
            if (StringUtils.isBlank(rawExternalStorage)) {
                storageDirectories.add("/storage/sdcard0");
            } else {
                storageDirectories.add(rawExternalStorage);
            }
        } else {
            // Device has emulated storage; external storage paths should have
            // userId burned into them.
            final String rawUserId;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
                rawUserId = "";
            } else {
                final String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                final String[] folders = DIR_SEPARATOR.split(path);
                final String lastFolder = folders[folders.length - 1];
                boolean isDigit = false;
                try {
                    Integer.valueOf(lastFolder);
                    isDigit = true;
                } catch (NumberFormatException ignored) {
                    LogUtil.INSTANCE.error(TAG, ignored);
                }
                rawUserId = isDigit ? lastFolder : "";
            }

            LogUtil.INSTANCE.info("birdgangdrawerstorage", "rawUserId : " + rawUserId);

            // /storage/emulated/0[1,2,...]
            if (StringUtils.isBlank(rawUserId)) {
                storageDirectories.add(rawEmulatedStorageTarget);
            } else {
                storageDirectories.add(rawEmulatedStorageTarget + File.separator + rawUserId);
            }
        }

        // Add all secondary storages
        if (StringUtils.isNotBlank(rawSecondaryStoragesStr)) {
            // All Secondary SD-CARDs splited into array
            final String[] rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator);
            Collections.addAll(storageDirectories, rawSecondaryStorages);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkExternalStoragePermission()) {
            storageDirectories.clear();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String strings[] = MediaStorage.INSTANCE.getExtSdCardPaths(context);
            for (String s : strings) {
                File f = new File(s);
                if (!storageDirectories.contains(s) && FileUtil.canListFiles(f)) {
                    storageDirectories.add(s);
                }
            }
        }

        File usb = MediaStorage.INSTANCE.getUsbDrive();
        if (null != usb) {
            LogUtil.INSTANCE.info("birdgangdrawerstorage", " usb.getName() : " + usb.getName());
            Toast.makeText(context, "null != usb " + usb.getPath(), Toast.LENGTH_LONG).show();
        }

        if (usb != null && !storageDirectories.contains(usb.getPath())) {
            storageDirectories.add(usb.getPath());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (MediaStorage.INSTANCE.isUsbDeviceConnected(context))  {
                storageDirectories.add("otg:/");
            }
        }

        LogUtil.INSTANCE.info(TAG, "rawEmulatedStorageTarget : " + rawEmulatedStorageTarget);

        return storageDirectories;
    }

}