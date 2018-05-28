package com.inka.netsync.lms;

import android.content.Context;

import com.inka.netsync.data.cache.db.dao.LMSDao;
import com.inka.netsync.data.cache.db.model.LMSCacheEntry;
import com.inka.netsync.lms.model.LmsEntry;
import com.inka.netsync.logs.LogUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class LmsControler {

    private final String TAG = "LmsControler";

    private Context context;

    private class LMSData {
        int startTime = -1;
        int endTime = -1;
    }

    private static volatile LmsControler defaultInstance;

    public static LmsControler getDefault() {
        if (defaultInstance == null) {
            synchronized (LmsControler.class) {
                if (defaultInstance == null) {
                    defaultInstance = new LmsControler();
                }
            }
        }
        return defaultInstance;
    }

    private int mBeforeTime = 0;
    private int mIndex = 0;
    private LMSData lmsData = null;
    private LmsEntry lmsEntry;
    private ArrayList<LMSData> mArrLMSData = null;

    /***
     *
     * @param section
     * @param rawSection
     * @param duration
     */
    public void init(String section, String rawSection, int duration, String path) {
        mBeforeTime = 0;
        mIndex = 0;
        lmsEntry = new LmsEntry(section, rawSection, duration);
        lmsData = new LMSData();
        mArrLMSData = new ArrayList<LMSData>();
        LogUtil.INSTANCE.info("birdgangloadlms" , "init > section: " + section + " , rawSection : " + rawSection + " , duration : " + duration + " , path : " + path);
    }


    /**
     * LMS RawSection을 계산
     * @param second	현재 seek하고 있는 시간(초)
     */
    public void setLMSSection(int second) {
        // 전체시간과 현재시간이 1초 이내일 경우
        int duration = lmsEntry.getDuration();
        if (duration - second <= 1) {
            second = duration;
        }

        // 최초 등록
        if (mArrLMSData.size() == 0) {
            mBeforeTime = lmsData.endTime = lmsData.startTime = second;
            mArrLMSData.add(mIndex, lmsData);

            // 하나라도 데이터가 있을 경우
        } else {
            // 2초 이내일 경우 기존 데이터 갱신
            if (Math.abs(second - mBeforeTime) <= 2) {
                mBeforeTime = lmsData.endTime = second;
                mArrLMSData.set(mIndex, lmsData);

                // 기존 데이터와 2초 초과 차이가 날 경우
            } else {
                // 기존 데이터 갱신
                int startTime = mArrLMSData.get(mIndex).startTime;
                int endTime = mArrLMSData.get(mIndex).endTime;
                LogUtil.INSTANCE.info("birdganglms" , "setLMSSection > startTime : " + startTime + " , endTime : " + endTime);

                if (Math.abs(endTime - startTime) <= 2) { // 2초 이상 시청을 하지 않았다면. 갱신.
                    mBeforeTime = lmsData.endTime = lmsData.startTime = second;
                    mArrLMSData.set(mIndex, lmsData);
                    // 신규 데이터 생성
                } else {
                    mIndex++;
                    lmsData = new LMSData();
                    mBeforeTime = lmsData.endTime = lmsData.startTime = second;
                    mArrLMSData.add(mIndex, lmsData);
                }
            }
        }

        // TODO : 기존 데이터가 비정상적인지를 파악하고, 비정상적일 경우 제거 - 이걸 언제 해줘야할까?
        if (mIndex > 0 && (mArrLMSData.get(mIndex-1).startTime >= mArrLMSData.get(mIndex-1).endTime)) {
            mArrLMSData.remove(mIndex-1);
            mIndex--;
        }

    }

    /**
     * LMS RawSection을 String 형태로 반환
     * @return		RawSection
     */
    public String getRawSection() {
        String strRawSection = "";
        if (StringUtils.isNotBlank(lmsEntry.getRawSection())) {
            strRawSection = lmsEntry.getRawSection() + ",";
        }

        // "StartTime:EndTime,"의 형태로 구성
        for (int i = 0; i < mArrLMSData.size(); i++) {
            LMSData lmsData = mArrLMSData.get(i);
            int startTime = lmsData.startTime;
            int endTime = lmsData.endTime;
            if (endTime > startTime) {
                strRawSection += String.valueOf(startTime) + ":" + String.valueOf(endTime) + ",";
                LogUtil.INSTANCE.info("birdgangloadlms" , "getRawSection > endTime > startTime > startTime : " + startTime + " , endTime : " + endTime);
            } else {
                LogUtil.INSTANCE.info("birdgangloadlms" , "getRawSection > else > startTime : " + startTime + " , endTime : " + endTime);
            }
        }

        // 마지막이 ","로 끝날 경우 제거
        if (StringUtils.endsWith(strRawSection, ",")) {
            strRawSection = StringUtils.substring(strRawSection, 0, StringUtils.length(strRawSection) - 1);
        }
        LogUtil.INSTANCE.info("birdgangloadlms" , "getRawSection > strRawSection : " + strRawSection);
        return strRawSection;
    }

    /**
     * LMS ProcessedSection을 String 형태로 반환 (기존과 동일)
     * @return		ProcessedSection
     */
    public String getProcessedSection() {
        String strProcessedSection = "";
        strProcessedSection = sortSection(getRawSection() + "," + lmsEntry.getSection());
        return strProcessedSection;
    }

    /**
     * 스킵 등으로 재생안된 구간 중에서 제일 앞 지점을 초단위로 리턴
     * @return second
     */
    public int getResumePoint() {
        String[] arSection = null;
        String[] arSecond = null;

        try {
            String section = lmsEntry.getSection();
            if (StringUtils.contains(section , ",")) {
                arSection = StringUtils.split(section, ",");
                if (StringUtils.contains(arSection[0], ":")) {
                    arSecond = arSection[0].split(":");
                    return Integer.valueOf(arSecond[1]);

                } else {
                    return 0;
                }

            } else {
                if (StringUtils.contains(arSection[0], ":")) {
                    arSecond = StringUtils.split(section, ":");
                    return Integer.valueOf(arSecond[1]);

                } else {
                    return 0;
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * LMS Percent를 String 형태로 반환 (기존과 동일)
     * @return		Percent
     */
    public int getLMSPercent() {
        int lmsPercent = 0;
        String[] strProcessedSection = getProcessedSection().split(",");
        if (null != strProcessedSection) {
            LogUtil.INSTANCE.info("birdgangloadlms" , "getLMSPercent > strProcessedSection: " + strProcessedSection.toString());
        }
        List<String[]> listProcessedSection = new ArrayList<String[]>();
        int lmsTime = 0;
        // ":"를 기준으로 StartTime, EndTime을 분리
        for (int i = 0; i < strProcessedSection.length; i++) {
            String processedSection = strProcessedSection[i];
            if (StringUtils.isNotBlank(processedSection)) {
                listProcessedSection.add(StringUtils.split(processedSection, ":"));
            }
        }

        // (EndTime - StartTime)을 이용해 실제 수강한 시간을 더함
        for (String[] section : listProcessedSection) {
            int start = Integer.parseInt(section[0]);
            int end = Integer.parseInt(section[1]);
            lmsTime += end - start;
            LogUtil.INSTANCE.info("birdgangloadlms" , "getLMSPercent > start: " + start + " , end : " + end + " , lmsTime : " + lmsTime + " , section : " + section);
        }
        // (수강이력 / 전체시간) * 100
        lmsPercent = (int) ((double) lmsTime / (double) lmsEntry.getDuration() * 100);
        LogUtil.INSTANCE.info("birdgangloadlms" , "getLMSPercent > lmsPercent: " + lmsPercent + " , lmsTime : " + lmsTime + " , mDuration : " + lmsEntry.getDuration());
        // TODO : LMS Percent가 98% 이상일 경우, 다 본 컨텐츠로 인식
        if (lmsPercent >= 98) {
            lmsPercent = 100;
        }

        return lmsPercent;
    }

    /**
     * LMS RawSection을 정렬하여 ProcessedSection으로 변환
     * @param rawSection
     * @return
     */
    private String sortSection(String rawSection) {
        LogUtil.INSTANCE.info("birdgangloadlms" , "sortSection > rawSection : " + rawSection);
        try {
            // 마지막이 ,로 끝날 경우 제거(mSection이 빈값일 경우 마지막이 ,로 끝남)
            if (StringUtils.endsWith(rawSection,",")) {
                rawSection = StringUtils.substring(rawSection, 0, rawSection.length()-1);
            }

            // 처음이 ,로 시작 경우 제거
            if (StringUtils.startsWith(rawSection, ",")) {
                rawSection = StringUtils.substring(rawSection, 1);
            }

            // ","를 기준으로 구간을 분리
            String[] strRawSection = StringUtils.split(rawSection, ",");
            // ":"를 기준으로 시작시간, 끝시간을 분리
            List<String[]> listRawSection = new ArrayList<String[]>();
            for (int i = 0; i < strRawSection.length; i++) {
                listRawSection.add(StringUtils.split(strRawSection[i], ":"));
            }

            // Map을 이용하여 시작시간이 동일한 경우 비교하여 병합
            // Map을 사용하는 이유 : Key를 이용하여 Value를 수정(Key=시작시간, Value=끝시간)
            Map<Integer, Integer> mapRawSection = new HashMap<Integer, Integer>();
            for (String[] raws : listRawSection) {
                // 최초 등록(Map에 데이터가 없는 경우)
                if (mapRawSection.size() == 0) {
                    mapRawSection.put(Integer.parseInt(raws[0]), Integer.parseInt(raws[1]));
                    // 기존 데이터가 존재(Map에 데이터가 있는 경우)
                } else {
                    // 현재 시작시간이 Map에 없으면 Section 추가
                    if (mapRawSection.get(Integer.parseInt(raws[0])) == null) {
                        mapRawSection.put(Integer.parseInt(raws[0]), Integer.parseInt(raws[1]));
                        // 현재 시작시간이 Map에 존재하면 비교
                    } else {
                        // 기존 끝시간과 현재 끝시간의 크기를 비교해서 현재 끝시간이 큰 경우에만 Section 추가
                        // 기존 끝시간이 더 큰 경우에는 기존 데이터를 유지
                        if (mapRawSection.get(Integer.parseInt(raws[0])) < Integer.parseInt(raws[1])) {
                            mapRawSection.put(Integer.parseInt(raws[0]), Integer.parseInt(raws[1]));
                        }
                    }
                }
            }

            // TreeMap을 이용하여 오름차순 정렬
            mapRawSection = new TreeMap<Integer, Integer>(mapRawSection);

            // Map을 Array로 변환
            // Map을 사용하면 Key에 대응하는 Value는 얻을 수 있지만, 모든 Key와 Value를 한번에 볼 수는 없어서 Array로 변환(Iterator를 이용)
            int[][] rawSectionArray = new int[mapRawSection.size()][2];
            int count = 0;
            Iterator iterator = mapRawSection.entrySet().iterator();
            while(iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                rawSectionArray[count][0] = (Integer) entry.getKey();
                rawSectionArray[count][1] = (Integer) entry.getValue();
                count++;
            }
            // count 초기화 - 재사용할 예정이므로 초기화
            count = 0;

            // 병합할 Array 생성. 병합중에는 이전(시작, 끝)이라는 이름으로 사용 - String으로 만들기 전 최종 데이터
            int[][] mergeSectionArray = new int[mapRawSection.size()][2];
            // 병합(구간별 차이가 2초 이내일 경우에는 하나의 구간으로 인식하여 병합)
            for (int i = 0; i < rawSectionArray.length; i++) {
                int startSec = rawSectionArray[i][0];
                int endSec = rawSectionArray[i][1];
                // 최초 등록
                if (count == 0) {
                    mergeSectionArray[count][0] = startSec;
                    mergeSectionArray[count][1] = endSec;
                    count++;
                    // 이전 시간이 존재할 경우 비교하여 병합
                } else {
                    // 이전_끝이 현재_끝보다 작을 경우
                    if (mergeSectionArray[count-1][1] < endSec) {
                        // 현재_끝이 영상 전체시간보다 클 경우 현재_끝 시간을 전체시간으로 수정
                        if (endSec > lmsEntry.getDuration()) {
                            endSec = lmsEntry.getDuration();
                        }
                        // 이전_끝과 현재_시작 시간의 차이가 2초 이내일 경우 구간을 병합
                        if (startSec - mergeSectionArray[count-1][1] <= 2) {
                            mergeSectionArray[count-1][1] = endSec;
                            // 병합할 수 없을 경우 새로운 구간을 추가
                        } else {
                            mergeSectionArray[count][0] = startSec;
                            mergeSectionArray[count][1] = endSec;
                            // 병합된 배열의 크기를 증가
                            count++;
                        }
                    }
                }
            }

            // 최종적으로 반환할 구간정보(String)
            String strMergeSection = "";
            for (int i = 0; i < count; i++) {
                strMergeSection += String.valueOf(mergeSectionArray[i][0]) + ":" + String.valueOf(mergeSectionArray[i][1]) + ",";
            }
            // 마지막이 ,로 끝날 경우 제거
            if (strMergeSection.endsWith(",")) {
                strMergeSection = strMergeSection.substring(0, strMergeSection.length()-1);
            }

            return strMergeSection;

        } catch(Exception e) {
            e.printStackTrace();
        }

        return "";
    }


    public long updateLms (LmsEntry lmsEntry) {
        LMSCacheEntry lmsCacheEntry = lmsEntry.toConvertCacheEntry();
        return LMSDao.getDefault().insertAndUpdateSync(lmsCacheEntry);
    }


    public LmsEntry findLmsCacheEntryById (int id) {
        LmsEntry lmsEntry = null;
        try {
            LMSCacheEntry lmsCacheEntry = LMSDao.getDefault().getEntryByContentId(id);
            if (null == lmsCacheEntry) {
                lmsEntry = new LmsEntry();
            }
            else {
                lmsEntry = new LmsEntry(lmsCacheEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return lmsEntry;
    }


    public LmsEntry findLmsCacheEntryByPath (String path) {
        LmsEntry lmsEntry = null;
        try {
            LMSCacheEntry lmsCacheEntry = LMSDao.getDefault().getEntryByContentPath(path);
            if (null == lmsCacheEntry) {
                lmsEntry = new LmsEntry();
            }
            else {
                lmsEntry = new LmsEntry(lmsCacheEntry);
            }
        } catch (Exception e) {
            LogUtil.INSTANCE.error(TAG, e);
        }
        return lmsEntry;
    }

}
