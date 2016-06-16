package com.hxsn.ssk.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxsn.ssk.R;
import com.hxsn.ssk.TApplication;
import com.hxsn.ssk.activity.AboutUsActivity;
import com.hxsn.ssk.activity.LoginActivity;
import com.hxsn.ssk.activity.PasswordEditActivity;
import com.hxsn.ssk.activity.SettingActivity;
import com.hxsn.ssk.db.UserService;
import com.hxsn.ssk.utils.AndroidUtil;
import com.hxsn.ssk.utils.Const;


/**
 * A simple {@link Fragment} subclass.  随时看
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */

@SuppressLint("ValidFragment")
public class Mine5Fragment extends Fragment implements View.OnClickListener {

    private Context context;
    private TextView txt2, txt3, txt4, txt5, txtName, txtVersion;


    public Mine5Fragment() {
    }

    public Mine5Fragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine5, container, false);

        addView(view);
        addListener();
        return view;
    }

    private void addListener() {
        txt2.setOnClickListener(this);
        txt3.setOnClickListener(this);
        txt4.setOnClickListener(this);
        txt5.setOnClickListener(this);

    }

    private void addView(View v) {
        txt2 = (TextView) v.findViewById(R.id.txt2);
        txt3 = (TextView) v.findViewById(R.id.txt3);
        txt4 = (TextView) v.findViewById(R.id.txt4);
        txt5 = (TextView) v.findViewById(R.id.txt5);
        txtVersion = (TextView) v.findViewById(R.id.txt_version);
        String version = "ssk_v" + AndroidUtil.getThisAppVersion();
        switch (TApplication.versionType) {
            case Const.TEST_VERTION:
                version += "T";
                break;
            case Const.TEST_VERTION1:
                version += "T1";
                break;
            case Const.DEBUG_VERTION:
                version += "D";
                break;
            case Const.RELEASE_VERTION:
                version += "R";
                break;
        }
        txtVersion.setText(version);
        txtName = (TextView) v.findViewById(R.id.txt_name);
        txtName.setText(TApplication.user.getName());
    }


    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.txt2:
                intent.setClass(context, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.txt3:
                intent.setClass(context, PasswordEditActivity.class);
                startActivity(intent);
                break;
            case R.id.txt4:
                intent.setClass(context, AboutUsActivity.class);
                startActivity(intent);
                break;
            case R.id.txt5:
                TApplication.user = null;
                UserService.getInstance().clear();
                //intent.setClass(context, NewsActivity.class);
                intent.setClass(context, LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}
