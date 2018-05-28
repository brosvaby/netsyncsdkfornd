package com.inka.netsync.view.web.callback;

public interface ViewClientCallback {
    void onMoveMyFolder();
    void onMoveContent(int index);
    void onMovePlayedList();
    void onMoveHome();
    void onMoveFavorite();
    void onMoveSetting();
    void onMove3rdpartyapp();
}