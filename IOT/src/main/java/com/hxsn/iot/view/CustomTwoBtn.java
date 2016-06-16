package com.hxsn.iot.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.ShedsControlInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;


public class CustomTwoBtn extends LinearLayout implements OnClickListener{
	private TextView open;
	private TextView close;
	private ShedsControlInterface inter;
	private Context context;
	private Timer timer;  
	private int tag;
	private String[] names;
	private String[] ids;
	public CustomTwoBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.sheds_cotrol_two_btn_layout,this);
		initView();
	}
	
	private void initView() {
		open = (TextView) findViewById(R.id.sheds_control_two_open);
		close = (TextView) findViewById(R.id.sheds_control_two_close);
		open.setOnClickListener(this);
		close.setOnClickListener(this);
	}
	
	Handler handler = new Handler(){  
		  
        public void handleMessage(Message msg) {  
        	
        	if (msg.what > 0) { 
        		if(tag == 1){
        			open.setText(msg.what+"");
        		} else {
        			close.setText(msg.what+"");
        		}
            } else { 
                // 结束Timer计时器  
                timer.cancel(); 
                if(tag == 1){
                	open.setText(names[0]);
            		open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
        			close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
            	} else {
            		close.setText(names[1]);
            		open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
        			close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
            	}
                Toast.makeText(getContext(), "停止执行", Toast.LENGTH_SHORT).show();
            } 
        	
        }  
          
    };  
    private class MyTimerTask extends TimerTask{
    	int i ;
    	public MyTimerTask(int i){
    		this.i = i;
    	}
  
        public void run() {  
            Message message = new Message();   
            message.what = i--;
            handler.sendMessage(message);    
        }  
    }; 

	@Override
	public void onClick(View v) {
		String[] str = inter.getValue();
		if(str[0]==null){
			Toast.makeText(getContext(), "请选择控制设备", Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		case R.id.sheds_control_two_open:
			timer = new Timer();  
			tag = 1;
			int timeo = string2int(str[2]);
			if(timeo != 0){
				timer.schedule(new MyTimerTask(timeo), 0, 1000);
				Toast.makeText(context, timeo+"秒后停止执行", Toast.LENGTH_SHORT).show();
			}
			ParseDatas parse = DataController.getShedsControl(str[1], ids[0], str[0], str[2]);
			ArrayList<HashMap<String, String>> list = showErrorCode(parse);
			
			refreshView(list);
			open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
			close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
			break;
		case R.id.sheds_control_two_close:
			timer = new Timer();  
			tag = 2;
			int timec = string2int(str[2]);
			if(timec != 0){
				timer.schedule(new MyTimerTask(timec), 0, 1000);  
				Toast.makeText(context, timec+"秒后执行", Toast.LENGTH_SHORT).show();
			}
			ParseDatas parse1 = DataController.getShedsControl(str[1], ids[1], str[0], str[2]);
			ArrayList<HashMap<String, String>> ls = showErrorCode(parse1);
			refreshView(ls);
			open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
			close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
			break;
		default:
			break;
		}
	}
	
	private ArrayList<HashMap<String, String>> showErrorCode(ParseDatas parse){
		ArrayList<HashMap<String, String>> list = null;
		if(parse != null){
			if(NetworkUtil.isErrorCode(parse.getCode(), context)){
				list = new ArrayList<HashMap<String,String>>();
			} else {
				list = parse.getList();
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), context);
			list = new ArrayList<HashMap<String,String>>();
		}
		return list;
	}
	
	private void refreshView(ArrayList<HashMap<String, String>> list){
		inter.refreshView(list);
	}
	
	private int string2int(String str){
		int intRet = 0;
        try {
            intRet = Integer.parseInt(str);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return intRet;
	}
	
	public void initalView(ArrayList<HashMap<String,String>> hashMap){
		names = new String[hashMap.size()];
		ids = new String[hashMap.size()];
		for (int i = 0; i < hashMap.size(); i++) {
			names[i] = hashMap.get(i).get("name");
			ids[i] = hashMap.get(i).get("id");
		}
		open.setText(names[0]);
		close.setText(names[1]);
		open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
		close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
	}
	
	public void setContext(ShedsControlInterface inter) {
		this.inter = inter;
	}

}
