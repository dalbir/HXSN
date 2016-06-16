package com.hxsn.iot.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_CLIENTINFO;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_IPPARACFG_V40;
import com.hikvision.netsdk.PTZCommand;
import com.hikvision.netsdk.RealPlayCallBack;
import com.hxsn.iot.AbsApplication;
import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.model.VideoData;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.view.wheelview.ArrayWheelAdapter;
import com.hxsn.iot.view.wheelview.OnWheelChangedListener;
import com.hxsn.iot.view.wheelview.WheelView;

import org.MediaPlayer.PlayM4.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("LongLogTag")
public class VideoSurveillanceFragment extends AbsBaseFgt implements Callback{
	
	private Button mConfigureBtn;
	private WheelView mChangeBtn;
//	private Button mPauseBtn;
	private Button mStartBtn;
	private SurfaceView mSurfaceView;
	
	private LinearLayout mConfigureLayout;
	private EditText mIPAddressEt;
	private EditText mPortEt;
	private EditText mUserEt;
	private EditText mPwdEt;
	private Button mLoginBtn;
	
	private LinearLayout mVideoControlLayout;
	private Button mVideoControlUpBtn;
	private Button mVideoControlDownBtn;
	private Button mVideoControlLeftBtn;
	private Button mVideoControlRightBtn;
	private Button mVideoControlUpLeftBtn;
	private Button mVideoControlDownLeftBtn;
	private Button mVideoControlUpRightBtn;
	private Button mVideoControlDownRightBtn;
	
	private ControlBtnListener mControlListener;
	
	private Player m_oPlayerSDK;
	
	private NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
	
	private	int iFirstChannelNo = -1;				// get start channel no
	private int m_iLogID = -1;				// return by NET_DVR_Login_v30
	private int m_iPlayID = -1;				// return by NET_DVR_RealPlay_V30
	private int m_iPlaybackID = -1;				// return by NET_DVR_PlayBackByTime	
	private int m_iPort = -1;				// play port
	private int m_iPauseID = -1;
	
	private boolean mControlTag = false;
	
	private final String TAG = "VideoSurveillanceFragment";
	
	private View view;
	
	private String ip = "";
	private String port = "";
	private String user = "";
	private String password = "";
	private ArrayList<HashMap<String,String>> mChannelMap = null;
	private int postionChan;//通道数
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.video_surveillance_layout, container, false);
		initData();
		initConf();
		return view;
	}
	
	private void initData(){
		String id = AbsApplication.getInstance().getDapeng().getId();
		VideoData data = DataController.getVideoData(id);
		getErrorCode(data);
	}
	
	private void getErrorCode(VideoData data){
    	if(data != null){
			if(NetworkUtil.isErrorCode(data.getCode(), getActivity())){
			} else {
				ip = data.getIp();
				port = data.getPort();
				user = data.getUser();
				password = data.getPassword();
				mChannelMap = data.getList();
				if(mChannelMap==null){
					HashMap<String,String> map = new HashMap<String,String>();
					map.put("note", "通道一");
					map.put("channel", "0");
					mChannelMap = new ArrayList<HashMap<String,String>>();
					mChannelMap.add(map);
				}
				if(ip==null){ip="";}
				if(port==null){port="8000";};
				if(user==null){user="";};
				if(password==null){password="";};
			}
		} else {
			MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
		}
    }
	
	private void initConf(){
		if (!initeSdk()){
        	return;
        }
        
        if (!initeActivity()){
        	return;
        }
        LoginMethod();
        perviewMethod(1);//开始显示第一个通道
	}
	
	private void initView() {
		mConfigureBtn = (Button) view.findViewById(R.id.video_configure_button);
		mChangeBtn = (WheelView) view.findViewById(R.id.video_change_button);
//		mPauseBtn = (Button) view.findViewById(R.id.video_pause_button);
		mStartBtn = (Button) view.findViewById(R.id.video_start_button);
		mConfigureLayout = (LinearLayout) view.findViewById(R.id.video_configure_layout);
		mSurfaceView = (SurfaceView) view.findViewById(R.id.Sur_Player);
		
		mIPAddressEt = (EditText) view.findViewById(R.id.video_configure_ip);
		mPortEt = (EditText) view.findViewById(R.id.video_configure_port);
		mUserEt = (EditText) view.findViewById(R.id.video_configure_user);
		mPwdEt = (EditText) view.findViewById(R.id.video_configure_pwd);
		mLoginBtn = (Button) view.findViewById(R.id.video_configure_login);
		
		mVideoControlLayout = (LinearLayout) view.findViewById(R.id.video_control_layout);
		mVideoControlUpBtn = (Button) view.findViewById(R.id.video_control_up);
		mVideoControlDownBtn = (Button) view.findViewById(R.id.video_control_down);
		mVideoControlLeftBtn = (Button) view.findViewById(R.id.video_control_left);
		mVideoControlRightBtn = (Button) view.findViewById(R.id.video_control_right);
		mVideoControlUpLeftBtn = (Button) view.findViewById(R.id.video_control_upleft);
		mVideoControlDownLeftBtn = (Button) view.findViewById(R.id.video_control_downleft);
		mVideoControlUpRightBtn = (Button) view.findViewById(R.id.video_control_upright);
		mVideoControlDownRightBtn = (Button) view.findViewById(R.id.video_control_downright);
		
		mControlListener = new ControlBtnListener();
		
		mIPAddressEt.setText(ip);
		mPortEt.setText(port);
		mUserEt.setText(user);
		mPwdEt.setText(password);
	}
	
	private void setListeners() {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < mChannelMap.size(); i++) {
			list.add(mChannelMap.get(i).get("note"));
		}
		mChangeBtn.setVisibleItems(3);//设置当前显示的条数，不是全部而是可视条数
		mChangeBtn.setAdapter(new ArrayWheelAdapter(list));
		mChangeBtn.addChangingListener(new ProListener());
		mChangeBtn.setCurrentItem(0);
		
		mConfigureBtn.setOnClickListener(Configure_Listener);
		mLoginBtn.setOnClickListener(Login_Listener);
		mStartBtn.setOnClickListener(Preview_Listener);
//		mPauseBtn.setOnClickListener(Pause_Listener);
		
		mVideoControlUpBtn.setOnClickListener(mControlListener);
		mVideoControlDownBtn.setOnClickListener(mControlListener);
		mVideoControlLeftBtn.setOnClickListener(mControlListener);
		mVideoControlRightBtn.setOnClickListener(mControlListener);
		
		mVideoControlUpBtn.setOnTouchListener(mControlListener);
		mVideoControlDownBtn.setOnTouchListener(mControlListener);
		mVideoControlLeftBtn.setOnTouchListener(mControlListener);
		mVideoControlRightBtn.setOnTouchListener(mControlListener);
		
		mVideoControlUpLeftBtn.setOnClickListener(mControlListener);
		mVideoControlDownLeftBtn.setOnClickListener(mControlListener);
		mVideoControlUpRightBtn.setOnClickListener(mControlListener);
		mVideoControlDownRightBtn.setOnClickListener(mControlListener);
		
		mVideoControlUpLeftBtn.setOnTouchListener(mControlListener);
		mVideoControlDownLeftBtn.setOnTouchListener(mControlListener);
		mVideoControlUpRightBtn.setOnTouchListener(mControlListener);
		mVideoControlDownRightBtn.setOnTouchListener(mControlListener);
	}
	
	private class ProListener implements OnWheelChangedListener {
		@Override
		public void onChanged(WheelView wheel, int oldValue, int newValue) {
			postionChan = newValue+1;
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.i(TAG, "surface is created" + m_iPort); 
		if (-1 == m_iPort){
			return;
		}
        Surface surface = holder.getSurface();
        if (null != m_oPlayerSDK && true == surface.isValid()) {
        	if (false == m_oPlayerSDK.setVideoWindow(m_iPort, 0, holder)) {	
        		Log.e(TAG, "Player setVideoWindow failed!");
        	}	
    	}   
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
		if (-1 == m_iPort){
			return;
		}
        if (null != m_oPlayerSDK && true == holder.getSurface().isValid()) {
        	if (false == m_oPlayerSDK.setVideoWindow(m_iPort, 0, null)) {	
        		Log.e(TAG, "Player setVideoWindow failed!");
        	}
        }
	}
	
//	@Override  
//	protected void onSaveInstanceState(Bundle outState) {    
//		outState.putInt("m_iPort", m_iPort);  
//		super.onSaveInstanceState(outState);  
//		Log.i(TAG, "onSaveInstanceState"); 
//	}  
//	
//    
//	@Override  
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {  
//		m_iPort = savedInstanceState.getInt("m_iPort");  
//	//	m_oPlayerSDK = Player.getInstance();
//		super.onRestoreInstanceState(savedInstanceState);  
//		Log.i(TAG, "onRestoreInstanceState" ); 
//	}  
	
	private boolean initeSdk()
	{
		//init net sdk
    	if (!HCNetSDK.getInstance().NET_DVR_Init())
    	{
    		Log.e(TAG, "HCNetSDK init is failed!");
    		return false;
    	}
    	HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/",true);
    	
    	// init player
    	m_oPlayerSDK = Player.getInstance();
    	if (m_oPlayerSDK == null)
    	{
    		Log.e(TAG,"PlayCtrl getInstance failed!");
    		return false;
    	}
    	
    	return true;
	}
	
	private boolean initeActivity()
    {
		initView();
    	
		mSurfaceView.getHolder().addCallback(this);
    	setListeners();
    	return true;
    }
	
	//配置按钮事件
	private OnClickListener Configure_Listener = new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(mConfigureLayout.getVisibility() == View.VISIBLE){
				mConfigureLayout.setVisibility(View.GONE);
			} else {
				mConfigureLayout.setVisibility(View.VISIBLE);
			}
		}
		
	};
	
	//根据用户名密码登陆
	private OnClickListener Login_Listener = new OnClickListener() {
		public void onClick(View v) {
			try{
				if(m_iLogID < 0){
					// login on the device
					LoginMethod();
					mConfigureLayout.setVisibility(View.GONE);
				}
				else{
					// whether we have logout
					if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(m_iLogID)){
						Log.e(TAG, " NET_DVR_Logout is failed!");
						return;
					}
					mLoginBtn.setText("登陆");
					m_iLogID = -1;
				}		
			} 
			catch (Exception err){
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	
	private void LoginMethod(){
		m_iLogID = loginDevice();
		if (m_iLogID < 0){
			Log.e(TAG, "This device logins failed!");
			Toast.makeText(getActivity(), "登录服务器失败，请检查配置参数", Toast.LENGTH_LONG).show();
			return;
		}
		// get instance of exception callback and set
		ExceptionCallBack oexceptionCbf = getExceptiongCbf();
		if (oexceptionCbf == null){
		    Log.e(TAG, "ExceptionCallBack object is failed!");
		    return ;
		}
		
		if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf)){
	        Log.e(TAG, "NET_DVR_SetExceptionCallBack is failed!");
	        return;
	    }
		
		mLoginBtn.setText("注销");
		Log.i(TAG, "Login sucess ****************************1***************************");

	}
	
	//显示surfaceview
	private OnClickListener Preview_Listener = new OnClickListener() {
		public void onClick(View v) {
			try{
				if(m_iLogID < 0){
					Log.e(TAG,"please login on device first");
					return ;
				}
				if(m_iPlayID < 0){	
					perviewMethod(postionChan);
				}else{
					stopPlay();
					mStartBtn.setText("开始");
//					mPauseBtn.setEnabled(false);
//					mPauseBtn.setText("暂停");
					mLoginBtn.setEnabled(true);
				}				
			} catch (Exception err){
				Log.e(TAG, "error: " + err.toString());
			}
		}
	};
	
	private void perviewMethod(int channel){
		if(m_iPlaybackID >= 0){
			Log.i(TAG, "Please stop palyback first");
			return;
		}
		RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
		if (fRealDataCallBack == null){
		    Log.e(TAG, "fRealDataCallBack object is failed!");
            return;
		}
		
		NET_DVR_IPPARACFG_V40 struIPPara = new NET_DVR_IPPARACFG_V40();
		HCNetSDK.getInstance().NET_DVR_GetDVRConfig(m_iLogID, HCNetSDK.NET_DVR_GET_IPPARACFG_V40, 0, struIPPara);
		
		
		if(struIPPara.dwAChanNum > 0){
			iFirstChannelNo = 1;
		} else {
			iFirstChannelNo = struIPPara.dwStartDChan;
		}
		
		if(iFirstChannelNo <= 0){
			iFirstChannelNo = 1;
		}
		
		Log.i(TAG, "iFirstChannelNo:" +iFirstChannelNo);
		iFirstChannelNo = iFirstChannelNo+channel-1;
		NET_DVR_CLIENTINFO ClientInfo = new NET_DVR_CLIENTINFO();
        ClientInfo.lChannel =  iFirstChannelNo; 	// start channel no + preview channel
        ClientInfo.lLinkMode = 1<<31;  			// bit 31 -- 0,main stream;1,sub stream
        										// bit 0~30 -- link type,0-TCP;1-UDP;2-multicast;3-RTP 
        ClientInfo.sMultiCastIP = null;
        
		// net sdk start preview
        m_iPlayID = HCNetSDK.getInstance().NET_DVR_RealPlay_V30(m_iLogID, ClientInfo, fRealDataCallBack, true);
		if (m_iPlayID < 0){
		 	Log.e(TAG, "NET_DVR_RealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
		 	return;
		}
		
		Log.i(TAG, "NetSdk Play sucess ***********************3***************************");
							
		mStartBtn.setText("停止");
//		mPauseBtn.setEnabled(true);
		mLoginBtn.setEnabled(false);
	}
	
	//摄像头控制
	public class ControlBtnListener implements OnTouchListener,OnClickListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			
			if(event.getAction()== MotionEvent.ACTION_DOWN){
				switch (v.getId()) {
					case R.id.video_control_up:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.TILT_UP, 0);
						break;
					case R.id.video_control_down:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.TILT_DOWN, 0);
						break;
					case R.id.video_control_left:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.PAN_LEFT, 0);
						break;
					case R.id.video_control_right:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.PAN_RIGHT, 0);
						break;
					case R.id.video_control_upleft:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.UP_LEFT, 0);
						break;
					case R.id.video_control_upright:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.UP_RIGHT, 0);
						break;
					case R.id.video_control_downleft:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.DOWN_LEFT, 0);
						break;
					case R.id.video_control_downright:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.DOWN_RIGHT, 0);
						break;
					default:
						break;
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP){
				switch (v.getId()) {
					case R.id.video_control_up:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.TILT_UP, 1);
						break;
					case R.id.video_control_down:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.TILT_DOWN, 1);
						break;
					case R.id.video_control_left:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.PAN_LEFT, 1);
						break;
					case R.id.video_control_right:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.PAN_RIGHT, 1);
						break;
					case R.id.video_control_upleft:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.UP_LEFT, 1);
						break;
					case R.id.video_control_upright:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.UP_RIGHT, 1);
						break;
					case R.id.video_control_downleft:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.DOWN_LEFT, 1);
						break;
					case R.id.video_control_downright:
						HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.DOWN_RIGHT, 1);
						break;
					default:
						break;
				}
			}
			return false;
		}
		
		@Override
		public void onClick(View v) {
//			switch (v.getId()) {
//			case R.id.video_control_up:
//				HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.TILT_UP, 1);
//				break;
//			case R.id.video_control_down:
//				HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.TILT_DOWN, 1);
//				break;
//			case R.id.video_control_left:
//				HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.PAN_LEFT, 1);
//				break;
//			case R.id.video_control_right:
//				HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.PAN_RIGHT, 1);
//				break;
//			case R.id.video_control_upleft:
//				HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.UP_LEFT, 1);
//				break;
//			case R.id.video_control_upright:
//				HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.UP_RIGHT, 1);
//				break;
//			case R.id.video_control_downleft:
//				HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.DOWN_LEFT, 1);
//				break;
//			case R.id.video_control_downright:
//				HCNetSDK.getInstance().NET_DVR_PTZControl_Other(m_iLogID, iFirstChannelNo, PTZCommand.DOWN_RIGHT, 1);
//				break;
//			default:
//				break;
//			}
		}
	}
	
//	private Button.OnClickListener Pause_Listener = new Button.OnClickListener() {
//
//		@Override
//		public void onClick(View v) {
//			if ( m_iPlayID < 0){
//				Log.e(TAG, "m_iPlayID < 0");
//				return;
//			} 
//			if(m_iPauseID == -1){
//				if (!m_oPlayerSDK.pause(m_iPort,1)) {
//		            Log.e(TAG, "pause is failed!");
//		            return;
//		        } 
//		        m_iPauseID = 0;
//		        mPauseBtn.setText("恢复");
//			} else {
//				if (!m_oPlayerSDK.pause(m_iPort,0)) {
//		            Log.e(TAG, "pause is failed!");
//		            return;
//		        } 
//		        m_iPauseID = -1;
//		        mPauseBtn.setText("暂停");
//			}
//			
//		}
//		
//	};
	
	private void stopPlay() {
		if ( m_iPlayID < 0){
			Log.e(TAG, "m_iPlayID < 0");
			return;
		}
		
		//  net sdk stop preview
		if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(m_iPlayID)){
			Log.e(TAG, "StopRealPlay is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			return;
		}
		
		// player stop play
		if (!m_oPlayerSDK.stop(m_iPort)) {
            Log.e(TAG, "stop is failed!");
            return;
        }	
		
		if(!m_oPlayerSDK.closeStream(m_iPort)){
            Log.e(TAG, "closeStream is failed!");
            return;
        }
		if(!m_oPlayerSDK.freePort(m_iPort)){
            Log.e(TAG, "freePort is failed!" + m_iPort);
            return;
        }
		m_iPort = -1;
		// set id invalid
		m_iPlayID = -1;		
	}
	
	private int loginDevice(){
		// get instance
		m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
		if (null == m_oNetDvrDeviceInfoV30){
			Log.e(TAG, "HKNetDvrDeviceInfoV30 new is failed!");
			return -1;
		}
		String strIP = mIPAddressEt.getText().toString();
		int	nPort = Integer.parseInt(mPortEt.getText().toString());
		String strUser = mUserEt.getText().toString();
		String strPsd = mPwdEt.getText().toString();
		// call NET_DVR_Login_v30 to login on, port 8000 as default
		int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(strIP, nPort, strUser, strPsd, m_oNetDvrDeviceInfoV30);
		if (iLogID < 0){
			Log.e(TAG, "NET_DVR_Login is failed!Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
			return -1;
		}
		
		Log.i(TAG, "NET_DVR_Login is Successful!");
		
		return iLogID;
	}
	
	private ExceptionCallBack getExceptiongCbf(){
	    ExceptionCallBack oExceptionCbf = new ExceptionCallBack(){
            public void fExceptionCallBack(int iType, int iUserID, int iHandle){
            	;// you can add process here
            }
        };
        return oExceptionCbf;
	}
	
	private RealPlayCallBack getRealPlayerCbf(){
	    RealPlayCallBack cbf = new RealPlayCallBack(){
             public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize){
            	// player channel 1
            	 VideoSurveillanceFragment.this.processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME); 
             }
        };
        return cbf;
	}
	
	public void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode){
	 //   Log.i(TAG, "iPlayViewNo:" + iPlayViewNo + ",iDataType:" + iDataType + ",iDataSize:" + iDataSize);
	    if(HCNetSDK.NET_DVR_SYSHEAD == iDataType){
	    	if(m_iPort >= 0){
    			return;
    		}	    			
    		m_iPort = m_oPlayerSDK.getPort();	
    		if(m_iPort == -1){
    			Log.e(TAG, "getPort is failed with: " + m_oPlayerSDK.getLastError(m_iPort));
    			return;
    		}
    		Log.i(TAG, "getPort succ with: " + m_iPort);
    		if (iDataSize > 0){
    			if (!m_oPlayerSDK.setStreamOpenMode(m_iPort, iStreamMode)) {
    				Log.e(TAG, "setStreamOpenMode failed");
    				return;
    			}
    			if(!m_oPlayerSDK.setSecretKey(m_iPort, 1, "ge_security_3477".getBytes(), 128)){
    				Log.e(TAG, "setSecretKey failed");
    				return;
    			}
    			if (!m_oPlayerSDK.openStream(m_iPort, pDataBuffer, iDataSize, 2*1024*1024)) {
    				Log.e(TAG, "openStream failed");
    				return;
    			}
    			if (!m_oPlayerSDK.play(m_iPort, mSurfaceView.getHolder())) {
    				Log.e(TAG, "play failed");
    				return;
    			}	
    		}
	    }else{
	    	if (!m_oPlayerSDK.inputData(m_iPort, pDataBuffer, iDataSize)){
    			Log.e(TAG, "inputData failed with: " + m_oPlayerSDK.getLastError(m_iPort));
    		}	 
	    }
	}
	
	public void Cleanup() {
        // release player resource
    	
        m_oPlayerSDK.freePort(m_iPort);
		m_iPort = -1;
        
        // release net SDK resource
		HCNetSDK.getInstance().NET_DVR_Cleanup();
    }
	
	@Override
	public void onPause() {
		super.onPause();
		stopPlay();
  	  	Cleanup();
//        android.os.Process.killProcess(android.os.Process.myPid());
	}
}
