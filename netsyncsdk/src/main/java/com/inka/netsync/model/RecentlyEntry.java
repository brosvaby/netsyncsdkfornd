package com.inka.netsync.model;

import com.inka.netsync.common.utils.DateTimeUtil;
import com.inka.netsync.data.cache.db.model.RecentlyCacheEntry;
import com.inka.netsync.lms.model.LmsEntry;
import com.inka.netsync.logs.LogUtil;
import com.inka.netsync.view.model.RecentlyViewEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class RecentlyEntry implements BaseEntry {

    private int recentlyId = -1;
    private int contentId = -1;
    private String contentPath;
    private String contentName;
    private String playDate = "";
    private LmsEntry lmsEntry;

    public RecentlyEntry () {
        lmsEntry = new LmsEntry();

    }

    public RecentlyEntry (ContentEntry contentEntry) {
        this();

        contentId = contentEntry.getContentId();
        contentName = contentEntry.getContentName();
        contentPath = contentEntry.getContentFilePath();
        playDate = contentEntry.getPlayDate();
    }

    public RecentlyEntry (RecentlyCacheEntry recentlyCacheEntry) {
        this();

        setRecentlyId(recentlyCacheEntry.getRecentlyId());
        setContentPath(recentlyCacheEntry.getContentPath());
        setContentName(recentlyCacheEntry.getContentName());
        setContentId(recentlyCacheEntry.getContentId());
        setPlayDate(recentlyCacheEntry.getPlayDate());
        setPlayDateToDate(recentlyCacheEntry.getPlayDateToDate());
    }

    public RecentlyCacheEntry convertCacheEntry () {
        RecentlyCacheEntry recentlyCacheEntry = new RecentlyCacheEntry();
        recentlyCacheEntry.setRecentlyId(recentlyId);
        recentlyCacheEntry.setContentPath(contentPath);
        recentlyCacheEntry.setContentName(contentName);
        recentlyCacheEntry.setContentId(contentId);
        recentlyCacheEntry.setPlayDate(playDate);
        recentlyCacheEntry.setPlayDateToDate(getPlayDateToDate());
        return recentlyCacheEntry;
    }

    public RecentlyViewEntry convertViewEntry () {
        RecentlyViewEntry recentlyViewEntry = new RecentlyViewEntry();
        recentlyViewEntry.setRecentlyId(recentlyId);
        recentlyViewEntry.setContentPath(contentPath);
        recentlyViewEntry.setContentName(contentName);
        recentlyViewEntry.setContentId(contentId);
        recentlyViewEntry.setPlayDate(playDate);
        recentlyViewEntry.setPlayDateToDate(getPlayDateToDate());
        recentlyViewEntry.setLmsRate(lmsEntry.getRate());
        return recentlyViewEntry;
    }


    public int getRecentlyId() {
        return recentlyId;
    }

    public void setRecentlyId(int recentlyId) {
        this.recentlyId = recentlyId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public String getContentPath() {
        return contentPath;
    }

    public void setContentPath(String contentPath) {
        this.contentPath = contentPath;
    }

    public String getPlayDate() {
        return playDate;
    }

    public void setPlayDate(String playDate) {
        this.playDate = playDate;
    }


    public Date getPlayDateToDate() {
        String from = playDate;
        SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();
        Date to = null;
        try {
            to = detailedDate.parse(from);
        } catch (ParseException e) {
            LogUtil.INSTANCE.error("error", e);
        }
        return to;
    }

    public void setPlayDateToDate(Date date){
        Date from = date;
        SimpleDateFormat detailedDate = DateTimeUtil.getSimpleDateFormat();
        playDate = detailedDate.format(from);
    }


    // 이름순
    public static Comparator<RecentlyEntry> PlayDateComparatorAsc = new Comparator<RecentlyEntry>() {
        @Override
        public int compare(RecentlyEntry lhs, RecentlyEntry rhs) {
            String lhsPlayDate = lhs.getPlayDate();
            String rhsPlayDate = rhs.getPlayDate();
            return lhsPlayDate.compareTo(rhsPlayDate);
        }
    };

    // 이름역순
    public static Comparator<RecentlyEntry> PlayDateComparatorDes = new Comparator<RecentlyEntry>() {
        @Override
        public int compare(RecentlyEntry lhs, RecentlyEntry rhs) {
            String rhsPlayDate = rhs.getPlayDate();
            String lhsPlayDate = lhs.getPlayDate();
            return rhsPlayDate.compareTo(lhsPlayDate);
        }
    };


}
