package com.xphonesoftware.fibscroller;

import android.app.Application;

/**
 * Application class used to maintain global app state
 *
 * Created by davidmedina on 8/1/15.
 */
public class FibApplication extends Application {

    private ScrollableFibGen mScrollableFibGen;

    @Override
    public void onCreate() {
        super.onCreate();

        // create a scrollable fibonacci generator and store
        //  it here so that it is available across config
        //  changes (e.g. rotation)
        mScrollableFibGen = new ScrollableFibGen();
    }

    public ScrollableFibGen getScrollableFibGen() {
        return mScrollableFibGen;
    }
}
