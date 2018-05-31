package com.inka.netsync.data.cache.db;

import android.provider.BaseColumns;

/**
 * Created by birdgang on 2015. 10. 9..
 */
public class MetaData {
    public final static String TAG = "MetaData";

    public static String DATABASE_NAME = "SmartNetsyncDB";
    public static int DB_VERSION = 17; // DB SCHEME (TABLE수정 액션이 있는상태로 앱을 업데이트시 반드시 DB_VERSION을 +1 한다
    public static int DB_OLD_VERSION = 12; //don't modify  by damian;

    public static final String SETTING_TABLE = "Setting";
    public static final String ENTERPRISE_TABLE = "Enterprise";
    public static final String SERIAL_TABLE = "Serial";
    public static final String TITLE_TABLE = "Title";
    public static final String CATEGORY_TABLE = "Category";
    public static final String CONTENT_TABLE = "Content";
    public static final String FAVORITE_TABLE = "favorite";
    public static final String RECENTLY_TABLE = "recently";
    public static final String BOOKMARK_TABLE = "Bookmark";

    public static final String CONTENT_TABLE_BACKUP = "ContentBackup";
    public static final String BOOKMARK_TABLE_BACKUP = "BookMarkBackup";

    public static final String LMS_TABLE = "lms";


    public static final class SettingColumns implements BaseColumns {
        public static final String SETTING_ID = "settingId";
        public static final String INKA_LOG = "inkaLog";
        public static final String NORMAL_TERMINATE = "normalTerminate";
        public static final String IS_DEVICE_REGISTER = "isDeviceRegister";
        public static final String USE_LOG = "useLog";
        public static final String LTE = "lte";
        public static final String DOWNLOAD_DIR_TYPE = "downloadDirType";
        public static final String PLAYER_TYPE = "playerType";
        public static final String SWX_PLAY = "swXPlay";
    }

    public static final class EnterpriseColumns implements BaseColumns {
        public static final String ENTERPRISE_ID = "enterpriseId";
        public static final String ENTERPRISE_CODE = "enterpriseCode";
        public static final String ENTERPRISE_NAME = "enterpriseName";
        public static final String ENTERPRISE_ICON_PATH = "enterpriseIconPath";
        public static final String USER_ID = "userID";
        public static final String PLAY_TYPE = "playType";
        public static final String SERVICE_MANAGER = "serviceManager";
        public static final String AES256KEY = "aes256Key";
        public static final String AES256IV = "aes256Iv";
        public static final String LOG = "log";
        public static final String LOG_TYPE = "logType";
        public static final String IS_REGISTERED = "isRegistered";
        public static final String REGISTER_DATE = "registerDate";
    }

    public static final class SerialColumns implements BaseColumns {
        public static final String SERIAL_ID = "serialId";
        public static final String SERIAL_NUMBER = "serialNumber";
        public static final String CID = "cid";
        public static final String PRODUCT_CODE = "productCode";
        public static final String PRODUCT_NAME = "productName";
        public static final String PRODUCT_ICON_PATH = "productIconPath";
    }

    public static final class TitleColumns implements BaseColumns {
        public static final String TITLE_ID = "titleId";
        public static final String ENTERPRISE_ID = "enterpriseId";
        public static final String TITLE_NAME = "titleName";
        public static final String TITLE_ICON_PATH = "titleIconPath";
        public static final String TITLE_DESCRIPTION = "titleDescription";
    }

    public static final class CategoryColumns implements BaseColumns {
        public static final String CATEGORY_ID = "categoryId";
        public static final String ENTERPRISE_ID = "enterpriseId";
        public static final String CATEGORY_NAME = "categoryName";
        public static final String CATEGORY_PERSON = "categoryPerson";
        public static final String CATEGORY_IMAGE = "categoryImage";
        public static final String CATEGORY_ASSORTMENT = "categoryAssortment";
        public static final String LAST_PLAY_DATE = "lastPlayDate";
        public static final String TITLE_ID = "titleId";
    }

    public static final class ContentColumns implements BaseColumns {
        public static final String CONTENT_ID = "contentId";
        public static final String CATEGORY_ID = "categoryId";
        public static final String CONTENT_DIRECTORY = "directory";         // 삭제
        public static final String PLAY_DATE = "playDate";
        public static final String CONTENT_DOWNLOAD_DATE = "contentDownloadDate";
        public static final String CONTENT_NAME = "contentName";
        public static final String CONTENT_FILE_PATH = "contentFilePath";
        public static final String CONTENT_URL = "contentURL"; // 삭제
        public static final String CONTENT_QNA = "contentQna"; // 삭제
        public static final String CONTENT_LAST_PLAY_TIME = "contentLastPlayTime";
        public static final String IS_FAVORITE_CONTENT = "isFavoriteContent";
        public static final String LMS_PERCENT = "lmsPercent"; // 삭제
        public static final String LMS_SECTION = "lmsSection"; // 삭제
        public static final String SEND_LMS_INFO = "sendLMSInfo"; // 삭제
        public static final String ORDER_ID = "orderID"; // 삭제
        public static final String INFO_I = "infoI"; // 삭제
        public static final String INFO_II = "infoII"; // 삭제
        public static final String INFO_III = "infoIII"; // 삭제
        public static final String INFO_IV = "infoIV"; // 삭제
        public static final String LICENSE_INFO = "licenseInfo";
        public static final String SERIAL = "serial";
        public static final String LMS_RAW_SECTION = "lmsRawSection"; // 삭제
        public static final String CONTENT_IMAGE = "contentImage"; // 삭제
    }


    public static final class FavoriteColumns implements BaseColumns {
        public static final String FAVORITE_ID = "favoriteId";
        public static final String CONTENT_ID = "contentId";
        public static final String CONTENT_NAME = "contentName";
        public static final String CONTENT_FILE_PATH = "contentFilePath";
    }


    public static final class RecentlyColumns implements BaseColumns {
        public static final String RECENTLY_ID = "recentlyId";
        public static final String CONTENT_ID = "contentId";
        public static final String CONTENT_FILE_PATH = "contentFilePath";
        public static final String CONTENT_NAME = "contentName";
        public static final String RECENTLY_DATE = "recentlyDate";
    }


    public static final class BookmarkColumns implements BaseColumns {
        public static final String BOOKMARK_ID = "bookmarkId";
        public static final String CONTENT_ID = "contentId";
        public static final String BOOKMARK_CONTENT_FILE_PATH = "contentFilePath";
        public static final String BOOKMARK_CONTENT_NAME = "contentName";
        public static final String BOOKMARK_DATE = "bookmarkDate";
        public static final String BOOKMARK_LOCATION = "bookmarkLocation";
        public static final String BOOKMARK_MEMO = "bookmarkMemo";
    }


    public static final class LMSColumns implements BaseColumns {
        public static final String LMS_ID = "lmsId";
        public static final String CONTENT_ID = "contentId";
        public static final String CONTENT_FILE_PATH = "contentFilePath";
        public static final String CONTENT_NAME = "contentName";
        public static final String SECTION = "section";
        public static final String RAW_SECTION = "rawSection";
        public static final String RATE = "rate";
    }

}
