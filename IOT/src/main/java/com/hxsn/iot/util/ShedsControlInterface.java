package com.hxsn.iot.util;

import java.util.ArrayList;
import java.util.HashMap;

import android.widget.BaseAdapter;

public interface ShedsControlInterface {
	public String[] getValue();
	public void refreshView(ArrayList<HashMap<String, String>> list);
}
