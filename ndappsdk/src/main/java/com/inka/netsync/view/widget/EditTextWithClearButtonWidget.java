package com.inka.netsync.view.widget;

import android.content.Context;
import android.content.res.Resources;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.inka.netsync.R;
import com.inka.netsync.logs.LogUtil;


public class EditTextWithClearButtonWidget extends LinearLayout {

    private final String TAG = "EditTextWithClearButtonWidget";

    private final int REPORT = 0;

    private Button button = null;
    private Button btnCompleate = null;
    private ImageView clearImage = null;
    private EditText editMessage = null;
    private LayoutInflater inflater = null;
    private OnDoneClickListener onDoneClickListener = null;
    private OnClearListener listener = null;
    private OnTextChangeListener textChangeListener = null;
    private int minLength = 1;
    private int maxLength = 15;
    private ViewGroup root = null;
    private RelativeLayout mEditLayout = null;
    private View mBottomLine;

    public EditTextWithClearButtonWidget(Context paramContext) {
        super(paramContext);
        initailizeWidget();
    }

    public EditTextWithClearButtonWidget(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        initailizeWidget();
    }

    private void initailizeWidget() {
        this.inflater = (LayoutInflater) super.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.root = ((ViewGroup)this.inflater.inflate(R.layout.widget_clear_button_edit_text_search_view, this));
        this.mEditLayout = ((RelativeLayout)this.root.findViewById(R.id.layout_edittext));
        this.editMessage = ((EditText)this.root.findViewById(R.id.text_edit));
        this.clearImage = ((ImageView)this.root.findViewById(R.id.iv_clear));
        this.clearImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.INSTANCE.info(TAG, "clearImage > onClick ");
                getEditText().setText("");
                if (getEditText() != null) {
                    getEditText().requestFocus();
                }
                
                if (listener != null) {
                    listener.onClear(EditTextWithClearButtonWidget.this);
                }
            }
        });

        this.editMessage.addTextChangedListener(new EditTextWatcherListener(this));
        this.editMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId) {
                    case EditorInfo.IME_ACTION_SEARCH:
                        // 검색 동작
                        if (null != onDoneClickListener) {
                            onDoneClickListener.onDoneClicked(v.getText().toString());
                        }
                        break;
                    default:
                        // 기본 엔터키 동작
                        return false;
                }
                return true;
            }
        });

//        mBottomLine = root.findViewById(R.id.bottom_line);
    }

    public void setClearImage (int type) {
        switch (type) {
            case REPORT :
                this.clearImage.setImageResource(R.drawable.btn_search_delete);
                break;

            default:
                this.clearImage.setImageResource(R.drawable.btn_search_delete);
                break;
        }
    }

    public EditText getEditText() {
        return this.editMessage;
    }

    public ImageView getImageView() {
        return this.clearImage;
    }

    public String getText() {
        return this.editMessage.getText().toString();
    }

    public View getView() {
        return this.root.getChildAt(0);
    }

    public void setHint(String paramString) {
        this.editMessage.setHint(paramString);
    }

    public void setHintTextColor(int color) {
        this.editMessage.setHintTextColor(color);
    }

    public void setInputType(int paramInt) {
        this.editMessage.setInputType(paramInt);
    }

    public void setMultiEditTextEnable(boolean enable) {
        if (enable) {
            this.editMessage.setSingleLine(false);
            this.editMessage.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        } else {
            this.editMessage.setSingleLine(true);
        }
    }

    public void setMinLength(int paramInt) {
        if (paramInt <= 0) {
            this.minLength = 0;
            return;
        }
        this.minLength = (paramInt - 1);
    }

    public void setMaxLenth(int paramInt) {
        if (paramInt <= 0) {
            this.maxLength = 0;
            return;
        }
        this.maxLength = (paramInt - 1);
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setFilterOnEnglish () {
        this.editMessage.setFilters(EditTextInputFilter.INSTANCE.getEditTextFilterOnEnglish());
    }

    public void setFilter () {
        this.editMessage.setFilters(EditTextInputFilter.INSTANCE.getEditTextFilter());
    }

    public abstract interface OnDoneClickListener {
        public abstract void onDoneClicked(String keywoard);
    }

    public abstract interface OnClearListener {
        public abstract void onClear(EditTextWithClearButtonWidget paramEditTextWithClearButtonWidget);
    }

    public abstract interface OnTextChangeListener {
        public abstract void onTextChanged(String message);
    }

    public void setOnDoneClickListener (OnDoneClickListener doneClickListener) {
        this.onDoneClickListener = doneClickListener;
    }

    public void setOnClearListener(OnClearListener paramOnClearListener) {
        this.listener = paramOnClearListener;
    }

    public void setOnTextChangedListener (OnTextChangeListener paramOnTextChangeListener) {
        this.textChangeListener = paramOnTextChangeListener;
    }

    public void setPasswordMode() {
        this.editMessage.setTransformationMethod(new PasswordTransformationMethod());
        setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        setPrivateImeOptions("defaultInputmode=english;");
    }

    public void setEnableButton(Button paramButton) {
        this.button = paramButton;
    }

    public Button getEnableButton() {
        return button;
    }

    public Button getBtnCompleate() {
        return btnCompleate;
    }

    public void setBtnCompleate(Button btnCompleate) {
        this.btnCompleate = btnCompleate;
    }

    public void setText(String paramString) {
        this.editMessage.setText(paramString);
    }

    public void setTextColor(int color) {
        this.editMessage.setTextColor(color);
    }

    /**
     * keyboard language option
     * @param option language ex) defaultInputmode=english;
     */
    public void setPrivateImeOptions(String option){
        this.editMessage.setPrivateImeOptions(option);
    }

    public void setImeOption() {
        this.editMessage.setSingleLine(true);
        this.editMessage.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public void setImeOptionSearch() {
        this.editMessage.setSingleLine(true);
        this.editMessage.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
    }

    /**
     * EditText Cursor Position
     */
    public void setSelection(String message){
        if (message != null && !TextUtils.isEmpty(message))
            this.editMessage.setSelection(message.length());
    }

    /**
     * Edit TextWatcher AdsSettings
     * @param textwacher textwatcher
     */
    public void setTextWatcher(TextWatcher textwacher){
        this.editMessage.addTextChangedListener(textwacher);
    }

    /**
     * editText 글자 수 제한
     * @param maxLength edittext의 사용 가능한 최대 글자 수
     */
    public void setTextFilters(int maxLength){
        this.editMessage.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
    }

    /**
     * 소개 수정 시 입력 창 사이즈.
     * @param context
     */
    public void setIntroduceHeight(Context context){
        Resources re = context.getResources();
        int heightPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 131, re.getDisplayMetrics());
        int paddingPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, re.getDisplayMetrics());
        editMessage.setMinHeight(heightPixels);
        editMessage.setPadding(paddingPixels, paddingPixels, paddingPixels, paddingPixels);
        editMessage.setGravity(Gravity.TOP);
    }

//	public void submitName (Context context, ConnectionListener.BaseMessageListener messageListener) {}

    public void notifyTextChanged (String message) {
        if (null != textChangeListener) {
            textChangeListener.onTextChanged(message);
        }
    }

    public void bottomLineVisiblity(int visible) {
        mBottomLine.setVisibility(visible);
    }

    public void setBottomLineBg(int resId) {
        mBottomLine.setBackgroundResource(resId);
    }

}