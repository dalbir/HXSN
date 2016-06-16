package com.hxsn.ssk.fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hxsn.ssk.R;
import com.hxsn.ssk.activity.WenActivity;
import com.hxsn.ssk.base.MyBaseAdapter;
import com.hxsn.ssk.beans.Topic;


/**
 * A simple {@link Fragment} subclass.  随时看
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * create an instance of this fragment.
 */
@SuppressLint("ValidFragment")
public class Wen3Fragment2 extends Fragment implements View.OnClickListener {

    private Context context;
    private TextView txtLeft, txtRight, txtDot1, txtDot2;
    private View vLine1, vLine2;
    private RelativeLayout rlLeft, rlRight;
    private ListView listView;
    private ThisAdapter adapter;

    public Wen3Fragment2() {
    }

    public Wen3Fragment2(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("JavascriptInterface")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wen3_2, container, false);
        addView(view);
        return view;
    }

    private void addView(View view) {
        txtDot1 = (TextView) view.findViewById(R.id.txt_dot1);
        txtDot2 = (TextView) view.findViewById(R.id.txt_dot2);
        txtLeft = (TextView) view.findViewById(R.id.txt_left);
        txtRight = (TextView) view.findViewById(R.id.txt_right);
        rlLeft = (RelativeLayout) view.findViewById(R.id.rl_left);
        rlRight = (RelativeLayout) view.findViewById(R.id.rl_right);
        vLine1 = view.findViewById(R.id.view_line1);
        vLine2 = view.findViewById(R.id.view_line2);

        listView = (ListView) view.findViewById(R.id.list);
        adapter = new ThisAdapter(context);
        listView.setAdapter(adapter);
        listView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, WenActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_left:
                txtDot1.setVisibility(View.VISIBLE);
                txtDot2.setVisibility(View.INVISIBLE);
                vLine1.setVisibility(View.VISIBLE);
                vLine2.setVisibility(View.INVISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.green_none));
                txtRight.setTextColor(getResources().getColor(R.color.black_text_n));
                break;
            case R.id.rl_right:
                txtDot1.setVisibility(View.INVISIBLE);
                txtDot2.setVisibility(View.VISIBLE);
                vLine1.setVisibility(View.INVISIBLE);
                vLine2.setVisibility(View.VISIBLE);
                txtLeft.setTextColor(getResources().getColor(R.color.black_text_n));
                txtRight.setTextColor(getResources().getColor(R.color.green_none));
                break;
        }
    }

    //     ViewHolder 模式, 效率提高 50%
    static class ViewHolder {

    }

    class ThisAdapter extends MyBaseAdapter<Topic> {
        ViewHolder holder;

        public ThisAdapter(Context context) {
            super(context);
        }

        @Override
        public View getMyView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_wen, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }
    }
}
