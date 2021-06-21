package com.example.news.util;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {
    private static AppContext mInstance;

    public static Context getInstance(){
        if (mInstance == null) {
            mInstance = new AppContext();
        }
        return mInstance;
    }
}
