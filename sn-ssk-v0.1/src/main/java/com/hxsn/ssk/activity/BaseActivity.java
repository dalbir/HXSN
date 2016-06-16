package com.hxsn.ssk.activity;

import android.app.Activity;
import android.os.Bundle;

import com.hxsn.ssk.TApplication;


/**
 * @description
 */
public class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStop() {
        TApplication.requestQueue.cancelAll(this);
        super.onStop();
    }
}
