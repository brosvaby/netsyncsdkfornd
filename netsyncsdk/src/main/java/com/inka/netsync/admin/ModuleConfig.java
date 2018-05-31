package com.inka.netsync.admin;

import java.util.HashMap;

/**
 * Created by birdgang on 2016. 7. 19..
 */
public class ModuleConfig {
    public static boolean ENABLE_LOG = true;            // 로컬 로그 사용 유무
    public static boolean ENABLE_SAVE_LOG_FILE = false;            // 로컬 로그 사용 유무
    public static boolean ENABLE_NOT_NEED_TO_SIRIAL_NUMBER = false;  // 시리얼 번호를 입력할 필요가 없다
    public static boolean ENABLE_EXTRACT_DATABASE = true;            // 데이타베이스 축출
    public static boolean ENABLE_NO_LIMIT_BY_CID = true;            // content id로 app에서 컨텐츠 제한 유무
    public static boolean ENABLE_NO_CERTIFICATION = false;            // 인증 유무
    public static boolean ENABLE_NO_DEVICE_CHECK = false;            // 디바이스 제한 체크 유무
    public static boolean ENABLE_NO_SDCARD_MANUFACTIRER_CHECK = false;      // SD카드 제조 업체 체크 - 현재 두이 업체만 확인하고 있음.
    public static boolean ENABLE_NO_LICENSE = false;                 // 라이센스 체크 유무

    public static boolean ENABLE_NO_DRAWER_MENU = false;        // 사이드 메뉴 활성화 유무
    public static boolean ENABLE_CUSTOM_WEBVIEW_RESOURCE = false;   // 홈쪽 외부 리소스 사용유무
    public static boolean ENABLE_FORCE_USE_BASE_PLAYER = false;
    public static boolean ENABLE_NO_SECURITY_MODE = false;               // 보안모드
    public static boolean ENABLE_NO_ANTI_MIRRORING_MODE = false;               // 외부출력
    public static boolean ENABLE_NO_SCAN_MEDIA = false;
    public static boolean ENABLE_SCAN_MEDIA_MP4 = false;             // mp4파일 스캔 유무
    public static boolean ENABLE_SCAN_MEDIA_MP4_ONLY = false;        //
    public static boolean ENABLE_FORCE_INTRO_GUIDE = false;
    
    public static boolean ENABLE_MODE_PREVIEW_APP = false;

    public static final int CERTIFICATION_FORCE_LEVEL = -1;    // Build.VERSION_CODES.KITKAT , Build.VERSION_CODES.LOLLIPOP

    public static HashMap<String, String> usedSerials = new HashMap<String, String>();


    static {
        usedSerials.put("LG-V498", "X27MKOQE9VCWEP472C015AEA");
        usedSerials.put("v400s1", "S9QRJ6SRRFTZGTPM3ADAADA4");
        usedSerials.put("v400s1", "ZSYFBE73QPPYSR0ID99512F7");
        usedSerials.put("SHV-E310S", "Q7SO44DCW5FL7VY6F74A7556"); // common

        usedSerials.put("LGM-K120S", "F8BVL01Q0VK15567635E454D"); // common
//        usedSerials.put("LGM-K120S", "F8BVL01Q0VK15567635E4533"); // invalid
//        usedSerials.put("LGM-K120S", "f8bvl01q0vk15567635e454d"); // common
//        usedSerials.put("LGM-K120S", "ENG4DEUCU4A92TF430B941FA");  // 고정 만료
//        usedSerials.put("LGM-K120S", "73AXDKNRA9ZINAWTE3FDD219");  // 고정
//        usedSerials.put("LGM-K120S", "OGC9L2N7NPDQ2IW9FC5DC0DC");  // 무제한
//        usedSerials.put("LGM-K120S", "LPRCGYTGNOX9GBAJ89167EF5");  // 유동1일

//        usedSerials.put("LGM-V300S" , "HR479PXAAQE4P51BF5F27640");
        usedSerials.put("LGM-V300S" , "XUKAQ91YSHV4EW77DF5AB9A8");
        usedSerials.put("SM-G950N" , "AFo1NE1oB33FE03J3288AAD1");       //

        usedSerials.put("SM-G935L", "2CYU363J7S0I8XB30E7F2120"); // common

        usedSerials.put("SHV-E300L", "Q57MKLDGNQG66EJ8C405F8E0");  // 무제한
        usedSerials.put("LG-V525S1", "98WIGWWXAXSO7BE8EBCE3389");
//        usedSerials.put("LG-V525S1", "6H98WIP1FB9TDD3PD2F8E5D5");       // 음원

        usedSerials.put("ST-PAD", "FK4R7G65I4WZ1UGYDF4AEFD9"); // 무제한

//        usedSerials.put("IM-A890L", "ODX13CNQRE85MHD293FAA56B");  //ProductCode: "C6HAJGFF2BBC3EC1",
        usedSerials.put("IM-A890L", "PYF3LZKQBWPL7SFMC8B40097");  //ProductCode: private note1,
    }

    public static String getAvailSirialNumber (String deviceModelName) {
        return usedSerials.get(deviceModelName);
    }

}