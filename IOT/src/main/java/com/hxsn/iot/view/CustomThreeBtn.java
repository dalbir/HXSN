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


public class CustomThreeBtn extends LinearLayout implements OnClickListener{
	private TextView open;
	private TextView close;
	private TextView stop;
	private ShedsControlInterface inter;
	private Context context;
	private Timer timer; 
	private int tag;
	private String[] names;
	private String[] ids;
	public CustomThreeBtn(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		timer = new Timer(); 
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.sheds_cotrol_three_btn_layout,this);
		initView();
	}
	
	private void initView() {
		open = (TextView) findViewById(R.id.sheds_control_three_open);
		close = (TextView) findViewById(R.id.sheds_control_three_close);
		stop = (TextView) findViewById(R.id.sheds_control_three_stop);
		open.setOnClickListener(this);
		close.setOnClickListener(this);
		stop.setOnClickListener(this);
	}
	
	Handler handler = new Handler(){  
		  
        public void handleMessage(Message msg) {  
        	
        	if (msg.what > 0) { 
        		if(tag == 1){
        			open.setText(msg.what+"");
        		} else if(tag == 2){
        			close.setText(msg.what+"");
        		} else {
        			stop.setText(msg.what+"");
        		}
            } else {
            	timer.cancel();
            	open.setText(names[0]);
            	close.setText(names[1]);
	        	Toast.makeText(context, "停止执行", Toast.LENGTH_SHORT).show();
	        	
				open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
				close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
				stop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
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
			Toast.makeText(getContext(), "请选择大棚", Toast.LENGTH_SHORT).show();
			return;
		}
		switch (v.getId()) {
		case R.id.sheds_control_three_open:
			timer = new Timer(); 
			tag = 1;
			int timeo = string2int(str[2]);
			if(timeo != 0){
				timer.schedule(new MyTimerTask(timeo), 0, 1000);  
				Toast.makeText(context, timeo+"秒后停止执行", Toast.LENGTH_SHORT).show();
			}
			ParseDatas parse = DataController.getShedsControl(str[1], ids[0], str[0], str[2]);
			ArrayList<HashMap<String, String>> listo = showErrorCode(parse);
			
			refreshView(listo);
			open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
			close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
			stop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
			break;
		case R.id.sheds_control_three_close:
			timer = new Timer(); 
			tag = 2;
			int timec = string2int(str[2]);
			if(timec != 0){
				timer.schedule(new MyTimerTask(timec), 0, 1000);  
				Toast.makeText(context, timec+"秒后停止执行", Toast.LENGTH_SHORT).show();
			}
			ParseDatas parse1 = DataController.getShedsControl(str[1], ids[1], str[0], str[2]);
			ArrayList<HashMap<String, String>> listc = showErrorCode(parse1);
			
			refreshView(listc);
			open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
			close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
			stop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
			break;
		case R.id.sheds_control_three_stop:
			timer = new Timer(); 
			tag = 3;
			int times = string2int(str[2]);
			if(times != 0){
				timer.schedule(new MyTimerTask(times), 0, 1000);  
				Toast.makeText(context, times+"秒后停止执行", Toast.LENGTH_SHORT).show();
			}
			ParseDatas parse2 = DataController.getShedsControl(str[1], ids[2], str[0], str[2]);
			ArrayList<HashMap<String, String>> lists = showErrorCode(parse2);
			
			refreshView(lists);
			open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
			close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
			stop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
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
		stop.setText(names[2]);
		open.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
		close.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_default));
		stop.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sheds_press));
	}
	
	public void setContext(ShedsControlInterface inter) {
		this.inter = inter;
	}

}
