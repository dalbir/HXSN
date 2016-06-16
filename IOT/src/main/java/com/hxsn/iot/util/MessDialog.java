package com.hxsn.iot.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hxsn.iot.R;

public class MessDialog {

        public static void show(String text,Context context){
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                AlertDialog dialog = b.create();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.mess_dialog, null);
                TextView tv = (TextView) view.findViewById(R.id.tv_mess);
                tv.setText(text);
                dialog.setView(view);
                dialog.show();
        }

        public static void show1(String text,Context context){
                AlertDialog.Builder b = new AlertDialog.Builder(context);
                AlertDialog dialog = b.create();
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.dialog_01, null);
                TextView tv = (TextView) view.findViewById(R.id.tv_mess);
                tv.setText(text);
                dialog.setView(view,0,0,0,0);
                dialog.show();
        }
}
