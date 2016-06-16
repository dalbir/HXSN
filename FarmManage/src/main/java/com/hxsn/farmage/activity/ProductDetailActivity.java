package com.hxsn.farmage.activity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.company.PlaySDK.IPlaySDK;
import com.dh.DpsdkCore.Enc_Channel_Info_Ex_t;
import com.dh.DpsdkCore.Get_RealStream_Info_t;
import com.dh.DpsdkCore.IDpsdkCore;
import com.dh.DpsdkCore.Login_Info_t;
import com.dh.DpsdkCore.Ptz_Direct_Info_t;
import com.dh.DpsdkCore.Ptz_Operation_Info_t;
import com.dh.DpsdkCore.Return_Value_Info_t;
import com.dh.DpsdkCore.fDPSDKStatusCallback;
import com.dh.DpsdkCore.fMediaDataCallback;
import com.hxsn.farmage.MainActivity;
import com.hxsn.farmage.MyApplication;
import com.hxsn.farmage.R;
import com.hxsn.farmage.adapter.PopListAdapter;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.beans.Contactor;
import com.hxsn.farmage.beans.PopListInfo;
import com.hxsn.farmage.beans.Videos;
import com.hxsn.farmage.myview.CircleImageView;
import com.hxsn.farmage.myview.LoadingDialog;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.Shared;

@SuppressLint({ "HandlerLeak", "ClickableViewAccessibility" })
@TargetApi(Build.VERSION_CODES.KITKAT)
public class ProductDetailActivity extends BaseActivity {
	  // 超时时间
    private static final int DPSDK_CORE_DEFAULT_TIMEOUT = 90000;
	private ImageButton yy_returnBtn;
	private LinearLayout videoDirectionLL, yy_videoLL, yy_jlLL, yy_czLL,gs_czLL;
	private ImageView ssvdIV, lsllIV, wzIV,wdgsIV;
	private TextView ssvdTV, lsllTV, wzTV,wdgsTV,gs_sx;
	private WebView yy_llWV;
	private SurfaceView video_surfaceView;
	private LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	private RelativeLayout product_video_control_view;
	private ImageButton product_video_up;
	private ImageButton product_video_down;
	private ImageButton product_video_right;
	private ImageButton product_video_left;
	private ImageButton product_video_big;
	private ImageButton product_video_small;
	
	private Intent tabIntent;
	private ArrayList<Videos> videoList = new ArrayList<Videos>();
	private String dkid = null;

	CircleImageView productHeadIV;
	TextView productStatusTV, yy_proNameTV, yypd_localTV;
	// 实时视频
	private  String QSPID = "";
	static int m_nLastError = 0;
	static Return_Value_Info_t m_ReValue = new Return_Value_Info_t();
	private int m_pDLLHandle = 0;
	private byte[] m_szCameraId = null;
	private int m_nPort = 0;
	private int m_nSeq = 0;
	private int mTimeOut = 30*1000;
	private boolean isPlay = false;
	FrameLayout video_ctFL;
	boolean isQiu=true;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		
		product_video_control_view=(RelativeLayout)findViewById(R.id.product_video_control_view);
		Intent inte = getIntent();

		if (inte != null){
			LoadingDialog.showLoading(this);
			initLogVideo();
			dkid = inte.getStringExtra("dkid"); 
			getDisInfor();//获取地块信息
		}
		else{ 
			showToast("获取地块信息失败， 请后退重试...");;
		}
		initView();//UI View
		initListener(); // 
	} 
	private void initLogVideo() {
		// TODO Auto-generated method stub
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
	class VideoThead implements Runnable { 
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 实时视频
			if (isPlay) {
				StopRealPlay();
			}  
			showVideo();
		}
	} 
	// 更新主线程
	Handler handVideo = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1000) {
			 	LoadingDialog.dissmissLoading(); //加载完成
				videoDirectionLL.setVisibility(View.VISIBLE);
			 	 if(isQiu)
					 product_video_control_view.setVisibility(View.VISIBLE);
				 else
					 product_video_control_view.setVisibility(View.GONE);
				if (StartRealPlay()) {
					isPlay = true;
				}
			}else if(msg.what==2000)
			{
				new Thread(new VideoThead()).start();
			}
		};
	}; 

	private void showVideo() { 
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
				new Thread(new Runnable(){
					public void run()
					{
						try {
							Thread.currentThread().sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}).start();
				handVideo.sendEmptyMessage(1000);
				//Log.e("zxw DPSDK_GetRealStream success!", ret + "");
			} else {
				StopRealPlay();
				//Log.e("zxw DPSDK_GetRealStream failed!", ret + "");
				 //Toast.makeText(getApplicationContext(), "开启摄像头失败！", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			//Toast.makeText(getApplicationContext(), "开启摄像头失败！", Toast.LENGTH_SHORT).show();
		}
	}

	public boolean StartRealPlay() {
		if (video_surfaceView == null)
			return false; 
		boolean bOpenRet = IPlaySDK.PLAYOpenStream(m_nPort, null, 0,
				1500 * 1024) == 0 ? false : true;
		if (bOpenRet) {
			boolean bPlayRet = IPlaySDK.PLAYPlay(m_nPort, video_surfaceView) == 0 ? false
					: true;
			//Log.i("StartRealPlay", "StartRealPlay1");
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
				//Log.i("StartRealPlay", "StartRealPlay4");
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
			//IPlaySDK.PLAYStopSoundShare(m_nPort);
			IPlaySDK.PLAYStop(m_nPort);  		
			IPlaySDK.PLAYCloseStream(m_nPort);
			isPlay = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isPlay) {
			StopRealPlay();
		}
	}

	public int getScreenWidth() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth();
	}

	private void getDisInfor() {
		// TODO Auto-generated method stub
		try {
			StringRequest stringRequest = new StringRequest(GetURL.DIKUAI
					+ Shared.getUserID() + "&code=" + dkid,
					new Response.Listener<String>() {
						@SuppressWarnings("deprecation")
						@Override
						public void onResponse(String response) {
							JSONObject j;
							try {
								j = new JSONObject(response);
								if (j.getString("code").equals("200")) {
									JSONObject diskInfor = new JSONObject(j
											.getString("result"));
									String img = diskInfor.getString("img");
									String status = diskInfor
											.getString("status");
									try{
									if(diskInfor.getString("story")!=null)
									{
										String story= diskInfor
											.getString("story");
									if(story.equals("1"))
									{	gs_czLL.setVisibility(View.VISIBLE);
									    gs_sx.setVisibility(View.VISIBLE);
									}
									}
									}
									catch(Exception e){}
									String crop = diskInfor.getString("crop");
									String name1 = diskInfor.getString("name1");
									String name2 = diskInfor.getString("name2");
									if (img != null) {
										ImageRequest imageRequest = new ImageRequest(
												GetURL.BASEURL + img,
												new Response.Listener<Bitmap>() {
													@Override
													public void onResponse(
															Bitmap response) {
														Bitmap btm2 = Bitmap
																.createScaledBitmap(
																		response,
																		60, 60,
																		false);
														productHeadIV
																.setImageBitmap(btm2);
													}
												}, 0, 0, Config.RGB_565,
												new Response.ErrorListener() {
													@Override
													public void onErrorResponse(
															VolleyError error) {
														productHeadIV
																.setImageResource(R.drawable.noimg);
													}
												});
										mQueue.add(imageRequest);
									}
									else
										productHeadIV
										.setImageResource(R.drawable.noimg);

									if(status!=null)
									if (status.contains("0"))
										productStatusTV.setText("未成熟");
									else
										productStatusTV.setText("已成熟");
									yy_proNameTV.setText(crop);
									yypd_localTV.setText(name1 + "-" + name2);

									JSONArray arrayVideos = diskInfor
											.getJSONArray("videos");
									if (arrayVideos.length() > 0) {
										for (int i = 0; i < arrayVideos
												.length(); i++) {
											JSONObject newsObject = (JSONObject) arrayVideos
													.get(i);
											String id = newsObject
													.getString("id");
											String position = newsObject
													.getString("position");
											String isdef = newsObject
													.getString("isdef");
											String eqtype = newsObject
													.getString("eqtype");
											String address = newsObject
													.getString("address");
											if (isdef.equals("1")) {
												QSPID = address;
												if(eqtype.equals("0"))
													isQiu=true;
												else
													isQiu=false;
											}
											
											String name = newsObject
													.getString("name");
											Videos videos = new Videos(id,
													position, isdef, eqtype,
													address, name);
											
											videoList.add(videos);
										}  //end for
										
										if (videoList.size() > 0){ 
										 if(QSPID.length()==0&&QSPID.equals(""))
											{
											 QSPID=videoList.get(0).getAddress();
												if(videoList.get(0).getEqtype().equals("0"))
													isQiu=true;
												else
													isQiu=false;
											}
											handVideo.sendEmptyMessage(2000);
										}
									} else {
										LoadingDialog.dissmissLoading();
										videoDirectionLL .setVisibility(View.GONE);
										product_video_control_view.setVisibility(View.GONE);
									} 
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("请重新打开...");
						}
					});
			mQueue.add(stringRequest);
		} catch (Exception e) {
		}
	} 
	private void initView() {
		// TODO Auto-generated method stub
		tab1Layout = (LinearLayout) findViewById(R.id.tab1);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3);
		tab4Layout = (LinearLayout) findViewById(R.id.tab4);
		tabIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
 
		gs_czLL=(LinearLayout)findViewById(R.id.gs_czLL); 
		gs_sx = (TextView) this.findViewById(R.id.gs_sx); 
		wdgsTV = (TextView) this.findViewById(R.id.wdgsTV); //我的故事
		wdgsIV= (ImageView) this.findViewById(R.id.wdgsIV);
		ssvdIV = (ImageView) this.findViewById(R.id.ssvdIV);
		lsllIV = (ImageView) this.findViewById(R.id.lsllIV);
		wzIV = (ImageView) this.findViewById(R.id.wzIV);
		ssvdTV = (TextView) this.findViewById(R.id.ssvdTV);
		lsllTV = (TextView) this.findViewById(R.id.lsllTV);
		wzTV = (TextView) this.findViewById(R.id.wzTV);
		yy_llWV = (WebView) this.findViewById(R.id.yy_llWV);
		
		
		TextView dsfhTV=(TextView)findViewById(R.id.dsfhTV);
		dsfhTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});

		yy_llWV.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		productHeadIV = (CircleImageView) findViewById(R.id.productHeadIV);
		productStatusTV = (TextView) findViewById(R.id.productStatusTV);
		yy_proNameTV = (TextView) findViewById(R.id.yy_proNameTV);
		yypd_localTV = (TextView) findViewById(R.id.yypd_localTV);

		product_video_up = (ImageButton) findViewById(R.id.product_video_up);
		 
		product_video_down = (ImageButton) findViewById(R.id.product_video_down);
		product_video_left = (ImageButton) findViewById(R.id.product_video_left);
		product_video_right = (ImageButton) findViewById(R.id.product_video_right);
		product_video_big = (ImageButton) findViewById(R.id.product_video_big);
		product_video_small = (ImageButton) findViewById(R.id.product_video_small); 
		
		video_ctFL=(FrameLayout)findViewById(R.id.video_ctFL); 
		yy_returnBtn = (ImageButton) this.findViewById(R.id.yy_returnBtn);
		yy_returnBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		yy_returnBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		}); 
		// 在线视频
		video_surfaceView = (SurfaceView) findViewById(R.id.yy_ssVV);
		int width = getScreenWidth();
		LayoutParams params = video_surfaceView.getLayoutParams();
		params.height = (int) (width * 0.75);
		LayoutParams param = video_ctFL.getLayoutParams();
		param.height = (int) (width * 0.75);
		video_ctFL.setLayoutParams(param);

		video_surfaceView.setLayoutParams(params);
		// 选择摄像头的方向
		videoDirectionLL = (LinearLayout) findViewById(R.id.videoDirectionLL); 
		yy_videoLL = (LinearLayout) findViewById(R.id.yy_videoLL); 
		// 绿色履历
		yy_jlLL = (LinearLayout) findViewById(R.id.yy_jlLL);
		// 我要采摘
		yy_czLL = (LinearLayout) findViewById(R.id.yy_czLL); 
	} 
	private void initListener() {
		// TODO Auto-generated method stub
		// 弹出选择不同方向的框
		videoDirectionLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showPopu();
			}
		});
		yy_videoLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clear();
				ssvdIV.setImageResource(R.drawable.sp01);
				ssvdTV.setTextColor(getResources().getColor(R.color.font_color));
				yy_llWV.setVisibility(View.GONE);
				// video_surfaceView.setVisibility(View.VISIBLE);
				if(videoList.size()>0)
				videoDirectionLL.setVisibility(View.VISIBLE);
			}
		});
		// 绿色履历
		yy_jlLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clear();
				lsllIV.setImageResource(R.drawable.ll01);
				lsllTV.setTextColor(getResources().getColor(R.color.font_color));
				yy_llWV.setVisibility(View.VISIBLE);
				//设置WebView全屏
				LayoutParams wparams = yy_llWV.getLayoutParams();
				wparams.height = LayoutParams.MATCH_PARENT ;
		        yy_llWV.setLayoutParams(wparams);
				// video_surfaceView.setVisibility(View.GONE);
				videoDirectionLL.setVisibility(View.GONE);

				WebSettings wSet = yy_llWV.getSettings();
				wSet.setJavaScriptEnabled(true);

				yy_llWV.loadUrl(GetURL.ProductLL + Shared.getUserID()
						+ "&dkid=" + dkid);

				yy_llWV.setWebViewClient(new WebViewClient() {
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
						view.loadUrl(url);
						return true;
					}

					@Override
					public void onPageFinished(WebView view, String url) {
						super.onPageFinished(view, url);
					}

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap favicon) {
						super.onPageStarted(view, url, favicon);
					}
				});

			}
		});
		
		//我的故事
		gs_czLL.setOnClickListener(new OnClickListener() {
        	@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clear();
				wdgsIV.setImageResource(R.drawable.gs01);
				wdgsTV.setTextColor(getResources().getColor(R.color.font_color));
				yy_llWV.setVisibility(View.VISIBLE);
				//设置WebView全屏
				LayoutParams wparams = yy_llWV.getLayoutParams();
				wparams.height = wparams.FILL_PARENT ;
		        yy_llWV.setLayoutParams(wparams);
				// video_surfaceView.setVisibility(View.GONE);
				videoDirectionLL.setVisibility(View.GONE);

				WebSettings wSet = yy_llWV.getSettings();
				wSet.setJavaScriptEnabled(true);

				yy_llWV.loadUrl(GetURL.MyStory + Shared.getUserID()
						+ "&dkid=" + dkid);

				yy_llWV.setWebViewClient(new WebViewClient() {
					public boolean shouldOverrideUrlLoading(WebView view,
							String url) {
						view.loadUrl(url);
						return true;
					}

					@Override
					public void onPageFinished(WebView view, String url) {
						super.onPageFinished(view, url);
					}

					@Override
					public void onPageStarted(WebView view, String url,
							Bitmap favicon) {
						super.onPageStarted(view, url, favicon);
					}
				});
			}
		});

		// 我要采摘
		yy_czLL.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				clear();
				wzIV.setImageResource(R.drawable.wozhai01);
				wzTV.setTextColor(getResources().getColor(R.color.font_color));

				if(MyApplication.app.servers.size()==0)
					getServices();
				if (MyApplication.app.servers.size() > 0) {
					for (int i = 0; i < MyApplication.app.servers.size(); i++) {
						if (MyApplication.app.servers.get(i).isOnduty()
								&& MyApplication.app.servers.get(i).getId()
										.length() > 0) {
							Intent inte = new Intent(
									ProductDetailActivity.this,
									OnLineActivity.class);
							inte.putExtra("otherID", MyApplication.app.servers
									.get(i).getId());
							inte.putExtra("otherName",
									MyApplication.app.servers.get(i).getName());
							inte.putExtra("titleName", "在线客服");
							inte.putExtra("headPic", MyApplication.app.servers
									.get(i).getUheadpic());
							inte.putExtra("phone", MyApplication.app.servers
									.get(i).getPhone());
							inte.putExtra("dkid", dkid);
							startActivity(inte);
							finish();
							return;
						}
					
					}
					showToast("目前还没有客服人员， 请稍后再联系...");
				} 
			}
		});

		// 视频操作监听
		product_video_up.setOnTouchListener(new OnTouchListener() {
			
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

		product_video_down.setOnTouchListener(new OnTouchListener() {
			
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

		product_video_left.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
//				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					cotorlDirector(3, false, true);
				}
				
				if (event.getAction() == MotionEvent.ACTION_UP) {
					cotorlDirector(3, true, false);
				}
				 
				return false;
			}
		});

		product_video_right.setOnTouchListener(new OnTouchListener() {
			
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
		product_video_big.setOnTouchListener(new OnTouchListener() { 
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

		product_video_small.setOnTouchListener(new OnTouchListener() {
			
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
		// 模块跳转
		tab1Layout.setOnClickListener(new NewIVListener());
		tab2Layout.setOnClickListener(new NewIVListener());
		tab3Layout.setOnClickListener(new NewIVListener());
		tab4Layout.setOnClickListener(new NewIVListener());
	}

	
	
	private void getServices() {
		// TODO Auto-generated method stub
		//GETSERVERS
		try{
			StringRequest stringRequest = new StringRequest(GetURL.GETSERVERS,  
                    new Response.Listener<String>() {  
                        @Override  
                        public void onResponse(String response) { 
                        		JSONObject j;
								try {
									//"id":"40288ca151be33c00151be3b68f1000b","onduty":true,"phone":"13333262005","name":"马利强","role":"客服人员","uheadpic":""
							      j = new JSONObject(response);
									if (j.getString("code").equals("200")) {
										MyApplication.app.servers.clear();
									             JSONArray jsonArray=j.getJSONArray("result");
									             for (int i=0;i<jsonArray.length();i++) {
									            	 JSONObject newObject=(JSONObject)jsonArray.get(i);
									            	    String id=newObject.getString("id"); 
									            	    
									            	    //Log.i("++++++++++++++++++", id);
									            	    
														boolean onduty=newObject.getBoolean("onduty");
														String phone=newObject.getString("phone");
														String name=newObject.getString("name");
														String role=newObject.getString("role");
														String uheadpic=newObject.getString("uheadpic");
														Contactor contactor=new Contactor(id, uheadpic, name, role, onduty, phone);
														MyApplication.app.servers.add(contactor);
												}
									             
									} 
									else
									{
										//showToast("请求失败，返回码："+j.getString("code"));
									}
								
				                        } catch (JSONException e) {
				                            e.printStackTrace();	
				                          }
                        }  
                    }, new Response.ErrorListener() {  
                        @Override  
                        public void onErrorResponse(VolleyError error) {  
                           // Log.e("eeeeeeee", error.getMessage(), error);  
                        }  
                    });  
			
			  mQueue.add(stringRequest);  
			  
		    }catch(Exception w)
		    {
		    	 		
		    }
	}
	protected void showPopu() {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(this).inflate(R.layout.pop_list, null);
		int height = 0;
		int size = videoList.size();
		switch (size) {
		case 1:
			height = 120;
			break;
		case 2:
			height = 240;
			break;
		case 3:
			height = 360;
			break;
		case 4:
			height = 480;
			break;
		default:
			height = 240;
			break;
		}
		final PopupWindow pop = new PopupWindow(view,
				videoDirectionLL.getWidth(), height, false);
		// 需要设置一下此参数，点击外边可消失
		pop.setBackgroundDrawable(new BitmapDrawable());
		// 设置点击窗口外边窗口消失
		pop.setOutsideTouchable(true);
		// 设置此参数获得焦点，否则无法点击
		pop.setFocusable(true);
		ListView listView = (ListView) view.findViewById(R.id.pop_listView);
		ArrayList<PopListInfo> list = new ArrayList<PopListInfo>();
		for (int i = 0; i < size; i++) {
			PopListInfo info = new PopListInfo();
			info.setVideos(videoList.get(i));
			list.add(info);
		}
		if (list.size() > 0) {
			PopListAdapter adapter = new PopListAdapter(list, this);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub 
					Videos videos = videoList.get(position);
					changeVideo(videos); 
					pop.dismiss();
				}
			});
			pop.showAsDropDown(videoDirectionLL, 0, -(videoDirectionLL.getHeight() + height));
		}
		else
			 showToast("此处还未有摄像头...");
	}
	public void Logout()
    { 
    	int nRet = IDpsdkCore.DPSDK_Logout(m_ReValue.nReturnValue, 30000);
    }
	private void closeMove() {
		int ret = IDpsdkCore.DPSDK_CloseRealStreamBySeq(m_pDLLHandle, m_nSeq,
				mTimeOut);
		if (ret == 0) {
			//Log.e("zxw", "DPSDK_CloseRealStreamByCameraId success!");
		} else {
			//Log.e("zxw", "DPSDK_CloseRealStreamByCameraId failed! ret = " + ret);
		}
		StopRealPlay(); 
		product_video_control_view.setVisibility(View.INVISIBLE);
	}
	protected void changeVideo(Videos videos) {
		// TODO Auto-generated method stub
		QSPID = videos.getAddress();
		if(videos.getEqtype().equals("0"))
			isQiu=true;
		else
			isQiu=false;
		closeMove();
		LoadingDialog.showLoading(this);
		showVideo(); 
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

	public void clear() {
		ssvdIV.setImageResource(R.drawable.sp);
		lsllIV.setImageResource(R.drawable.ll);
		wzIV.setImageResource(R.drawable.wozhai);
		wdgsIV.setImageResource(R.drawable.gs);
		
		wdgsTV.setTextColor(Color.rgb(129, 129, 129));
		ssvdTV.setTextColor(Color.rgb(129, 129, 129));
		lsllTV.setTextColor(Color.rgb(129, 129, 129));
		wzTV.setTextColor(Color.rgb(129, 129, 129));
	}

	class NewIVListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub

			switch (view.getId()) {
			case R.id.tab1:
				tabIntent.putExtra("tag", 0);
				startActivity(tabIntent);
				finish();
				break;
			case R.id.tab2:
				tabIntent.putExtra("tag", 1);
				startActivity(tabIntent);
				finish();
				break;
			case R.id.tab3:
				tabIntent.putExtra("tag", 2);
				startActivity(tabIntent);
				finish();
				break;
			case R.id.tab4:
				tabIntent.putExtra("tag", 3);
				startActivity(tabIntent);
				finish();
				break;
		
			default:
				break;
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 屏蔽返回键
		switch(keyCode){
//		case KeyEvent.KEYCODE_HOME:return true;
		case KeyEvent.KEYCODE_BACK:finish();return true;
//		case KeyEvent.KEYCODE_CALL:return true;
//		case KeyEvent.KEYCODE_SYM: return true;
//		case KeyEvent.KEYCODE_VOLUME_DOWN: return true;
//		case KeyEvent.KEYCODE_VOLUME_UP: return true;
//		case KeyEvent.KEYCODE_STAR: return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
