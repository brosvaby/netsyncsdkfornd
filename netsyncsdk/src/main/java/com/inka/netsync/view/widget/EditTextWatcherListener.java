package com.inka.netsync.view.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.inka.netsync.logs.LogUtil;

public class EditTextWatcherListener implements TextWatcher {
	private EditTextWithClearButtonWidget editTextWithClearButtonWidget = null;
	
	public EditTextWatcherListener(EditTextWithClearButtonWidget paramEditTextWithClearButtonWidget) {
		this.editTextWithClearButtonWidget = paramEditTextWithClearButtonWidget;
	}
	
	@Override
	public void afterTextChanged(Editable paramEditable) {}

	@Override
	public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {}

	@Override
	public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3) {
		int length = paramCharSequence.length();
		try {
			checkLength(length);
			if (length > 0) {
				setVisibleClearBtn(View.VISIBLE);
			} else {
				setVisibleClearBtn(View.INVISIBLE);
			}
			editTextWithClearButtonWidget.notifyTextChanged(paramCharSequence.toString());
			setCompleateEnable(false);
		} catch (Exception e){
			LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
		}
	}

	public void checkLength (int inputLength) {
		int minLength = editTextWithClearButtonWidget.getMinLength();
		int maxLength = editTextWithClearButtonWidget.getMaxLength();
		
		if (inputLength >= minLength && inputLength <= maxLength) {
			setEnableBtn(true);
			setVisibleClearBtn(View.VISIBLE);
		} else {
			setEnableBtn(false);
			setVisibleClearBtn(View.INVISIBLE);
		}
	}
	
	public void setEnableBtn (boolean state) {
		Button enableButton = editTextWithClearButtonWidget.getEnableButton();
		if (enableButton != null) {
			enableButton.setEnabled(state);
		}
	}
	
	public void setCompleateEnable(boolean state) {
		Button enableCompleateButton = editTextWithClearButtonWidget.getBtnCompleate();
		if (enableCompleateButton != null) {
			enableCompleateButton.setEnabled(state);
		}
	}
	
	public void setVisibleClearBtn (int visible) {
		ImageView clear = editTextWithClearButtonWidget.getImageView();
		if (clear != null) {
			clear.setVisibility(visible);
		}
	}
	
}
