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

### 어플리케이션 제작 API
: 어플리케이션 제작시 고객이 원하는 일부 디자인 요소 및 기능등을 수정할 수 있습니다.
수정 할 요소는 각 뷰(Activity or Fragment) 단위로 메소드 정의가 되어 있습니다.

</br>

### 플레이어 망고

</br>

<B> MangoApplication</B>
: Application 구현체 입니다. 해당 클래스는 앱 제작시 필요한 리소스를 재정의 할 수 있습니다.

``` java
provideEnableDeviceModels();
> 디바이스 제한 : 해당 목록에 포함된 디바이스 만 구동 됩니다. 값이없는 경우는 디바이스 제한하지 않습니다.


provideApplicationContentId();
> 상품에 포함된 컨텐츠의 고유 값을 정의 합니다.


provideExternalSdPath();
>


provideCardManufacturer();
> SD 카드 제조번호를 제공합니다.


provideHomeWebViewUrl();
> 홈 영역에 노출될 웹뷰 영역 URL 을 제공 합니다.


provideSubHomeWebViewUrl();
> 홈 영역에 노출될 오프라인 웹뷰 영역 리소스 경로를 제공 합니다.


providePrivacyPolicyUrl();
> 개인정보 취급방침 : 해당 메소드를 오버라이딩 하여 요청 url을 변경 할 수 있습니다.


provideDialogBtnColor();
> 어플리케이션 내 다이어로그 팝업 컬러 코드를 제공 합니다.
```

</br></br>
<B>SplashActivityEx </B>
: 어플리케이션 구동 시 초기 화면 입니다.

``` java
Class<?> provideNextContentView();
> 인증 성공시 다음에 보여질 화면을 지정합니다.


int provideSplashDelayTime();
> 화면 유지 시간을 지정 합니다.


int provideSplashImageResource();
>
```

</br></br>
<B>CertificationActivityEx</B>
: SD 카드를 통한 인증화면 입니다.

``` java
Class<?> provideNextContentView();
> 인증 성공시 다음에 보여질 화면을 지정합니다.


boolean onResultEnableDeviceModels();
> 디바이스 제한에 실패 했을 경우에 대한 Callback 메소드 입니다.
```


</br></br>
<B>DrawerActivityEx</B>
: 어플리케이션 메인 화면 입니다.
기능 별 각 화면을 정의 합니다.

``` java
int provideStatusBarBackgroundColor();
> 상태바의 컬러를 재정의 합니다.


int provideActionBarColor();
> 액션바의 컬러를 재정의 합니다.


int provideProgressBarDrawerResource ();
> 미디어 스캔시 프로그래시브 바 컬러를 재정의 합니다. 최초 한번 실행이 되며 재 스킨시 다시 보여집니다.


int provideProgressBarSyncDrawerResource ();
> 미디어 스캔시 데이터 싱크 프로그래시브 바 컬러를 재정의 합니다. 최초 한번 실행이 되며 재 스킨시 다시 보여집니다.

boolean onResultEnableDeviceModels();
> 디바이스 제한에 실패 했을 경우에 대한 Callback 메소드 입니다.
```

</br></br>
<B>ExplorerFragmentEx</B>
: 미디어 목록 화면 입니다.

``` java
int provideNavigationBackgroundColor ();
> 디렉토리 탐색기 컬러를 재정의 합니다.


int provideProgressBarForLmsDrawable ();
> 강의 진도율 프로그래스 바 컬러를 재정의 합니다.


int provideTextRateColorForLms ();
> 강의 진도율 활성화 컬러를 재정의 합니다.


int provideTextRateDimColorForLms ();
> 강의 진도율 비활성화 컬러를 재정의 합니다.


Class<?> provideSearchView();
> 검색 Activity를 지정 합니다.


void onLoadPlaybackActivity(PlayerEntry playerEntry);
> 재생 시도 후처리 Callback method 입니다.
```

</br></br>
<B>FavoriteFragmentEx</b>
: 즐겨 찾기 목록 화면 입니다.

``` java
void onLoadPlaybackActivity(PlayerEntry playerEntry);
> 재생 시도 후처리 Callback method 입니다.
```

</br></br>
<B>RecentlyFragmentEx</b>
: 최근재생 목록 화면 입니다.

``` java
void onLoadPlaybackActivity(PlayerEntry playerEntry);
> 재생 시도 후처리 Callback method 입니다.
```

</br></br>
<B>SearchActivityEx</b>
: 검색 메인 화면 입니다.

``` java
BaseFragment provideSearchFragment();
> 검색 목록 화면을 지정합니다.

void onLoadPlaybackActivity(PlayerEntry playerEntry);
> 재생 시도 후처리 Callback method 입니다.
```

</br></br>
<B>WebViewFragmentEx</B>
: WebView 화면 입니다.


</br></br>
<B>SettingFragmentEx</b>
: 설정 화면 입니다.

``` java
List<SectionedRecyclerViewAdapter.Section> provideSettingSectionMenus();
: 설정화면 섹션 목록을 지정합니다.

List<SettingMenuViewEntry> provideSettingMenus ();
: 설정화면 메뉴 목록을 지정합니다.\

```




