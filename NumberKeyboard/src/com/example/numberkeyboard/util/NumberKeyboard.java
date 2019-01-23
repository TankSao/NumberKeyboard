package com.example.numberkeyboard.util;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class NumberKeyboard {
	private KeyboardView mKeyboardView;
	private Activity mActivity;
	private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener()
    {

        public final static int CodeDelete = -5; //É¾³ý
        public final static int CodeCancel = -3; //È¡Ïû
        public final static int CodeHide = -6; //Òþ²Ø

        @Override
        public void onKey(int primaryCode, int[] keyCodes)
        {
            View focusCurrent = mActivity.getWindow().getCurrentFocus();
            if (focusCurrent == null
                    || focusCurrent.getClass() != EditText.class)
                return;
            EditText edittext = (EditText) focusCurrent;
            Editable editable = edittext.getText();
            int start = edittext.getSelectionStart();
            if (primaryCode == CodeCancel)
            {
                hideKeyboard();
            }
            else if (primaryCode == CodeDelete)
            {
                if (editable != null && start > 0)
                    editable.delete(start - 1, start);
            }else if (primaryCode == CodeHide)
            {
            	hideKeyboard();
            }
            else
            { // insert character
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }

        @Override
        public void onPress(int arg0)
        {
        }

        @Override
        public void onRelease(int primaryCode)
        {
        }

        @Override
        public void onText(CharSequence text)
        {
        }

        @Override
        public void swipeDown()
        {
        }

        @Override
        public void swipeLeft()
        {
        }

        @Override
        public void swipeRight()
        {
        }

        @Override
        public void swipeUp()
        {
        }
    };
    
    public NumberKeyboard(Activity act, int viewid, int layoutid)
    {
        mActivity = act;
        mKeyboardView = (KeyboardView) mActivity.findViewById(viewid);
        mKeyboardView.setKeyboard(new Keyboard(mActivity, layoutid));
        mKeyboardView.setPreviewEnabled(false); 
        mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);
        //ÆÁ±ÎÏµÍ³¼üÅÌ
        mActivity.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //ÅÐ¶Ï¼üÅÌÊÇ·ñµ¯³ö
    public boolean isKeyboardVisible()
    {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    //ÏÔÊ¾¼üÅÌ
    public void showKeyboard(View v)
    {
        mKeyboardView.setVisibility(View.VISIBLE);
        mKeyboardView.setEnabled(true);
        if (v != null)
            ((InputMethodManager) mActivity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    //Òþ²Ø¼üÅÌ
    public void hideKeyboard()
    {
        mKeyboardView.setVisibility(View.GONE);
        mKeyboardView.setEnabled(false);
    }
    
    //°ó¶¨ÊäÈë¿ò
    public void bindEditText(int resid)
    {
        EditText edittext = (EditText) mActivity.findViewById(resid);
        edittext.setOnFocusChangeListener(new OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (hasFocus)
                    showKeyboard(v);
                else
                    hideKeyboard();
            }
        });
        edittext.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showKeyboard(v);
            }
        });
        edittext.setOnTouchListener(new OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                EditText edittext = (EditText) v;
                int inType = edittext.getInputType();
                edittext.setInputType(InputType.TYPE_NULL); 
                edittext.onTouchEvent(event); 
                edittext.setInputType(inType); 
                return true; 
            }
        });
        edittext.setInputType(edittext.getInputType()
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    
    
}
