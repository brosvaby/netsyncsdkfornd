package com.inka.netsync.view.web;

/**
 * Created by birdgang on 2017. 4. 22..
 * 자바 스크립트 인터페이스
 * 자바 스크립트에서 호출되는 메소드. SmartNetsyncWebViewClient 사용 클래스에서는 해당 인터페이스를 반드시 구현해야 한다.
 *
 * 아래 코드로 자바스크립트에서 Android App단의 java함수를 호출할 수 있게 해준다.
 * mWebView.addJavascriptInterface(new InkaJavaScriptClass(), "INKA");
 *
 * java 스크립트에서는 아래와 같이 App의 메소드를 직접호출한다.
 * 예)
 * window.INKA.resultDownloadInfos( json_content_if );
 */
public interface WebViewJavaScriptInterface {
    void onSetHtmlTitleMessage(String title);
}