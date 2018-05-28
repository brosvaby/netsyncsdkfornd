package com.inka.netsync.view.dialog.progress;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.inka.netsync.R;

/**
 * Created by birdgang on 2018. 4. 26..
 */
public class LoadingProgressDialog extends ProgressDialog {

    private Animation mAnimation = null;

    private ImageView progressImg;

    private Context context = null;

    public LoadingProgressDialog(Context context) {
        this(context, -1);
    }

    public LoadingProgressDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        mAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_rotation);
        progressImg = (ImageView) findViewById(R.id.loading_img);
        progressImg.startAnimation(mAnimation);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (null != mAnimation) {
            mAnimation.start();
        }
    }

    @Override
    public void show() {
        super.show();
        setContentView(R.layout.dialog_progress_loading_content);

        if (null == progressImg) {
            return;
        }

        if (null != mAnimation) {
            mAnimation.start();
        }
    }

    @Override
    public void dismiss() {
        if (isShowing()) {
            if (null != mAnimation) {
                mAnimation.cancel();
            }
            super.dismiss();
        }
    }

}
