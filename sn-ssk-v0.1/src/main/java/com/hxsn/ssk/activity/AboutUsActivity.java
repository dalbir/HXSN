package com.hxsn.ssk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import com.hxsn.ssk.R;
import com.hxsn.ssk.base.BaseTitle;

public class AboutUsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        BaseTitle.getInstance(this).setTitle("关于我们");
        EditText edt;

    }

}
