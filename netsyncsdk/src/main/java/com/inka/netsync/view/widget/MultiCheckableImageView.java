package com.inka.netsync.view.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.Toast;

import com.inka.netsync.R;

/**
 * Created by birdgang on 2018. 2. 23..
 */
public class MultiCheckableImageView extends AppCompatImageView {

    private Context mContext;

    private int mCheckedState = -1;
    public static final int REPEAT_OFF = -1;
    public static final int REPEAT_ONE = 0;
    public static final int REPEAT_ALL = 1;
    public static final int REPEAT_A = 2;
    public static final int REPEAT_B = 3;

    public MultiCheckableImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public int isCheckedState() {
        return mCheckedState;
    }

    public void setCheckedStateRepeatMode (int state) {
        this.mCheckedState = state;
        if (REPEAT_ONE == state) {
            setImageResource(R.drawable.img_repeatmode_1_on);
        }
        else if (REPEAT_ALL == state) {
            setImageResource(R.drawable.img_repeatmode_on);
        }
        else {
            setImageResource(R.drawable.img_repeatmode_off);
        }

        refreshDrawableState();
    }

    public void setCheckedStateArea (int state) {
        this.mCheckedState = state;
        if (REPEAT_A == state) {
            setImageResource(R.drawable.img_me_repeat_a);
        }
        else if (REPEAT_B == state) {
            setImageResource(R.drawable.img_me_repeat_ab);
        }
        else {
            setImageResource(R.drawable.img_me_repeat_off);
        }

        refreshDrawableState();
    }

    public int initReqeatMode () {
        return mCheckedState = REPEAT_OFF;
    }

    public int changeRepeatMode(boolean needToRepeatAll, boolean isAudio) {
        if (REPEAT_OFF == isCheckedState()) {
            mCheckedState = REPEAT_ONE;
            setImageResource(R.drawable.img_repeatmode_1_on);
            Toast.makeText(mContext, mContext.getString(R.string.player_function_repeatmode2), Toast.LENGTH_SHORT).show();
        }
        else if (REPEAT_ONE == isCheckedState() && needToRepeatAll) {
            mCheckedState = REPEAT_ALL;
            setImageResource(R.drawable.img_repeatmode_on);
            Toast.makeText(mContext, mContext.getString(R.string.player_function_repeatmode3), Toast.LENGTH_SHORT).show();
        }
        else {
            mCheckedState = REPEAT_OFF;
            setImageResource(R.drawable.img_repeatmode_off);
            Toast.makeText(mContext, mContext.getString(R.string.player_function_repeatmode1), Toast.LENGTH_SHORT).show();
        }

        if (isAudio) {
            Toast.makeText(mContext, mContext.getString(R.string.message_for_toast_player_repeat_hidden), Toast.LENGTH_LONG).show();
        }

        refreshDrawableState();

        return mCheckedState;
    }


    public int changeRepeatArea() {
        if (REPEAT_OFF == isCheckedState()) {
            mCheckedState = REPEAT_A;
            setImageResource(R.drawable.img_me_repeat_a);
        }
        else if (REPEAT_A == isCheckedState()) {
            mCheckedState = REPEAT_B;
            setImageResource(R.drawable.img_me_repeat_ab);
        }
        else {
            mCheckedState = REPEAT_OFF;
            setImageResource(R.drawable.img_me_repeat_off);
        }

        refreshDrawableState();

        return mCheckedState;
    }

}