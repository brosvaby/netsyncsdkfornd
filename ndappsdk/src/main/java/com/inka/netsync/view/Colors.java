package com.inka.netsync.view;

import android.graphics.Color;

/**
 * Created by birdgang on 2018. 2. 28..
 */

public class Colors {
    public static boolean isLight(int color) {
        return Math.sqrt(
                Color.red(color) * Color.red(color) * .241 +
                        Color.green(color) * Color.green(color) * .691 +
                        Color.blue(color) * Color.blue(color) * .068) > 130;
    }
}
