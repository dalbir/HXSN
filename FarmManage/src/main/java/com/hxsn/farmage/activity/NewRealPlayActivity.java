package com.hxsn.farmage.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.company.PlaySDK.IPlaySDK;
import com.company.PlaySDK.IPlaySDKCallBack.pCallFunction;
import com.dh.DpsdkCore.Audio_Fun_Info_t;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.Get_TalkStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Login_Info_t;
import com.dh.DpsdkCore.Ptz_Direct_Info_t;
import com.dh.DpsdkCore.Ptz_Operation_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.Send_Audio_Data_Info_t;
import com.dh.DpsdkCore.Talk_Sample_Rate_e;
import com.dh.DpsdkCore.dpsdk_audio_type_e;
import com.dh.DpsdkCore.dpsdk_talk_bits_e;
import com.dh.DpsdkCore.dpsdk_talk_type_e;
import com.dh.DpsdkCore.dpsdk_trans_type_e;
import com.dh.DpsdkCore.fDPSDKStatusCallback;
import com.dh.DpsdkCore.fDPSDKTalkParamCallback;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.beans.SignListBean;
import com.hxsn.farmage.myview.LoadingDialog;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.RealVideos;


public class NewRealPlayActivity extends BaseActivity {
	  // 超时时间
   //
//	private static final String NAME = "system";
//	private static final String PASSWORD = "hxsn1234";
//	private static final String WWIP = "60.10.151.28";
	//private static final String NWIP = "192.168.12.104";
	
	private boolean neiwaiwang = false;
	
	//球机
	private final static String QSPNAME = "dahua_1";
	private static String QSPID = "";
	
	//单兵
	private final static String SPNAME = "PZB3MN088D00166_1";
	private static String SPID = "";
	
	private ImageView jiatu;
	private ImageView shipin;
	private ImageView duijiangIV;
	private SurfaceView video_surfaceView;
	private Button videoBtn;

	private boolean isBallFill = false;
	private boolean isMoveFill = false;
	
	private byte[] m_szCameraId = null;
	private int m_pDLLHandle = 0;
	private int m_nPort = 0;
	private int m_nSeq = 0;
	private int mTimeOut = 30 * 1000;
	
	static IDpsdkCore dpsdkcore = new IDpsdkCore();
	static long m_loginHandle = 0;
	static int m_nLastError = 0;
	static Return_Value_Info_t m_ReValue = new Return_Value_Info_t();
	
	public static final int MSG_SHOW_DIALOG = 1;
	/** 打开声音提醒 */
	public static final int MSG_LIVE_OPEN_SOUND = 2;
	public static final int MSG_LIVE_CLOSE_SOUND = 3;
	/** 打开对讲提醒 */
	public static final int MSG_LIVE_OPEN_TALK = 5;
	public static final int MSG_LIVE_CLOSE_TALK = 6;
	/** 对讲成功消息 */
	public static final int MSG_LIVE_TALK_SUCCUSS = 7;
	/** 对讲失败消息 */
	public static final int MSG_LIVE_TALK_FAIL = 8;
	/** 对讲状态 */
	public static final int MSG_LIVE_TALK_STATUS = 9;
	/** 关闭对讲成功消息 */
	public static final int MSG_LIVE_STOP_TALK_SUCCUSS = 10;
	/** 关闭对讲失败消息 */
	public static final int MSG_LIVE_STOP_TALK_FAIL = 11;
	public static final int MSG_TOAST_OPEN_SOUND = 12;
	public static final int MSG_TOAST_CLOSE_SOUND = 13;
	// 超时时间
	private static final int DPSDK_CORE_DEFAULT_TIMEOUT = 30000;
	private int ret;
	private Audio_Fun_Info_t afInfo = null;
	// playsdk端口
	private int port = 0;
	private Send_Audio_Data_Info_t sadInfo = null;
	private Boolean ISTALKOPEN = false;
	private Return_Value_Info_t rvi;

	private int mAudioType = dpsdk_audio_type_e.Talk_Coding_G711a;
	private int mSampleRate = Talk_Sample_Rate_e.Talk_Audio_Sam_8K;
	private int mTalkBits = dpsdk_talk_bits_e.Talk_Audio_Bits_16;
	private int callBackTag = 0;
	private Handler mHandler;

	private boolean isPlay = false;
	
	private boolean isRun = true;
	RelativeLayout onLineViewRL;
	private ImageButton online_video_up;
	private ImageButton online_video_down;
	private ImageButton online_video_right;
	private ImageButton online_video_left;
	private ImageButton online_video_big;
	private ImageButton online_video_small;
	
	String mr="0";
	private boolean isQiang = false;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		  initVideo();//初始化摄像头
		
		if(RealVideos.beans.getSignList().size()>0)
		{		
		initview();
		initSound();
		}
		else
			showToast("视频未接通，请稍后...");
		}
	
	class VideoThead implements Runnable { 
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 实时视频
			if (isPlay) {
				StopRealPlay();
			}    
			initVideo();//初始化摄像头   
		}
	} 
	// 更新主线程
	Handler handVideo = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1000) {
			 	LoadingDialog.dissmissLoading();
				if (StartRealPlay()) {
					isPlay = true;
				}
			}else if(msg.what==2000)
			{
				new Thread(new VideoThead()).start();
			}
		};
	}; 
	
	
	
	private void init() {
		// TODO Auto-generated method stub
		m_nLastError = IDpsdkCore.DPSDK_Create(1, m_ReValue); 
		IDpsdkCore.DPSDK_SetDPSDKStatusCallback(m_ReValue.nReturnValue,
				new fDPSDKStatusCallback() {

					@Override
					public void invoke(int nPDLLHandle, int nStatus) {
						Log.v("fDPSDKStatusCallback", "nStatus = " + nStatus);
					}
				});

		Login_Info_t loginInfo = new Login_Info_t();
		Integer error = Integer.valueOf(0);
	//	if(neiwaiwang){
			loginInfo.szIp = GetURL.VideoIP.getBytes();
			String strPort = "9000".trim();
			loginInfo.nPort = Integer.parseInt(strPort);
//		} else {
//			loginInfo.szIp = NWIP.getBytes();
//			String strPort = "9000".trim();
//			loginInfo.nPort = Integer.parseInt(strPort);
//		}
		loginInfo.szUsername = GetURL.VideoName.getBytes();
		loginInfo.szPassword = GetURL.VideoPwd.getBytes();
		loginInfo.nProtocol = 2;
		IDpsdkCore.DPSDK_Login(m_ReValue.nReturnValue, loginInfo, 30000);
		IDpsdkCore.DPSDK_SetCompressType(m_ReValue.nReturnValue, 0);
		m_pDLLHandle = m_ReValue.nReturnValue;
	}

	private void initview() {
		shipin = (ImageView) findViewById(R.id.shipin);
		video_surfaceView = (SurfaceView) findViewById(R.id.video_surfaceView);
		jiatu = (ImageView) findViewById(R.id.jiatu);
		duijiangIV = (ImageView) findViewById(R.id.duijiangIV);
		videoBtn=(Button)findViewById(R.id.videoBtn);
		
		online_video_up = (ImageButton) findViewById(R.id.online_video_up);
		 
		online_video_down = (ImageButton) findViewById(R.id.online_video_down);
		online_video_left = (ImageButton) findViewById(R.id.online_video_left);
		online_video_right = (ImageButton) findViewById(R.id.online_video_right);
		online_video_big = (ImageButton) findViewById(R.id.online_video_small);
		online_video_small = (ImageButton) findViewById(R.id.online_video_big); 
		
		onLineViewRL=(RelativeLayout)findViewById(R.id.onLineViewRL);
		
		for(int i=0;i<RealVideos.beans.getSignList().size();i++)
		{
			SignListBean signBean=RealVideos.beans.getSignList().get(i);
			String videoAddress=signBean.getAddress();
			String isdef=signBean.getIsdef();
			String eqtype=signBean.getEqtype();
			if(eqtype.equals("0")||eqtype.equals("1"))
				QSPID=videoAddress;
			if (eqtype.equals("1"))
				isQiang=true;
			if(eqtype.equals("2"))
				SPID=videoAddress;
			if(isdef.equals("1"))
			{			mr="1";
				if(eqtype.equals("0"))
				{
					isBallFill = true;
				}
				else if(eqtype.equals("1"))
				{	
					isBallFill = true;
					isQiang=true;
				}
				else  if(eqtype.equals("2"))
				{
					isBallFill = false;
				}
						
		     }
			
		}
		
		if(mr.equals("1"))
		{
			if(	isBallFill)
			{
				if(isQiang)
				{
				showBall();
				onLineViewRL.setVisibility(View.INVISIBLE);
				duijiangIV.setVisibility(View.INVISIBLE);
		        }
			else
				{	showBall();
				onLineViewRL.setVisibility(View.VISIBLE);
				duijiangIV.setVisibility(View.INVISIBLE);
			}
			} else
			{
				showMove();
				shipin.setImageResource(R.drawable.qiuji);
				onLineViewRL.setVisibility(View.INVISIBLE);
				duijiangIV.setVisibility(View.VISIBLE);
			}
		}
		
		else if(mr.equals("0"))
		{
			try{
			SignListBean signBean=RealVideos.beans.getSignList().get(0);
			String eqtype=signBean.getEqtype();
			if(eqtype.equals("0"))
			{
				isBallFill = true;
				isQiang=false;
				showBall();
				onLineViewRL.setVisibility(View.VISIBLE);
				duijiangIV.setVisibility(View.INVISIBLE);
			}
			else if(eqtype.equals("1"))
			{
				isQiang=true;
				isBallFill = true;
				showBall();
				onLineViewRL.setVisibility(View.INVISIBLE);
				duijiangIV.setVisibility(View.INVISIBLE);
			}
			else  if(eqtype.equals("2"))
			{
				isBallFill = false;
				showMove();
				shipin.setImageResource(R.drawable.qiuji);
				onLineViewRL.setVisibility(View.INVISIBLE);
				duijiangIV.setVisibility(View.VISIBLE);
			}
			}
			catch(Exception e)
			{
				
			}
		}
		
		
//		videoBtn.setOnTouchListener(ImgBtnEffact.btnTL);
//		videoBtn.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				finish();
//			}
//		});
		shipin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(isBallFill){
					closeBall();
					isBallFill = false;
					showMove();
					shipin.setImageResource(R.drawable.qiuji);
					onLineViewRL.setVisibility(View.INVISIBLE);
					duijiangIV.setVisibility(View.VISIBLE);
				}
				else{
					closeMove();
					isBallFill = true;
					showBall();
					shipin.setImageResource(R.drawable.danbing);
					if(isQiang)
					onLineViewRL.setVisibility(View.INVISIBLE);
					else
					onLineViewRL.setVisibility(View.VISIBLE);
					duijiangIV.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		duijiangIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (ISTALKOPEN) {
					soundClose();
				} else {
					soundOpen();
				}
			}
		});
		
		
		// 视频操作监听
				online_video_up.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							cotorlDirector(1, false, true);
							
						}
						
						if (event.getAction() == MotionEvent.ACTION_UP) {
							cotorlDirector(1, true, false);
						}
						return false;
					}
				});

				online_video_down.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							cotorlDirector(2, false, true);
						}
						
						if (event.getAction() == MotionEvent.ACTION_UP) {
							cotorlDirector(2, true, false);
						}
						return false;
					}
				});

				online_video_left.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
//						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							cotorlDirector(3, false, true);
						}
						
						if (event.getAction() == MotionEvent.ACTION_UP) {
							cotorlDirector(3, true, false);
						}
						 
						return false;
					}
				});

				online_video_right.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							cotorlDirector(4, false, true);
						}
						
						if (event.getAction() == MotionEvent.ACTION_UP) {
							cotorlDirector(4, true, false);
						}
						return false;
					}
				}); 
				online_video_big.setOnTouchListener(new OnTouchListener() { 
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) { 
							cotorlDirector(true, 0);
						}
						
						if (event.getAction() == MotionEvent.ACTION_UP) {
							cotorlDirector(false, 0);
						}
						return false;
					}
				});

				online_video_small.setOnTouchListener(new OnTouchListener() {
					
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							cotorlDirector(true, 3);
						}
						
						if (event.getAction() == MotionEvent.ACTION_UP) {
							cotorlDirector(false, 3);
						}
						return false;
					}
				});
		
	}
	
	private void showBall() {
		// TODO Auto-generated method stub
		m_szCameraId = QSPID.getBytes();
		m_nPort = IPlaySDK.PLAYGetFreePort();
		SurfaceHolder holder = video_surfaceView.getHolder();
		holder.addCallback(new Callback() {

			public void surfaceCreated(SurfaceHolder holder) {
				Log.d("zxw", "surfaceCreated");
				IPlaySDK.InitSurface(m_nPort, video_surfaceView);
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				Log.d("zxw", "surfaceChanged");
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.d("zxw", "surfaceDestroyed");
				holder.removeCallback(this);
			}
		});

		final fMediaDataCallback fm = new fMediaDataCallback() {

			@Override
			public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
					byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {

				int ret = IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
				if (ret == 1) {
					Log.e("xss", "playing success=" + nSeq + " package size="
							+ nDataLen);
				} else {
					Log.e("xss", "playing failed=" + nSeq + " package size="
							+ nDataLen);
				}
			}
		};

		if (!StartRealPlay()) {
			Log.e("zxw", "StartRealPlay failed!");
			return;
		}

		try {
			Return_Value_Info_t retVal = new Return_Value_Info_t();
			Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t();
			// m_szCameraId = etCam.getText().toString().getBytes();
        	System.arraycopy(m_szCameraId, 0, getRealStreamInfo.szCameraId, 0,
					m_szCameraId.length);
			// getRealStreamInfo.szCameraId = "1000096$1$0$0".getBytes();
			getRealStreamInfo.nMediaType = 1;
			getRealStreamInfo.nRight = 1;
			getRealStreamInfo.nStreamType = 2;
			getRealStreamInfo.nTransType = 1;
			Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
			IDpsdkCore.DPSDK_GetChannelInfoById(m_pDLLHandle, m_szCameraId,
					ChannelInfo);
			int ret = IDpsdkCore.DPSDK_GetRealStream(m_pDLLHandle, retVal,
					getRealStreamInfo, fm, mTimeOut);
			if (ret == 0) {
				m_nSeq = retVal.nReturnValue;
				Log.e("success!", ret + "");
				jiatu.setVisibility(View.GONE);
			} else {
				StopRealPlay();
				Log.e("failed!", ret + "");
				Toast.makeText(getApplicationContext(), "Open video failed!",
						Toast.LENGTH_SHORT).show();
				jiatu.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			Log.e("zxw", e.toString());
		}
	}
	
	private void closeBall() {
		int ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq,
				mTimeOut);
		if (ret == 0) {
			Log.e("zxw", "DPSDK_CloseRealStreamByCameraId success!");
		} else {
			Log.e("zxw", "DPSDK_CloseRealStreamByCameraId failed! ret = " + ret);
		}
		StopRealPlay();
		isBallFill = false;
	}
	
	private void showMove() {
		// TODO Auto-generated method stub

		m_szCameraId = SPID.getBytes();
		m_nPort = IPlaySDK.PLAYGetFreePort();
		SurfaceHolder holder = video_surfaceView.getHolder();
		holder.addCallback(new Callback() {

			public void surfaceCreated(SurfaceHolder holder) {
				Log.d("zxw", "surfaceCreated");
				IPlaySDK.InitSurface(m_nPort, video_surfaceView);
			}

			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
				Log.d("zxw", "surfaceChanged");
			}

			public void surfaceDestroyed(SurfaceHolder holder) {
				Log.d("zxw", "surfaceDestroyed");
				holder.removeCallback(this);
			}
		});

		final fMediaDataCallback fm = new fMediaDataCallback() {

			@Override
			public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
					byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {

				int ret = IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
				if (ret == 1) {
					Log.e("xss", "playing success=" + nSeq + " package size="
							+ nDataLen);
				} else {
					Log.e("xss", "playing failed=" + nSeq + " package size="
							+ nDataLen);
				}
			}
		};

		if (!StartRealPlay()) {
			Log.e("zxw", "StartRealPlay failed!");
			return;
		}

		try {
			Return_Value_Info_t retVal = new Return_Value_Info_t();

			Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t();
			// m_szCameraId = etCam.getText().toString().getBytes();

			System.arraycopy(m_szCameraId, 0, getRealStreamInfo.szCameraId, 0,
					m_szCameraId.length);
			// getRealStreamInfo.szCameraId = "1000096$1$0$0".getBytes();
			getRealStreamInfo.nMediaType = 1;
			getRealStreamInfo.nRight = 1;
			getRealStreamInfo.nStreamType = 2;
			getRealStreamInfo.nTransType = 1;
			Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
			IDpsdkCore.DPSDK_GetChannelInfoById(m_pDLLHandle, m_szCameraId,
					ChannelInfo);
			int ret = IDpsdkCore.DPSDK_GetRealStream(m_pDLLHandle, retVal,
					getRealStreamInfo, fm, mTimeOut);
			if (ret == 0) {
				m_nSeq = retVal.nReturnValue;
				Log.e("success!", ret + "");
				jiatu.setVisibility(View.GONE);
			} else {
				StopRealPlay();
				Log.e("failed!", ret + "");
				Toast.makeText(getApplicationContext(), "Open video failed!",
						Toast.LENGTH_SHORT).show();
				jiatu.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			Log.e("zxw", e.toString());
		}
	}

	private void closeMove() {
		int ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq,
				mTimeOut);
		if (ret == 0) {
			Log.e("zxw", "DPSDK_CloseRealStreamByCameraId success!");
		} else {
			Log.e("zxw", "DPSDK_CloseRealStreamByCameraId failed! ret = " + ret);
		}
		StopRealPlay();
		isMoveFill = false;
	}
	
	public boolean StartRealPlay() {
		if (video_surfaceView == null)
			return false;

		boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort, null, 0,
				1500 * 1024) == 0 ? false : true;
		if (bOpenRet) {
			boolean bPlayRet = IPlaySDK
					.PLAYPlay(m_nPort, video_surfaceView) == 0 ? false
					: true;
			Log.i("StartRealPlay", "StartRealPlay1");
			if (bPlayRet) {
				// boolean bSuccess = IPlaySDK.PLAYPlaySoundShare(m_nPort) == 0
				// ? false : true;
				//
				// Log.i("StartRealPlay", "StartRealPlay2");
				// if(!bSuccess)
				// {
				// IPlaySDK.PLAYStop(m_nPort);
				// IPlaySDK.PLAYCloseStream(m_nPort);
				// Log.i("StartRealPlay", "StartRealPlay3");
				// return false;
				// }
			} else {
				IPlaySDK.PLAYCloseStream(m_nPort);
				Log.i("StartRealPlay", "StartRealPlay4");
				return false;
			}
		} else {
			Log.i("StartRealPlay", "StartRealPlay5");
			return false;
		}

		return true;
	}
	
	public void StopRealPlay() {
		try {
			// IPlaySDK.PLAYStopSoundShare(m_nPort);
			IPlaySDK.PLAYStop(m_nPort);
			IPlaySDK.PLAYCloseStream(m_nPort);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initSound() {
		Handler.Callback callback = new Handler.Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_TOAST_OPEN_SOUND:
					break;
				case MSG_TOAST_CLOSE_SOUND:
					break;
				case MSG_LIVE_OPEN_SOUND:
					//���Խ��Ǵ򿪵ģ��رնԽ���������
					//closeTalk();
					playSound(true);
					break;
				case MSG_LIVE_CLOSE_SOUND:	
					playSound(false);
					break;
				case MSG_LIVE_OPEN_TALK:
					//关闭声音，开始对讲，打开语音对讲等待框
					//playSound(false);
					//showProgressDialog();
					startTalk();
					break;
				case MSG_LIVE_CLOSE_TALK:
					closeTalk();
					break;
				case MSG_LIVE_TALK_SUCCUSS:
					Toast.makeText(NewRealPlayActivity.this, "打开对讲成功", Toast.LENGTH_SHORT).show();
					break;
				case MSG_LIVE_TALK_FAIL:
		             Toast.makeText(NewRealPlayActivity.this, "打开对讲失败" + ",errCode:" + msg.arg1, Toast.LENGTH_SHORT).show();
					break;
				case MSG_LIVE_STOP_TALK_SUCCUSS:
					Toast.makeText(NewRealPlayActivity.this, "关闭对讲成功", Toast.LENGTH_SHORT).show();
					break;
				case MSG_LIVE_STOP_TALK_FAIL:
					Toast.makeText(NewRealPlayActivity.this, "关闭对讲失败" + ",errCode:" + msg.arg1, Toast.LENGTH_SHORT).show(); 
					break;
				}

				return false;
			}
		};
		mHandler = new Handler(getMainLooper(), callback);
		
		IDpsdkCore.DPSDK_SetDPSDKTalkParamCallback(m_pDLLHandle, fdpsdkCallback);
	}
	
	private void soundClose() {
		ISTALKOPEN = false;
		duijiangIV.setImageResource(R.drawable.microphone_0);
		sendMessage(mHandler, MSG_LIVE_CLOSE_TALK, 0, 0);
	}
	
	private void soundOpen() {
		callBackTag = 0;
		ISTALKOPEN = true;
		duijiangIV.setImageResource(R.drawable.microphone_1);
		new Thread() {
			public void run() {
				startTalk();
				Log.i("thread", "run thread");
			}
		}.start();
	}
	
	fMediaDataCallback fmdCallback = new fMediaDataCallback() {
		@Override
		public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
				byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) {
		
			IPlaySDK.PLAYInputData(port, szData, nDataLen);
		}
	};
	
	pCallFunction fun = new pCallFunction() {
		@Override
		public void invoke(byte[] arg0, int arg1, long arg2) {

			sadInfo = new Send_Audio_Data_Info_t(arg1);
			sadInfo.pData = arg0;
			sadInfo.nLen = arg1;
			sadInfo.nAudioType = dpsdk_audio_type_e.Talk_Coding_PCM;
			sadInfo.nTalkBits = dpsdk_talk_bits_e.Talk_Audio_Bits_16;
			sadInfo.nSampleRate = Talk_Sample_Rate_e.Talk_Audio_Sam_8K;
			sadInfo.pCallBackFun = afInfo.pCallBackFun;
			sadInfo.pUserParam = afInfo.pUserParam;
			IDpsdkCore.DPSDK_SendAudioData(m_pDLLHandle, sadInfo);

		}
	};
	
	fDPSDKTalkParamCallback fdpsdkCallback = new fDPSDKTalkParamCallback() {
		@Override
		public void invoke(int arg0, int arg1, int arg2, int arg3, int arg4,
				int arg5) {
			mAudioType = arg1;
			mSampleRate = arg4;
			mTalkBits = arg3;
			if (callBackTag < 1) {
				new Thread() {
					public void run() {
						startTalk();
						Log.i("thread", "run thread");
					}
				}.start();
			} else {
				sendMessage(mHandler, MSG_LIVE_TALK_FAIL, 0, 0);
				Log.i("fdpsdkCallback", "start talk failed");
			}
			++callBackTag;
			Log.i("fDPSDKTalkParamCallback", "callBackTag = " + callBackTag);
		}
	};
	
	private void startTalk() {


		Get_TalkStream_Info_t gti = new Get_TalkStream_Info_t();
		gti.nAudioType = mAudioType;
		gti.nBitsType = mTalkBits;
		gti.nSampleType = mSampleRate;

		gti.nTalkType = dpsdk_talk_type_e.Talk_Type_Device;
		gti.nTransType = dpsdk_trans_type_e.DPSDK_CORE_TRANSTYPE_TCP;

		gti.szCameraId = SPID.substring(0,7).getBytes(); // �豸ID
		//gti.szCameraId="1000000$1$0$0".getBytes();
		// �����������,����Ϊ���������ʶ
		rvi = new Return_Value_Info_t();
		int ret = IDpsdkCore.DPSDK_GetTalkStream(m_pDLLHandle, rvi, gti,
				fmdCallback, DPSDK_CORE_DEFAULT_TIMEOUT);
		Log.e("startTalk", "rvi.nReturnValue=" + rvi.nReturnValue);

		if (ret != 0) {
			return;
		} else if (ret == 0) {
			sendMessage(mHandler, MSG_LIVE_TALK_SUCCUSS, 0, 0);
			playSound(true);
		}
		/************** �ֻ�ɼ���ݷ��͸��豸 ****************/
		// ��ȡ�����ɼ��ص���Ϣ
		afInfo = new Audio_Fun_Info_t();
		ret = IDpsdkCore.DPSDK_GetSdkAudioCallbackInfo(m_pDLLHandle,
				afInfo);
		if (ret != 0) {

		}

		// ����Ƶ�ɼ�����
		ret = IPlaySDK.PLAYOpenAudioRecord(fun,
				dpsdk_talk_bits_e.Talk_Audio_Bits_16,
				Talk_Sample_Rate_e.Talk_Audio_Sam_8K, 1024, 0);
		if (ret != 1) {

		
		}
	}
	
	
	private void closeTalk() {
		playSound(false);
		IPlaySDK.PLAYCloseAudioRecord();

		if (rvi != null) {
			ret = IDpsdkCore.DPSDK_CloseTalkStreamBySeq(m_pDLLHandle,
					rvi.nReturnValue, DPSDK_CORE_DEFAULT_TIMEOUT);
			Log.e("stopTalk", "rvi.nReturnValue=" + rvi.nReturnValue);
		}

		if (ret != 0) {
			Log.e("stopTalk", "ret=" + ret);
			sendMessage(mHandler, MSG_LIVE_STOP_TALK_FAIL, ret, 0);
			return;
		}
		sendMessage(mHandler, MSG_LIVE_STOP_TALK_SUCCUSS, 0, 0);
		ISTALKOPEN = false;
	}
	
	private void playSound(boolean isPlaySound) {
		if (isPlaySound) {
			port = IPlaySDK.PLAYGetFreePort();
			if (port == -1) {
				return;
			}
			// open stream
			IPlaySDK.PLAYOpenStream(port, null, 0, 1024 * 1024);
			int ret = IPlaySDK.PLAYPlay(port, null);
			if (ret == 0) {
				// release port
				IPlaySDK.PLAYReleasePort(port);
				port = -1;
				return;
			}
			IPlaySDK.PLAYPlaySound(port);
			sendMessage(mHandler, MSG_TOAST_OPEN_SOUND, 0, 0);
		} else {
			IPlaySDK.PLAYStopSound();
			sendMessage(mHandler, MSG_TOAST_CLOSE_SOUND, 0, 0);
		}
	}
	
	private void sendMessage(Handler mHandler, int msg, int org1, int org2) {
		Message Msg = mHandler.obtainMessage();
		Msg.what = msg;
		Msg.arg1 = org1;
		Msg.arg2 = org2;
		Msg.sendToTarget();
	}
	
	
	
	public void cotorlDirector(int direct, boolean flag, boolean isDirection) {
		if (isDirection) {
			Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
			System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0,
					m_szCameraId.length);
			ptzDirectInfo.bStop = flag;
			ptzDirectInfo.nDirect = direct;
			ptzDirectInfo.nStep = 4;
		
			int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo,
					mTimeOut);
			if (ret == 0) {
				//Log.e("xss", "DPSDK_PtzDirection success!");
			} else {
				//Log.e("xss", "DPSDK_PtzDirection failed!");
			}
		} else {
			Ptz_Direct_Info_t ptzDirectInfo = new Ptz_Direct_Info_t();
			System.arraycopy(m_szCameraId, 0, ptzDirectInfo.szCameraId, 0, m_szCameraId.length);
			ptzDirectInfo.bStop = flag;
			ptzDirectInfo.nDirect = direct;	
			ptzDirectInfo.nStep = 4;
			
			int ret = IDpsdkCore.DPSDK_PtzDirection(m_pDLLHandle, ptzDirectInfo, mTimeOut);
			if(ret == 0)
			{
				//Log.e("xss","DPSDK_PtzDirection success!");
			}
			else
			{
				//Log.e("xss","DPSDK_PtzDirection failed!");
			}	
		}
	}
	public void cotorlDirector(boolean direct, int flag) {
		if(direct)
		{
			Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
			System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
			ptzOperationInfo.bStop = false;
			ptzOperationInfo.nOperation = flag;	
			ptzOperationInfo.nStep = 4; 
			int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
			if(ret == 0)
			{
				//Log.e("xss","DPSDK_PtzCameraOperation success!");
			}
			else
			{
				//Log.e("xss","DPSDK_PtzCameraOperation failed!");
			}
		}
		else {
			Ptz_Operation_Info_t ptzOperationInfo = new Ptz_Operation_Info_t();
			System.arraycopy(m_szCameraId, 0, ptzOperationInfo.szCameraId, 0, m_szCameraId.length);
			ptzOperationInfo.bStop = true;
			ptzOperationInfo.nOperation = flag;	
			ptzOperationInfo.nStep = 4;
			
			int ret = IDpsdkCore.DPSDK_PtzCameraOperation(m_pDLLHandle, ptzOperationInfo, mTimeOut);
			if(ret == 0)
			{
				//Log.e("xss","DPSDK_PtzCameraOperation success!");
			}
			else
			{
				//Log.e("xss","DPSDK_PtzCameraOperation failed!");
			}
		}
	} 			
	
	
	private void initVideo() { 
		m_nPort = IPlaySDK.PLAYGetFreePort(); 
		m_nLastError = IDpsdkCore.DPSDK_Create(1, m_ReValue); 
		IDpsdkCore.DPSDK_SetDPSDKStatusCallback(m_ReValue.nReturnValue,
				new fDPSDKStatusCallback() {
					@Override
					public void invoke(int nPDLLHandle, int nStatus) { 
					}
				}); 
		Login_Info_t loginInfo = new Login_Info_t(); 
		loginInfo.szIp = GetURL.VideoIP.getBytes();
		String strPort = "9000".trim();
		loginInfo.nPort = Integer.parseInt(strPort); 
		loginInfo.szUsername = GetURL.VideoName.getBytes();
		loginInfo.szPassword = GetURL.VideoPwd.getBytes();
		loginInfo.nProtocol = 2;
		IDpsdkCore.DPSDK_Login(m_ReValue.nReturnValue, loginInfo, 30000);
		IDpsdkCore.DPSDK_SetCompressType(m_ReValue.nReturnValue, 0);
		m_pDLLHandle = m_ReValue.nReturnValue; 
        Return_Value_Info_t rvInfo2 = new Return_Value_Info_t();
        IDpsdkCore.DPSDK_LoadDGroupInfo(m_pDLLHandle, rvInfo2, DPSDK_CORE_DEFAULT_TIMEOUT);
	} 
	private void showBallVideo() { 
		m_szCameraId = QSPID.getBytes();
		m_nPort = IPlaySDK.PLAYGetFreePort();
		SurfaceHolder holder = video_surfaceView.getHolder();
		holder.addCallback(new Callback() { 
			public void surfaceCreated(SurfaceHolder holder) { 
				IPlaySDK.InitSurface(m_nPort, video_surfaceView);
			} 
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {  
			} 
			public void surfaceDestroyed(SurfaceHolder holder) { 
				holder.removeCallback(this);
			}
		}); 
		final fMediaDataCallback fm = new fMediaDataCallback() { 
			@Override
			public void invoke(int nPDLLHandle, int nSeq, int nMediaType,
					byte[] szNodeId, int nParamVal, byte[] szData, int nDataLen) { 
				int ret = IPlaySDK.PLAYInputData(m_nPort, szData, nDataLen);
				if (ret == 1) { 
				} else { 
				}
			}
		}; 
		if (!StartRealPlay()) {
			isPlay = false; 
			return;
		} else {
			isPlay = true;
		} 
		try {
			Return_Value_Info_t retVal = new Return_Value_Info_t(); 
			Get_RealStream_Info_t getRealStreamInfo = new Get_RealStream_Info_t(); 
			System.arraycopy(m_szCameraId, 0, getRealStreamInfo.szCameraId, 0,
					m_szCameraId.length);
			getRealStreamInfo.nMediaType = 1;
			getRealStreamInfo.nRight = 1;
			getRealStreamInfo.nStreamType = 2;
			getRealStreamInfo.nTransType = 1;
			Enc_Channel_Info_Ex_t ChannelInfo = new Enc_Channel_Info_Ex_t();
			IDpsdkCore.DPSDK_GetChannelInfoById(m_pDLLHandle, m_szCameraId,
					ChannelInfo);
			int ret = IDpsdkCore.DPSDK_GetRealStream(m_pDLLHandle, retVal,
					getRealStreamInfo, fm, mTimeOut);
			if (ret == 0) {
				m_nSeq = retVal.nReturnValue;
				handVideo.sendEmptyMessage(1000);
				//Log.e("zxw DPSDK_GetRealStream success!", ret + "");
			} else {
				StopRealPlay();
				//Log.e("zxw DPSDK_GetRealStream failed!", ret + "");
				 Toast.makeText(getApplicationContext(), "open video failed", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "open video failed", Toast.LENGTH_SHORT).show();
		}
	}
	
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isBallFill) {
			closeBall();
		} else {
			closeMove();
		}
		closeTalk();
		IDpsdkCore.DPSDK_Logout(m_ReValue.nReturnValue, 30000);
		IDpsdkCore.DPSDK_Destroy(m_ReValue.nReturnValue);
	}
	
}
