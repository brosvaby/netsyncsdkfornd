package com.inka.netsync.view.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.inka.netsync.R;

import org.apache.commons.lang3.StringUtils;

public class EditTextViewSerialNumberWidget extends LinearLayout {

    private MaterialEditText editMessage1 = null;
    private MaterialEditText editMessage2 = null;
    private MaterialEditText editMessage3 = null;
    private MaterialEditText editMessage4 = null;

    private LayoutInflater inflater = null;
    private ViewGroup root = null;

    private InputMethodManager inputMethodManager;

    private EditTextInputFinishListener editTextInputFinishListener;

    private int primaryColor = -1;

    public interface EditTextInputFinishListener {
        public void onUpdate(String values);
        public void onCompleate(String values);
    }

    public EditTextViewSerialNumberWidget(Context paramContext) {
        super(paramContext);
        initailizeWidget();
    }

    public EditTextViewSerialNumberWidget(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initailizeWidget();
    }

    private void initailizeWidget() {
        this.inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.root = ((ViewGroup)this.inflater.inflate(R.layout.widget_serial_edit_text_view, this));
        this.editMessage1 = ((MaterialEditText)this.root.findViewById(R.id.edit_serial_number1));
        this.editMessage1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6) {
                    editMessage2.requestFocus();
                }
            }
        });

        this.editMessage2 = ((MaterialEditText)this.root.findViewById(R.id.edit_serial_number2));
        this.editMessage2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != editTextInputFinishListener) {
                    editTextInputFinishListener.onUpdate(getEditMessageAll());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6) {
                    editMessage3.requestFocus();
                }
            }
        });

        this.editMessage3 = ((MaterialEditText)this.root.findViewById(R.id.edit_serial_number3));
        this.editMessage3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != editTextInputFinishListener) {
                    editTextInputFinishListener.onUpdate(getEditMessageAll());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6) {
                    editMessage4.requestFocus();
                }
            }
        });

        this.editMessage4 = ((MaterialEditText)this.root.findViewById(R.id.edit_serial_number4));
        this.editMessage4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (null != editTextInputFinishListener) {
                    editTextInputFinishListener.onUpdate(getEditMessageAll());
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    
    public void setEditTextInputFinishListener (EditTextInputFinishListener listener ) {
        this.editTextInputFinishListener = listener;
    }

    public void setPrivateImeOptions (String options) {
        this.editMessage1.setPrivateImeOptions(options);
        this.editMessage2.setPrivateImeOptions(options);
        this.editMessage3.setPrivateImeOptions(options);
        this.editMessage4.setPrivateImeOptions(options);
    }

    public void setPrimaryColor(int color) {
        this.primaryColor = color;
        if (null != editMessage1 && color > 0) {
            this.editMessage1.setPrimaryColor(ContextCompat.getColor(getContext(), color));
        }
        if (null != editMessage2 && color > 0) {
            this.editMessage2.setPrimaryColor(ContextCompat.getColor(getContext(), color));
        }
        if (null != editMessage3 && color > 0) {
            this.editMessage3.setPrimaryColor(ContextCompat.getColor(getContext(), color));
        }
        if (null != editMessage4 && color > 0) {
            this.editMessage4.setPrimaryColor(ContextCompat.getColor(getContext(), color));
        }
    }


    public void requestForcedKeyboard (Activity activity) {
        inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInputFromInputMethod((getEditMessage1()).getWindowToken(), InputMethodManager.SHOW_FORCED);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    public String getEditMessageAll() {
        return  ((null != editMessage1) ? editMessage1.getText().toString() : "") + ((null != editMessage2) ? editMessage2.getText().toString() : "") +  ((null != editMessage3) ? editMessage3.getText().toString() : "") + ((null != editMessage4) ? editMessage4.getText().toString() : "");
    }


    public String getEditMessageToUpperCaseAll () {
        return  ((null != editMessage1) ? editMessage1.getText().toString().trim().toUpperCase() : "") + ((null != editMessage2) ? editMessage2.getText().toString().trim().toUpperCase() : "") +  ((null != editMessage3) ? editMessage3.getText().toString().trim().toUpperCase() : "") + ((null != editMessage4) ? editMessage4.getText().toString().trim().toUpperCase() : "");
    }

    public void setEditMessage (String values) {
        editMessage1.setText(StringUtils.substring(values, 0,6));
        editMessage1.invalidate();
        editMessage2.setText(StringUtils.substring(values, 6,12));
        editMessage2.invalidate();
        editMessage3.setText(StringUtils.substring(values, 12,18));
        editMessage3.invalidate();
        editMessage4.setText(StringUtils.substring(values, 18,24));
        editMessage4.invalidate();
    }


    public EditText getEditMessage1() {
        return editMessage1;
    }

    public void setEditMessage1(MaterialEditText editMessage1) {
        this.editMessage1 = editMessage1;
    }

    public EditText getEditMessage2() {
        return editMessage2;
    }

    public void setEditMessage2(MaterialEditText editMessage2) {
        this.editMessage2 = editMessage2;
    }

    public EditText getEditMessage3() {
        return editMessage3;
    }

    public void setEditMessage3(MaterialEditText editMessage3) {
        this.editMessage3 = editMessage3;
    }

    public EditText getEditMessage4() {
        return editMessage4;
    }

    public void setEditMessage4(MaterialEditText editMessage4) {
        this.editMessage4 = editMessage4;
    }
}
