package com.inka.netsync;

public class BaseConfiguration {

    private String APPLICATION_CONTENT_CID = "";
    private int APPLICATION_DIALOG_BTN_COLOR = -1;

    private static volatile BaseConfiguration defaultInstance;

    public static BaseConfiguration getDefault() {
        if (defaultInstance == null) {
            synchronized (BaseConfiguration.class) {
                if (defaultInstance == null) {
                    defaultInstance = new BaseConfiguration();
                }
            }
        }
        return defaultInstance;
    }

    private String 	strCardManufacturer = "";

    private BaseConfiguration() { }

    public String getApplicationContentId () {
        return APPLICATION_CONTENT_CID;
    }

    public void setApplicationContentId (String contentId) {
        APPLICATION_CONTENT_CID = contentId;
    }

    public int getAppDialogBtnColor () {
        return APPLICATION_DIALOG_BTN_COLOR;
    }

    public void setAppDialogBtnColor (int color) {
        APPLICATION_DIALOG_BTN_COLOR = color;
    }

    public String getStrCardManufacturer() {
        return strCardManufacturer;
    }

    public void setStrCardManufacturer(String strCardManufacturer) {
        this.strCardManufacturer = strCardManufacturer;
    }
}
