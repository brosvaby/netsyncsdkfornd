package com.inka.netsync.view.widget;

import android.text.InputFilter;
import android.text.Spanned;

import com.inka.netsync.logs.LogUtil;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;


public enum EditTextInputFilter {
	INSTANCE;
	
	private final String TAG = "EditTextInputFilter";
	
	public InputFilter[] getEditTextFilter () {
		InputFilter[] InputFilter = null;
		try {
			InputFilter = new InputFilter[] {new TextCommonFilter()};
		} catch (Exception e) {
			LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
		}
		return InputFilter;
	}
	
	public InputFilter[] getEditTextFilterOnEnglish () {
		InputFilter[] InputFilter = null;
		try {
			InputFilter = new InputFilter[] {new TextEnglishFilter()};
		} catch (Exception e) {
			LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
		}
		return InputFilter;
	}
	
	public InputFilter[] getEditTextFilterOnEmail () {
		InputFilter[] InputFilter = null;
		
		try {
			InputFilter = new InputFilter[] { new TextEmailFilter()};
		} catch (Exception e) {
			LogUtil.INSTANCE.error(getClass().getSimpleName(), e);
		}
		return InputFilter;
	}
	/**
	 * filter [Email - English, number, "@", "-", "_" , "." ]
	 * @author user
	 *
	 */
	class TextEmailFilter implements InputFilter {
		@Override
		public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
			Pattern ps = Pattern.compile("^[a-zA-Z0-9@_.-]+$");
			if (!ps.matcher(source).matches()) {
				LogUtil.INSTANCE.info(TAG, "source :" + source);
				return "";
			} 
			return null;
		}
	}
	
	/**
	 * filter [korean]
	 * @author pc
	 *
	 */
	class TextKorFilter implements InputFilter {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
			Pattern ps = Pattern.compile("^[ㄱ-ㅎ가-힣]+$");
	        if (!ps.matcher(source).matches()) {
	        	LogUtil.INSTANCE.info(TAG, "source :" + source);
	        	return "";
	        } 
			return null;
		}
	}
	
	/**
	 * filter [english, number]
	 * @author pc
	 *
	 */
	class TextEnglishFilter implements InputFilter {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
			Pattern ps = Pattern.compile("^[a-zA-Z0-9]+$");
			if (!ps.matcher(source).matches()) {
				LogUtil.INSTANCE.info(TAG, "source :" + source + ", start :" +start + " , end :" + end + " , dest :" + dest + " , dstart :" + dstart + " , dend :" + dend);
				return "";
			}
			return null;
		}
	}
	
	/**
	 * filter [korean, english, number]
	 * @author pc
	 *
	 */
	class TextCommonFilter implements InputFilter {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
			Pattern ps = Pattern.compile("^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9]+$");
			if (!ps.matcher(source).matches()) {
				LogUtil.INSTANCE.info(TAG, "source :" + source);
				return "";
			}
			return null;
		}
	}

	
	/**
	 * EditText 등의 필드에 텍스트 입력/수정시 
	 * 입력문자열의 바이트 길이를 체크하여 입력을 제한하는 필터.
	 *
	 */
	public class ByteLengthFilter implements InputFilter {
	    private String mCharset; //인코딩 문자셋
	    protected int mMaxByte; // 입력가능한 최대 바이트 길이

	    public ByteLengthFilter(int maxbyte, String charset) {
	        this.mMaxByte = maxbyte;
	        this.mCharset = charset;
	    }
	    /**
	     * 이 메소드는 입력/삭제 및 붙여넣기/잘라내기할 때마다 실행된다.
	     *
	     * - source : 새로 입력/붙여넣기 되는 문자열(삭제/잘라내기 시에는 "")
	     * - dest : 변경 전 원래 문자열
	     */
	    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                   int dend) {

	        // 변경 후 예상되는 문자열
	        String expected = new String();
	        expected += dest.subSequence(0, dstart);
	        expected += source.subSequence(start, end);
	        expected += dest.subSequence(dend, dest.length());

	        int keep = calculateMaxLength(expected) - (dest.length() - (dend - dstart));

	        if (keep <= 0) {
	            return ""; // source 입력 불가(원래 문자열 변경 없음)
	        } else if (keep >= end - start) {
	            return null; // keep original. source 그대로 허용
	        } else {
	            return source.subSequence(start, start + keep); // source중 일부만 입력 허용
	        }
	    }

	    /**
	     * 입력가능한 최대 문자 길이(최대 바이트 길이와 다름!).
	     */
	    protected int calculateMaxLength(String expected) {
	        return mMaxByte - (getByteLength(expected) - expected.length());
	    }    
	    
	    /**
	     * 문자열의 바이트 길이.
	     * 인코딩 문자셋에 따라 바이트 길이 달라짐.
	     * @param str
	     * @return
	     */
	    private int getByteLength(String str) {
	        try {
	            return str.getBytes(mCharset).length;
	        } catch (UnsupportedEncodingException e) {
	            //e.printStackTrace();
	        }
	        return 0;
	    }
	}
}