## INKA NetSync

전용 어플리케이션 제작을 위해 SDK 와 템플릿 소스를 제공 합니다.


### 상품 프로세스

1. 업체에서 어플리케이션 제작 요청
2. 플레이어 배속재생을 위한 어플리케이션 Package 지정 및 전달  ex) com.inka.playermango
3. 해당 어플리케이션 CID (SD 카드에 고유값을 도출하기 위해 , Content ID 와는 다른용도) 생성.
4. 컨텐츠 패키징 및 전용 소스 & 템플릿 Repository 주소 & 배속관련 설정 파일 전달
5. 각 업체에서 앱제작 및 APK 파일 받음.
6. 잉카에서 전달 받은 APK 파일과 패키징 된 컨텐츠와 함께 마스터 카드 생성 및 전달


</br>

### NCG 가이드

- <a href="https://docs.pallycon.com/ko/license-gateway.html">PallyCon NCG Android SDK 개발 가이드</a>
- <a href="https://docs.pallycon.com/ko/license-gateway.html">PallyCon 멀티 DRM 라이선스 콜백 게이트웨이 API
</a>


</br>

### 모듈
- netsyncsdk : ND 어플리케이션 SDK
- samplendplayerapple : 시리얼 인증 방식 어플리케이션 샘플
- samplendplayermango : SD카드 인증 방식 어플리케이션 샘플

</br>

### 환경


프로젝트의 루트에있는 build.gradle 파일에 JCenter 및 Google 리포지토리가 포함되어 있는지 확인해야합니다.

``` java
repositories {
    jcenter()
    google()
}
    
dependencies {
    classpath 'com.android.tools.build:gradle:3.1.2'
    classpath 'com.jakewharton:butterknife-gradle-plugin:8.5.1'
}
    
ext {
    minSdkVersion = 14
    targetSdkVersion = 24
    compileSdkVersion = 26
    buildToolsVersion = '26.0.2'

    supportLibraryVersion = '24.2.1'
    supportAppcompatVersion = '24.2.1'
    supportDesignVersion = '24.2.1'
    supportRecyclerViewVersion = '24.2.1'
    supportLibraryMultidexVersion = '1.0.1'

    gsonVersion = '2.8.0'
    nineoldandroids = '2.4.0'

    rx2AndroidNetworking = '0.0.1'

    dagger2Version = '2.8'
    rxjava2Version = '2.0.5'
    rxandroidVersion = '2.0.1'
    butterKnifeVersion = '8.5.1'
}

```


그런 다음 모듈의 build.gradle 파일에 종속성을 추가하십시오. 다음은 전체 라이브러리에 종속성을 추가합니다.
``` java
api project(':netsyncsdk');
api project(':visualonplayer');
```


## 배속 플레이
어플리케이션 배속 사용시 플레이어 라이센스 파일이 필요합니다. <br>
라이센스는 어플리케이션 패키지 명을 기준으로 동작되기 때문에, 앱 제작 하기전 미리 패키지 명을 INKA 에 알려주시면 배속 라이센스를 포함한 voVidDec.dat 파일을 제공해 드립니다.<br>
해당 voVidDec.dat 파일은 Assets 디렉토리로 관리 되어 집니다. <br>



## 화면별 사용자 정의 API 
: 어플리케이션 제작시 고객이 원하는 일부 디자인 요소 및 기능등을 수정할 수 있습니다.
수정 할 요소는 각 뷰(Activity or Fragment) 단위로 메소드 정의가 되어 있습니다.


``` java
/**
* 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 제한될 디바이스 목록을 지정 하십시오. 
* 그러면 해당 명을 가진 디바이스 외에 다른 디바이스 에서는 실행이 제한 되어 집니다.
* 해당 항목이 비어있을 경우에는 디바이스 제한을 하지 않습니다.
**/
public static List<String> provideEnableDeviceModels() {
    return new ArrayList<String>(Arrays.asList("LG-V498S2", "LG-V498", "LG-V498S1", "LG-V400S1", "LG-V525S1", "LG-V525S3"));
}
    
    

/**
* 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 제한될 컨텐츠의 고유 번호를 지정 하십시오. 
* 해당 항목이 비어있을 경우에는 컨텐츠 목록이 보여지지 않거나 원할한 진행이 되지 않을 수 있기 때문에 반듯이 입력을 해야 하는 값입니다.
* 관련된 값은 INKA 에서 제공 받을 수 있습니다.
**/
@Override
protected String provideApplicationContentId() {
    return "xxxx@pallycon.sd";
}
    
    
/**
* 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 온라인 상태일 경우 홈 영역에 보여질 URL 을 지정 하십시오. 
* 해당 항목이 비어있을 경우에는 홍 영역을 제대로 표시 할 수 없습니다.
**/
@Override
protected String provideHomeWebViewUrl() {
    return "http://ndpro.co.kr/";
}
    
    
/**
* 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 오프라인 상태일 경우 홈 영역에 보여질 Path 를 지정 하십시오. 
* 해당 항목이 비어있을 경우에는 홍 영역을 제대로 표시 할 수 없습니다.
**/
@Override
protected String provideSubHomeWebViewUrl() {
    return "file:///android_asset/mainscreen/index.html";
}
    
    
/**
* 템플릿 Application 영역에 하기 메소드를 오버라이드 하여 개인정보 취급 방침에 대한 URL 을 지정 하십시오.
* 해당 항목이 비어있을 경우에는 정상적으로 표시 되어 지지 않습니다.
**/
@Override
protected String providePrivacyPolicyUrl() {
    return "http://ndpro.co.kr/privacypolicy/ndprivacypolicy.html";
}
```

    

템플릿 망고
-------
<div>
    <img width="200" src="https://user-images.githubusercontent.com/39693463/40693044-3a14d160-63f0-11e8-9c5b-701a65de4c5e.jpg"></img>
    <img width="200" src="https://user-images.githubusercontent.com/39693463/40693049-4498c038-63f0-11e8-81b1-be35258fd975.jpg"></img>
    <img width="200" src="https://user-images.githubusercontent.com/39693463/40693727-fe695a6a-63f3-11e8-87db-026583d8ac7a.jpg"></img>
    <img width="200" src="https://user-images.githubusercontent.com/39693463/40693728-feac4078-63f3-11e8-9ebd-dcab154159fc.jpg"></img>
</div>
