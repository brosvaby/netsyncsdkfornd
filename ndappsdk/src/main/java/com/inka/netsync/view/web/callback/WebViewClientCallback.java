package com.inka.netsync.view.web.callback;

/**
 * Created by birdgang on 2017. 4. 22..
 * WebViewClient 에서 로직을 처리하다가 UI단에서 처리해야되는 부분을 콜백으로 호출해주기 위한 인터페이스
 */

public interface WebViewClientCallback {
    public void onRequireDoMainWork();
    public void onUpdateScreen();
    public void onShowRefreshPage();
    public void onShowWebViewPage();
    public void onMoveMyFolder();
    public void onMoveContent(int index);
    public void onMovePlayedList();
    public void onMoveHome();
    public void onMoveFavorite();
    public void onMoveSetting();
    public void onMove3rdpartyapp();
}