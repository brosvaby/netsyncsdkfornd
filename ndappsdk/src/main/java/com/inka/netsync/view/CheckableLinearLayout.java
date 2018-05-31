package com.inka.netsync.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    final String NS 	= "http://schemas.android.com/apk/res-auto";
    final String ATTR 	= "checkable";

    int 		mCheckableId;
    Checkable 	mCheckable;

    public CheckableLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mCheckableId = attrs.getAttributeResourceValue(NS, ATTR, 0);
    }

    @Override
    public boolean isChecked() {
        mCheckable = (Checkable)findViewById(mCheckableId);
        if(mCheckable == null) {
            return false;
        }
        return mCheckable.isChecked();
    }

    @Override
    public void setChecked(boolean checked) {
        mCheckable = (Checkable)findViewById(mCheckableId);
        if(mCheckable == null) {
            return;
        }

        mCheckable.setChecked(checked);
    }

    @Override
    public void toggle() {
        mCheckable = (Checkable) findViewById(mCheckableId);
        if(mCheckable == null)
            return;
        mCheckable.toggle();
    }
}