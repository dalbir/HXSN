package com.hxsn.intelliwork.login;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.hxsn.intelliwork.R;
import com.hxsn.intelliwork.base.BaseActivity;
import com.hxsn.intelliwork.myviews.LoadingDialog;
import com.hxsn.intelliwork.myviews.ViewfinderView;
import com.hxsn.intelliwork.utils.CameraManager;
import com.hxsn.intelliwork.utils.CaptureActivityHandler;
import com.hxsn.intelliwork.utils.GetURL;
import com.hxsn.intelliwork.utils.InactivityTimer;
import com.hxsn.intelliwork.utils.RGBLuminanceSource;
import com.hxsn.intelliwork.utils.Shared;



/**
 * Initial the camera
 * 
 */
@SuppressLint("HandlerLeak")
public class CaptureActivity extends BaseActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	int ifOpenLight = 0; 
    static String strings="";
     static int count=1;
     String uid="";
     TextView camerTitleTV;
 	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.camera);
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		camerTitleTV= (TextView) findViewById(R.id.camerTitleTV);
		camerTitleTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CaptureActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
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
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent(CaptureActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * Handler scan result
	 * 
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String resultString = result.getText();
		// FIXME
		if (resultString.equals("")) {
			Toast.makeText(CaptureActivity.this,  "扫描失败!", Toast.LENGTH_SHORT)
					.show();
		} else {
			// System.out.println("Result:"+resultString);
		//	Intent resultIntent = new Intent(this,WebActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putString("result", resultString);
//			resultIntent.putExtras(bundle);
//			this.setResult(RESULT_OK, resultIntent);
			
//			strings+=(resultString);
//			if(resultString.endsWith("end"))
//			{
//			resultIntent.putExtra("result",strings);
//			resultIntent.putExtra("count", count);
//			startActivity(resultIntent);
//		    strings="";
//		    count=1;
			showToast(resultString);
			requtst(resultString);
			CaptureActivity.this.finish();
			}
//			else
//			{
//				count++;
//				AlertDialog.Builder builder = new AlertDialog.Builder(this);
//				builder.setCancelable(false);
//				builder.setTitle("扫描结果")  
//                .setMessage("扫描成功，继续下一个")  
//                .setPositiveButton("确认", new DialogInterface.OnClickListener() {  
//                   @Override  
//                    public void onClick(DialogInterface dialog, int which) {  
//                		Intent openCameraIntent = new Intent(CaptureActivity.this,
//        						CaptureActivity.class);
//                		startActivity(openCameraIntent);
//                   }  
//                }).show();  
//				
//				
//			}
		}
		
		

	
	public void requtst(String logcode)
	{LoadingDialog.showLoading(this);
		try{
			Shared.setCode(logcode);
			StringRequest stringRequest = new StringRequest(GetURL.LOGIN+"logcode="+logcode,  
                    new Response.Listener<String>() {  
                        @Override  
                        public void onResponse(String response) { 
                        		JSONObject j;
                        		try {
							      j = new JSONObject(response);
									if (j.getString("code").equals("200")) {
									             uid=j.getString("result");
									             Shared.setUserID(uid);
									             Intent inte = new Intent(CaptureActivity.this, MainActivity.class);
												 startActivity(inte);
									} 
									else
									{
										showToast("登录失败，返回码："+j.getString("code"));
									}
									LoadingDialog.dissmissLoading();
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

				mProgress = new ProgressDialog(CaptureActivity.this);
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
				if (resultString.equals("")) {
					Toast.makeText(CaptureActivity.this, "扫描失败!",
							Toast.LENGTH_SHORT).show();
				} else {
					// System.out.println("Result:"+resultString);
					Intent resultIntent = new Intent();
					Bundle bundle = new Bundle();
					bundle.putString("result", resultString);
					resultIntent.putExtras(bundle);
					CaptureActivity.this.setResult(RESULT_OK, resultIntent);
				}
				CaptureActivity.this.finish();
				break;

			case 2:
				mProgress.dismiss();
				Toast.makeText(CaptureActivity.this, "解析错误！", Toast.LENGTH_LONG)
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
	 * 目前识别度不高，有待改进
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
			handler = new CaptureActivityHandler(this, decodeFormats,
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
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

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
					R.raw.beep);
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

}