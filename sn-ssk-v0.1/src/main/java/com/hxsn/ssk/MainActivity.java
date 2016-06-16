package com.hxsn.ssk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.hxsn.ssk.activity.HomeActivity;
import com.hxsn.ssk.activity.LoginActivity;
import com.hxsn.ssk.activity.WelcomeActivity;
import com.hxsn.ssk.beans.User;
import com.hxsn.ssk.db.DictionaryService;
import com.hxsn.ssk.db.UserService;
import com.hxsn.ssk.utils.Const;
import com.hxsn.ssk.utils.DebugUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends Activity {

    DictionaryService dictionaryService = DictionaryService.getInstanse(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, "jAvXSGRoxPzaUBSwaMSNG3gm");


        Intent intent = new Intent();
        String value = dictionaryService.getValue("welcome");
        if (value == null) {
            dictionaryService.addValue("welcome", "welcome");
            intent.setClass(this, WelcomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (TApplication.user == null) {
                TApplication.user = UserService.getInstance().getUser();
                User user = TApplication.user;
                if (user == null) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    DebugUtil.d("跳转HomeActivity userid="+user.getId());
                    intent.setClass(this, HomeActivity.class);
                }
            } else {
                intent.setClass(this, HomeActivity.class);
                startActivity(intent);
            }
            startActivity(intent);
            finish();
        }

    }



}
