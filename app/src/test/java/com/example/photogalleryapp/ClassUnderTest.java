package com.example.photogalleryapp;

import android.content.Context;

// This class is used for the mockito example unit test (MockitoTest)
public class ClassUnderTest {
    Context mContext;
    public ClassUnderTest(Context context) {
        mContext = context;
    }
    public String getHelloWorldString() {
        return mContext.getString(R.string.text_hello_word);
    }
}
