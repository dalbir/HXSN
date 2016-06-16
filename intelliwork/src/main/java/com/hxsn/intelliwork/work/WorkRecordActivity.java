package com.hxsn.intelliwork.work;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.hxsn.intelliwork.MainActivity;
import com.hxsn.intelliwork.MyApplication;
import com.hxsn.intelliwork.R;
import com.hxsn.intelliwork.base.BaseActivity;
import com.hxsn.intelliwork.myviews.LoadingDialog;
import com.hxsn.intelliwork.myviews.ViewfinderView;
import com.hxsn.intelliwork.utils.CameraManager;
import com.hxsn.intelliwork.utils.DataTransfer;
import com.hxsn.intelliwork.utils.InactivityTimer;
import com.hxsn.intelliwork.utils.RGBLuminanceSource;
import com.hxsn.intelliwork.utils.WorkRecordActivityHandler;
import com.senter.support.openapi.StUhf.InterrogatorModelC;
import com.senter.support.openapi.StUhf.UII;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Vector;


/**
 * Initial the camera
 * 
 */
@SuppressLint({ "HandlerLeak"})
public class WorkRecordActivity extends BaseActivity implements Callback {

	private WorkRecordActivityHandler handler;
	private ViewfinderView wrviewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	public static boolean isFarm=false;
	

	private LinearLayout workCX_layout;
	private LinearLayout reidLayout;
	private FrameLayout mianLayout;
	int ifOpenLight = 0; 
	static String strings="";
     static int count=1;
     String uid="";
     boolean isType;
     private TextView wrcamerTitleTV;
     
     private String TAG = "WorkRecordActivity";

    //高频扫码判断
    private  TextView t;
     private String text="";
 	private String intentText="";
    boolean RFIDflag=false;
    private String idStrings="";
    String[] data=null;
 	/** Called when the activity is first created. */
    
 
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camers_wrecord);
		
		wrviewfinderView = (ViewfinderView) findViewById(R.id.wrviewfinder_view);
		wrviewfinderView.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		CameraManager.init(getApplication());
		
		WindowManager wm = (WindowManager)this
		         .getSystemService(Context.WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		mianLayout = (FrameLayout) findViewById(R.id.mainLayout);
	
		reidLayout  = new LinearLayout(this);
		reidLayout.setPadding(0, height-240, 0, 0);
		reidLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		t  =  new TextView(this);
		t.setGravity(Gravity.CENTER_HORIZONTAL);
		t.setText(text);
		t.setTextColor(Color.WHITE);
		t.setMovementMethod(ScrollingMovementMethod.getInstance());
		reidLayout.addView(t);
	    mianLayout.addView(reidLayout);
		
	    workCX_layout = (LinearLayout) findViewById(R.id.workCX_layout);
		wrcamerTitleTV=(TextView) findViewById(R.id.wrcamerTitleTV);
		if(isFarm) {
			wrcamerTitleTV.setText("收获扫描");
		} else {
			wrcamerTitleTV.setText("作业登记");
			if (MyApplication.getRfid() != null){
			LoadingDialog.showLoading(WorkRecordActivity.this,
					"正在初始化RFID");   
			rfidRun();
			}
		}
			hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		wrcamerTitleTV.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent inte=new Intent(WorkRecordActivity.this,MainActivity.class);
				inte.putExtra("tag", 0);
				startActivity(inte);
				finish();
			}
		});
		
		workCX_layout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent inte = new Intent(WorkRecordActivity.this, WorkSelectActivity.class);
				startActivity(inte);
			}
		});

		//识别与记录RFIDS
		
		
	/*	listView_data=(ListView) findViewById(R.id.listView_data);
		data=new String[] {"201602220","201602221","201602222","201602223","201602223","201602223"}; 
		ArrayList<Map<String, Object>> listMap=new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();
		Map<String, Object> map3 = new HashMap<String, Object>();
		Map<String, Object> map4 = new HashMap<String, Object>();
		Map<String, Object> map5 = new HashMap<String, Object>();
		map.put("EPC", "201602220");
		listMap.add(map);
		map1.put("EPC", "201602221");
		listMap.add(map1);
		map2.put("EPC", "201602222");
		listMap.add(map2);
		map3.put("EPC", "201602223");
		listMap.add(map3);
		map4.put("EPC", "201602224");
		listMap.add(map4);
		map5.put("EPC", "201602225");
		listMap.add(map5);
		
		listView_data.setAdapter(new SimpleAdapter(WorkRecordActivity.this,
				listMap, R.layout.rfid_item, 
				new String[]{"EPC"}, 
				new int[]{R.id.textView_epc}));
		listView_data.setVisibility(View.INVISIBLE);

		*/
	}
	
//高频扫码线程
	class InventoryThread extends Thread{
		public void run() {
			while(true){
						
						}
				}
			}
	
	

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		
	
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.wrpreview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL){
			playBeep = false;
		}
		initBeepSound();
	
		vibrate = true;

		
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyApplication.stop();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		MyApplication.stop();
		super.onDestroy();
	}
	
	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	@SuppressWarnings("deprecation")
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		
		try {
			resultString = URLDecoder.decode(resultString,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultString = "";
		}
		
		// FIXME
		if (resultString.equals("")) {
			Toast.makeText(WorkRecordActivity.this,  "扫描失败!", Toast.LENGTH_SHORT)
					.show();
		} else 	if(isFarm)
		{
			Intent resultIntent = new Intent(this,WorkCZActivity.class);	
			resultIntent.putExtra("code",resultString);
			startActivity(resultIntent);
			WorkRecordActivity.this.finish();
		}
		else
		{
			Intent resultIntent = new Intent(this,WorkDJActivity.class);	
			strings+=(resultString);
		
			if(resultString.contains("HJ")||resultString.contains("hj"))
			{
				if(RFIDflag){
					 
					if(strings!=null){
					strings=intentText.substring(0, intentText.length()-1)+strings;
					}
					idStrings+=strings;
				
					resultIntent.putExtra("RFIDS",idStrings);
					strings="";
				}
				else{
			resultIntent.putExtra("result",strings);
				}
				startActivity(resultIntent);
		    strings="";
		    RFIDflag=false;
		    count=1;
			WorkRecordActivity.this.finish();
			}
			else
			{
				count++;
				wrviewfinderView.setName(resultString);
				
				MyApplication.stop();
				if (handler != null) {
					handler.quitSynchronously();
					handler = null;
				}
				CameraManager.get().closeDriver();
				
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setCancelable(false);
				builder.setTitle("扫描结果")  
                .setMessage("扫描成功，继续下一个")  
                .setPositiveButton("是", new DialogInterface.OnClickListener() {  
                   @Override
                    public void onClick(DialogInterface dialog, int which) {  
                	   SurfaceView surfaceView = (SurfaceView) findViewById(R.id.wrpreview_view);
       				SurfaceHolder surfaceHolder = surfaceView.getHolder();
       				if (hasSurface) {
       					initCamera(surfaceHolder);
       				} else {
       					surfaceHolder.addCallback(WorkRecordActivity.this);
       					surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
       				}
       				decodeFormats = null;
       				characterSet = null;

       				playBeep = true;
       				AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
       				if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL){
       					playBeep = false;
       				}
       				initBeepSound();
       			
       				vibrate = true;
                   }  
                }).show();  
				
				
			}
		}
	}
	/*
	 * 获取带二维码的相片进行扫描
	 */
	public void pickPictureFromAblum(View v) {
		// 打开手机中的相册
		Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
		innerIntent.setType("image/*");
		Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
		this.startActivityForResult(wrapperIntent, 1);
	}

	String  photo_path;
	ProgressDialog mProgress;
	Bitmap scanBitmap;


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				// 获取选中图片的路径
				Cursor cursor = getContentResolver().query(data.getData(),
						null, null, null, null);
				if (cursor.moveToFirst()) {
					photo_path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					Log.i("路径", photo_path);
				}
				cursor.close();

				mProgress = new ProgressDialog(WorkRecordActivity.this);
				mProgress.setMessage("正在扫描...");
				mProgress.setCancelable(false);
				mProgress.show();

				new Thread(new Runnable() {
					@Override 
					public void run() {
						Result result = scanningImage(photo_path);
						if (result != null) {
							Message m = mHandler.obtainMessage();
							m.what = 1;
							m.obj = result.getText();
							mHandler.sendMessage(m);
						} else {
							Message m = mHandler.obtainMessage();
							m.what = 2;
							m.obj = "Scan failed!";
							mHandler.sendMessage(m);
						}

					}
				}).start();
				break;

			default:
				break;
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	final Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {
			case 1:
				mProgress.dismiss();
				String resultString = msg.obj.toString();
				showToast("Result:"+resultString);
				if (resultString.equals("")) {
					Toast.makeText(WorkRecordActivity.this, "扫描失败!",
							Toast.LENGTH_SHORT).show();
				} else {
					// System.out.println("Result:"+resultString);
					
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("result", resultString);
					resultIntent.putExtras(bundle);
					WorkRecordActivity.this.setResult(RESULT_OK, resultIntent);
				}
				WorkRecordActivity.this.finish();
				break;

			case 2:
				mProgress.dismiss();
				Toast.makeText(WorkRecordActivity.this, "解析错误！", Toast.LENGTH_LONG)
						.show();

				break;
			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

	/**
	 * 扫描二维码图片的方法
	 * 
	 * 
	 * @param path
	 * @return
	 */
	public Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {
			return null;
		}
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF8"); 

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; 
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; 
		int sampleSize = (int) (options.outHeight / (float) 100);
		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);
		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {
			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (ChecksumException e) {
			e.printStackTrace();
		} catch (FormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public void IfOpenLight(View v) {
		ifOpenLight++;

		switch (ifOpenLight % 2) {
		case 0:
			
			CameraManager.get().closeLight();
			break;

		case 1:
			
			CameraManager.get().openLight();
			break;
		default:
			break;
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new WorkRecordActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return wrviewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		wrviewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.tag_inventoried);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}
	private boolean rfidInit=false;
	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};
		/**
		 * REID
		 * */
	private boolean type=true;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == 222) {
			if(rfidInit&&type){
				type=false;
				uiOnInverntryButton();
				}
			return RFIDflag;
		}
		return super.onKeyDown(keyCode, event);
	};
	
	private  void uiOnInverntryButton()
	{
		runOnUiThread(new Runnable() {
				
				@Override
				public void run()
				{
					new Thread() {
						public void run()
						{
							UII uii = null;
				
							uii = startInventorySingleStep();
							
							if (uii != null)
							{	type=true;
							    RFIDflag=true;
								MyApplication.stop();
						    		playBeepSoundAndVibrate();
						    		String s=DataTransfer.xGetString(uii.getBytes()).replaceAll(" ", "");
						    		intentText+=s+",";
						    		byte[] sByte = s.getBytes();
						    		try {
										s = new String(sByte, "UTF-8");
									} catch (UnsupportedEncodingException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
						    		text=s+"\n"+text;
								 Message message = new Message();
								 message.what=1;
								 myHandler.sendMessage(message);
							}
							
						};
					}.start();
				}
			});
	}
	private void rfidRun(){
		new Thread(){
			@Override
			public void run() {
				super.run();
				Message msg =new Message();
				msg.what=2;
				while(!rfidInit){					
				if (MyApplication.getRfid() != null)
				{	
					MyApplication.getRfid().getInterrogatorModel();
					rfidInit=true;
					msg.what=2;
					myHandler.sendMessage(msg);
				}
				else
				{
					rfidInit=true;
				}
				}
			
		};
	}.start();
	}
	protected UII startInventorySingleStep()
	{
	
		return MyApplication.rfid().getInterrogatorAs(InterrogatorModelC.class).inventorySingleStep();
	}

	
	Handler myHandler =new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==1){
				t.setText(text);
		
			}
			if(msg.what==2){
				LoadingDialog.dissmissLoading();
			}
		
		}
	};

}