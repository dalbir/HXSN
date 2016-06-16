package com.hxsn.ssk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxsn.ssk.R;
import com.hxsn.ssk.TApplication;
import com.hxsn.ssk.utils.DataUtil;
import com.hxsn.ssk.utils.DebugUtil;

public class SettingActivity extends Activity implements View.OnClickListener {

    private RelativeLayout rl2, rl3, rl4, rl5, rl6;
    private TextView txtUsername, txtNickname, txtRealname, txtEmail, txtPhone, txtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        TApplication.activities.add(this);

        addView();
        addListener();
    }

    private void addListener() {
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        rl6.setOnClickListener(this);
    }

    private void addView() {
        rl2 = (RelativeLayout) findViewById(R.id.rl_2);
        rl3 = (RelativeLayout) findViewById(R.id.rl_3);
        rl4 = (RelativeLayout) findViewById(R.id.rl_4);
        rl5 = (RelativeLayout) findViewById(R.id.rl_5);
        rl6 = (RelativeLayout) findViewById(R.id.rl_6);
        txtUsername = (TextView) findViewById(R.id.txt_username);
        txtAddress = (TextView) findViewById(R.id.txt_address);
        txtEmail = (TextView) findViewById(R.id.txt_email);
        txtNickname = (TextView) findViewById(R.id.txt_nickname);
        txtPhone = (TextView) findViewById(R.id.txt_phone);
        txtRealname = (TextView) findViewById(R.id.txt_realname);

        txtUsername.setText(TApplication.user.getName());
        txtRealname.setText(TApplication.user.getRealName());
        txtPhone.setText(TApplication.user.getPhone());
        String nickName = TApplication.user.getNickName();
        nickName = DataUtil.toHexString(nickName);
        DebugUtil.d("字符转换", nickName);
        txtNickname.setText(TApplication.user.getNickName());
        txtEmail.setText(TApplication.user.getEmail());
        txtAddress.setText(TApplication.user.getAddress());

        ImageView imgBack = (ImageView) findViewById(R.id.img_left);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DebugUtil.d("SettingActivity onBackPressed");
                onBackPressed();
            }
        });
        TextView txtTitle = (TextView) findViewById(R.id.txt_middle);
        txtTitle.setText("个人设置");
    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(this, SetTextActivity.class);

        int mode;
        switch (v.getId()) {
            case R.id.rl_2:

                mode = 2;
                intent.putExtra("title", mode);
                startActivity(intent);
                break;
            case R.id.rl_3:
                String realName = txtRealname.getText().toString();
                if (realName.length() != 0) {
                    mode = 3;
                    intent.putExtra("title", mode);
                    startActivity(intent);
                }
                break;
            case R.id.rl_4:
                mode = 4;
                intent.putExtra("title", mode);
                startActivity(intent);
                break;
            case R.id.rl_5:
                mode = 5;
                intent.putExtra("title", mode);
                startActivity(intent);
                break;
            case R.id.rl_6:
                mode = 6;
                intent.putExtra("title", mode);
                startActivity(intent);
                break;
        }
    }
}
