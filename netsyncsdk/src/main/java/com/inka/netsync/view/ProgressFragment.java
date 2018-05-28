package com.inka.netsync.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inka.netsync.R;

/**
 * Created by birdgang on 2018. 4. 25..
 */

public class ProgressFragment extends Fragment {

    private final static String BUNDLE_FRAGMENT_DATA = "show_msg";
    private String mShowMessage = "";
    private TextView mTxtMessage = null;

    public static ProgressFragment newInstance(String showMessage) {
        ProgressFragment newFragment = new ProgressFragment();

        Bundle args = new Bundle();
        args.putString(BUNDLE_FRAGMENT_DATA, showMessage);
        newFragment.setArguments(args);
        return newFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mShowMessage = bundle.getString(BUNDLE_FRAGMENT_DATA);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_progress, container, false);
        mTxtMessage = (TextView) view.findViewById(R.id.txt_message);
        mTxtMessage.setText(mShowMessage);

        view.setOnTouchListener(mTouchListener);

        return view;
    }

    View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return true;
        }
    };

    public void setText(String message) {
        if (mTxtMessage != null) {
            mTxtMessage.setText(message);
        }
    }

    public void setSize(float size) {
        if (mTxtMessage != null) {
            mTxtMessage.setTextSize(size);
        }
    }

    public void setBackgroundColor(int color) {
        if (mTxtMessage != null) {
            mTxtMessage.setBackgroundColor(color);
        }
    }

    public void setTextColor(int color) {
        if (mTxtMessage != null) {
            mTxtMessage.setTextColor(color);
        }
    }

}
