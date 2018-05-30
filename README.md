## INKA NetSync

전용 어플리케이션 제작을 위해 SDK 와 템플릿 소스를 제공 합니다.


### 상품 프로세스

1. 업체에서 어플리케이션 제작 요청
2. 해당 어플리케이션 CID (SD 카드에 고유값을 도출하기 위해 , Content ID 와는 다른용도) 생성.
3. 컨텐츠 패키징 및 전용 소스&템플릿 Repository 주소 전달
4. 각 업체에서 앱제작 및 APK 파일 받음.
5. 잉카에서 전달 받은 APK 파일과 패키징 된 컨텐츠와 함께 마스터 카드 생성 및 전달


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
    classpath 'com.android.tools.build:gradle:3.0.1'
    classpath 'com.google.gms:google-services:3.1.0'
    classpath 'org.greenrobot:greendao-gradle-plugin:3.2.1'
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
    calligraphyVersion = '2.2.0'
    nineoldandroids = '2.4.0'

    rx2AndroidNetworking = '0.0.1'

    dagger2Version = '2.8'
    rxjava2Version = '2.0.5'
    rxandroidVersion = '2.0.1'
    butterKnifeVersion = '8.5.1'
    greenDaoVersion = '3.2.0'
    placeholderviewVersion = '0.6.1'
    debugDBVersion = '1.0.0'
    timberVersion = '4.5.1'

    junitVersion = '4.12'
    espressoVersion = '2.2.2'
    mockitoVersion = '2.7.1'
}

```


그런 다음 모듈의 build.gradle 파일에 종속성을 추가하십시오. 다음은 전체 라이브러리에 종속성을 추가합니다.
``` java
api project(':netsyncsdk');
api project(':visualonplayer');
```

</br>


### 화면별 사용자 정의 API 
: 어플리케이션 제작시 고객이 원하는 일부 디자인 요소 및 기능등을 수정할 수 있습니다.
수정 할 요소는 각 뷰(Activity or Fragment) 단위로 메소드 정의가 되어 있습니다.


스크린샷
-------
<img src="https://user-images.githubusercontent.com/39693463/40693044-3a14d160-63f0-11e8-9c5b-701a65de4c5e.jpg"></img>

<img src="https://user-images.githubusercontent.com/39693463/40693049-4498c038-63f0-11e8-81b1-be35258fd975.jpg"></img>