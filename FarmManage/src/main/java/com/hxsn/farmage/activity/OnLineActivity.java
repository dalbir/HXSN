package com.hxsn.farmage.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hxsn.farmage.MyApplication;
import com.hxsn.farmage.R;
import com.hxsn.farmage.adapter.ChatMsgViewAdapter;
import com.hxsn.farmage.base.BaseFragmentActivity;
import com.hxsn.farmage.beans.ChatMsgEntity;
import com.hxsn.farmage.beans.Emoji;
import com.hxsn.farmage.utils.EmojiUtil;
import com.hxsn.farmage.fragment.FaceFragment;
import com.hxsn.farmage.myview.AudioRecorderButton;
import com.hxsn.farmage.myview.AudioRecorderButton.AudioFinishRecorderListener;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.LogUtil;
import com.hxsn.farmage.utils.SDKCoreHelper;
import com.hxsn.farmage.utils.Shared;
import com.hxsn.farmage.myview.XListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

@SuppressLint({ "InflateParams", "ClickableViewAccessibility", "HandlerLeak", "UseSparseArrays" })
public class OnLineActivity extends BaseFragmentActivity implements
		OnClickListener, AudioFinishRecorderListener, FaceFragment.OnEmojiClickListener, 
		XListView.IXListViewListener  {

	// 初始化控件
	private Button cai_pbackBtn;
	private Button czo_telBtn;
	private XListView listview;
	private ImageView yuyin_ima, biaoqing_ima, wenjianxuanze_ima;
	private Button sendmes_btn;
	private AudioRecorderButton anzhushuohua_btn;
	private View fengexian_view;
	private LinearLayout shuru_lin;
	// 状态开关
	private boolean isOpenYunYing = false;
	public static EditText neirong_ed;

	public static OnLineActivity onLineActivity = null;

	private List<ChatMsgEntity> mDataArrays = null;
	private ChatMsgViewAdapter mAdapter = null;
	private TextView online_title_name;
	// 表情的布局
	// 表情的文件名字
	private LinearLayout main_qiehuan;
	private boolean isOpenbiaoqing = false;
	// 表情和文件的linlayout切换的布局
	private LinearLayout wenjian_lin;
	private LinearLayout biaoqin_lin;
	// 输入法的管理器
	private InputMethodManager imm;
	// 消息的类型的选择
	private LinearLayout tupianxiaoxi_lin, xiaoshipinxiaoxi_lin,
			wenjianxiaoxi_lin, xiaoxizhidingxiaoxi_lin, shoucangxiaoxi_lin,
			yuyinxiaoxi_lin;
	// 消息类型的ImageView
	private ImageView tupian_mess_ima;

	private String otherID = null;
	private String otherName = null;
	private String titleName = null;
	@SuppressWarnings("unused")
	private String headPic = null;
	private String phone=null;
    @SuppressWarnings("unused")
	private String dkid=null;
    private int num = 2;
   
	/***
	 * 发送内容的处理
	 */
	public static HashMap<Integer, String> hasp = new HashMap<Integer, String>();
	@SuppressLint("UseSparseArrays")
	public static ArrayList<Integer> jiaobiao = new ArrayList<Integer>();

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_online);
		onLineActivity = this;
		setTAG("OnLineActivity");
		init();
		initView();
		initListener();
		
		initChat();
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		onLineActivity = null;
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		onLineActivity = this;
		super.onRestart();
	}

	private void init() {
		// TODO Auto-generated method stub
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		Intent intent = getIntent();
		otherID = intent.getStringExtra("otherID");
		otherName = intent.getStringExtra("otherName");
		titleName = intent.getStringExtra("titleName");
		headPic = intent.getStringExtra("headPic");
		phone=intent.getStringExtra("phone");
		dkid=intent.getStringExtra("dkid");
		int nums = MyApplication.app.dbForQQ.queryRemind(otherID);
		MyApplication.app.dbForQQ.upDataRemind(otherID, true);
		if (nums > 2) {
			num = nums;
		}
		
		if (otherID == null || !(otherID.length() > 0) || otherName == null
				|| !(otherName.length() > 0) || titleName == null
				|| !(titleName.length() > 0)) {
			showToast("用户信息异常");
			finish();
			return;
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		cai_pbackBtn = (Button) findViewById(R.id.cai_pbackBtn);
		czo_telBtn = (Button) findViewById(R.id.czo_telBtn);
		
		online_title_name = (TextView) findViewById(R.id.online_title_name);

		// 发送消息类型的ImageView
		tupian_mess_ima = (ImageView) findViewById(R.id.tupian_mess_ima);
		// 获取文件类型的代码
		tupianxiaoxi_lin = (LinearLayout) findViewById(R.id.xiaxi_tupian_lin);
		xiaoshipinxiaoxi_lin = (LinearLayout) findViewById(R.id.xiaoxi_xiaoshipin_lin);
		wenjianxiaoxi_lin = (LinearLayout) findViewById(R.id.xiaoxi_wenjian_lin);
		xiaoxizhidingxiaoxi_lin = (LinearLayout) findViewById(R.id.xiaoxi_xiaoxizhiding_lin);
		shoucangxiaoxi_lin = (LinearLayout) findViewById(R.id.xiaoxi_wodeshoucang_lin);
		yuyinxiaoxi_lin = (LinearLayout) findViewById(R.id.yuyinxiaoxi_lin);
		// 表情选择代码
		// 初始化表情的
		main_qiehuan = (LinearLayout) findViewById(R.id.layout);
		wenjian_lin = (LinearLayout) findViewById(R.id.wenjian_lin_i);

		// 表情的布局
		biaoqin_lin = (LinearLayout) findViewById(R.id.biaoqing_lin_i);
		listview = (XListView) findViewById(R.id.liaotian_listview);
		yuyin_ima = (ImageView) findViewById(R.id.yuyin_ima);
		biaoqing_ima = (ImageView) findViewById(R.id.biaoqing_ima);
		wenjianxuanze_ima = (ImageView) findViewById(R.id.wenjianxuanze_ima);
		sendmes_btn = (Button) findViewById(R.id.sendmessage_btn);
		anzhushuohua_btn = (AudioRecorderButton) findViewById(R.id.anzhushuohua_btn);
		fengexian_view = findViewById(R.id.fengexian_view);
		shuru_lin = (LinearLayout) findViewById(R.id.shuruneirong_layout);
		neirong_ed = (EditText) findViewById(R.id.neirong_ed);
		
		TextView dsfhTV=(TextView)findViewById(R.id.dsfhTV);
		dsfhTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(neirong_ed.getWindowToken(), 0);
				}
			}
		});
		
		mDataArrays = new ArrayList<ChatMsgEntity>();
		//通知：    "'^^':{"type":"1","text":"梨子熟了","dkcode":"G0101"}";
		//视频切换："'^^':{"type":"3","text":[{"id":"1212","name":"kata","position":"南","isdef":"1","eqtype":"1","address":"1000000$1$0$0"},{"id":"1213","name":"peter","position":"北","isdef":"0","eqtype":"0","address":"1000001$1$0$0"}]}";
		   //	String msg="'^^':{\"type\":\"3\",\"text\":[{\"id\":\"1212\",\"name\":\"kata\",\"position\":\"南\",\"isdef\":\"1\",\"eqtype\":\"2\",\"address\":\"1000134$1$0$0\"},{\"id\":\"1213\",\"name\":\"peter\",\"position\":\"北\",\"isdef\":\"0\",\"eqtype\":\"0\",\"address\":\"1000001$1$0$0\"}]}";
		   //通知：    '^^':{"type":"1","text":"梨子熟了","dkcode":"G0101"}
		   //报警信息          '^^':{"type":"2","text":"牛该喝水了"}
		  //视频切换：    '^^':{\"type\":\"3\",\"text\":[{\"id\":\"1212\",\"name\":\"kata\",\"position\":\"南\",\"isdef\":\"1\",\"eqtype\":\"2\",\"address\":\"1000134$1$0$0\"},{\"id\":\"1213\",\"name\":\"peter\",\"position\":\"北\",\"isdef\":\"0\",\"eqtype\":\"0\",\"address\":\"1000001$1$0$0\"}]}
	     //	String msg="'^^':{\"type\":\"1\",\"text\":\"梨子熟了\",\"dkcode\":\"G0101\"}";
	     //	String msg="'^^':{\"type\":\"3\",\"text\":[{\"id\":\"1212\",\"name\":\"kata\",\"position\":\"南\",\"isdef\":\"1\",\"eqtype\":\"2\",\"address\":\"1000134$1$0$0\"},{\"id\":\"1213\",\"name\":\"peter\",\"position\":\"北\",\"isdef\":\"0\",\"eqtype\":\"0\",\"address\":\"1000001$1$0$0\"}]}";
       // neirong_ed.setText(msg);
		FaceFragment faceFragment = FaceFragment.Instance();
		getSupportFragmentManager().beginTransaction().add(R.id.biaoqing_lin_i,faceFragment).commit();
		
		listview.addFooterView(getLayoutInflater().inflate(
				R.layout.bootom_layot, null));
		listview.setXListViewListener(this);
		listview.setPullLoadEnable(true);
		// 设置文件和表情布局的不可见
		main_qiehuan.setVisibility(View.GONE);
		online_title_name.setText(titleName);
	}

	private void initListener() {
		// TODO Auto-generated method stub
		cai_pbackBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		czo_telBtn.setOnTouchListener(ImgBtnEffact.btnTL);

		cai_pbackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(neirong_ed.getWindowToken(), 0);
				}
			}
		});

		czo_telBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(phone.length()>0)
				{
					Intent inte = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
							+ phone));
					startActivity(inte);
				}
				else
					Toast.makeText(getApplication(), "sorry,目前还没有电话联系...", Toast.LENGTH_LONG).show();
					}
		});

		tupian_mess_ima.setOnClickListener(this);
		anzhushuohua_btn.setAudioFinishRecorderListener(this);
		shuru_lin.setOnClickListener(this);
		sendmes_btn.setOnClickListener(this);
		wenjianxuanze_ima.setOnClickListener(this);
		biaoqing_ima.setOnClickListener(this);
		yuyin_ima.setOnClickListener(this);
		// 消息类型监听
		tupianxiaoxi_lin.setOnClickListener(this);
		xiaoshipinxiaoxi_lin.setOnClickListener(this);
		wenjianxiaoxi_lin.setOnClickListener(this);
		xiaoxizhidingxiaoxi_lin.setOnClickListener(this);
		shoucangxiaoxi_lin.setOnClickListener(this);
		yuyinxiaoxi_lin.setOnClickListener(this);

		neirong_ed.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				Log.i("xxxxx", "改变啦----" + s.toString());
				if (s.length() > 0) {
					sendmes_btn.setVisibility(View.VISIBLE);
					wenjianxuanze_ima.setVisibility(View.GONE);
				} else {
					sendmes_btn.setVisibility(View.GONE);
					wenjianxuanze_ima.setVisibility(View.VISIBLE);
				}
				Log.i("TAG", "改变时：");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				Log.i("TAG", "改变前：" + s);
			}

			@Override
			public void afterTextChanged(Editable s) {
				Log.i("TAG", "删除后：" + neirong_ed.getText().toString());
			}
		});
		neirong_ed.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if (isOpenbiaoqing == true || isWenjian == true) {
						if (isOpenbiaoqing == true) {
							biaoqing_ima
									.setImageResource(R.drawable.tongyong28);
							isOpenbiaoqing = false;
							isWenjian = false;
						}
						isOpenbiaoqing = false;
						isWenjian = false;
					}
					main_qiehuan.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_MOVE:
					main_qiehuan.setVisibility(View.GONE);
					break;
				case MotionEvent.ACTION_UP:
					main_qiehuan.setVisibility(View.GONE);
					break;
				}
				return false;
			}
		});
	}
	
	@Override
	public void onFinish(float seconds, String filePath) {
		// TODO Auto-generated method stub
		SDKCoreHelper.sendMessage(otherID, filePath, seconds, otherName);
	}

	@SuppressWarnings("static-access")
	public void sendVoiceSucceed(float seconds, String filePath) {
		LogUtil.showLog(TAG, "语音消息发送成功");
		ChatMsgEntity entity = new ChatMsgEntity();
		entity.setDate(getDate());
		entity.setId(otherID);
		entity.setName(Shared.getNickName());
		entity.setMsgType(false);
		entity.setType(ChatMsgEntity.VOICE);
		entity.setSingle(true);
		entity.setText(filePath);
		entity.setVoiceleng((int) seconds);
		mDataArrays.add(entity);
		if (mAdapter == null) {
			mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
			listview.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
		listview.setSelection(listview.getCount() - 1);
	}

	/**
	 * 获取当前的日期的方法
	 * 
	 * @return
	 */
	private String getDate() {
		Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int mins = c.get(Calendar.MINUTE);
		String monthS;
		String dayS;
		String hourS;
		String minsS;
		if (month < 10) {
			monthS = "0" + month;
		} else {
			monthS = "" + month;
		}
		if (day < 10) {
			dayS = "0" + day;
		} else {
			dayS = "" + day;
		}
		if (hour < 10) {
			hourS = "0" + hour;
		} else {
			hourS = "" + hour;
		}
		if (mins < 10) {
			minsS = "0" + mins;
		} else {
			minsS = "" + mins;
		}

		StringBuffer sbBuffer = new StringBuffer();
		sbBuffer.append(year + "/" + monthS + "/" + dayS + " " + hourS + ":"
				+ minsS);

		return sbBuffer.toString();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.yuyin_ima:
			Yuyin();
			break;
		case R.id.biaoqing_ima:
			BiaoQing();
			break;
		case R.id.wenjianxuanze_ima:
			WenJianXuanZe();
			break;
		case R.id.sendmessage_btn:
			SendMess();
			break;
		case R.id.tupian_mess_ima:
			MessPicture();
			break;
		}
	}

	/**
	 * 文件选择
	 */
	private boolean isWenjian = false;

	private void WenJianXuanZe() {
		// TODO Auto-generated method stub
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(neirong_ed.getWindowToken(), 0);
			if (isWenjian == false && isOpenbiaoqing == false) {
				main_qiehuan.setVisibility(View.VISIBLE);
				biaoqin_lin.setVisibility(View.GONE);
				wenjian_lin.setVisibility(View.VISIBLE);
				isWenjian = true;
			} else if (isWenjian == true && isOpenbiaoqing == true) {
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				main_qiehuan.setVisibility(View.GONE);
				isWenjian = false;
				isOpenbiaoqing = false;
			} else if (isWenjian == true && isOpenbiaoqing == false) {
				main_qiehuan.setVisibility(View.GONE);
				isWenjian = false;
			} else if (isWenjian == false && isOpenbiaoqing == true) {
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				wenjian_lin.setVisibility(View.VISIBLE);
				biaoqin_lin.setVisibility(View.GONE);
				isOpenbiaoqing = false;
				isWenjian = true;
			}

		} else {
			if (isWenjian == false && isOpenbiaoqing == false) {
				main_qiehuan.setVisibility(View.VISIBLE);
				biaoqin_lin.setVisibility(View.GONE);
				wenjian_lin.setVisibility(View.VISIBLE);
				isWenjian = true;
			} else if (isWenjian == true && isOpenbiaoqing == true) {
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				main_qiehuan.setVisibility(View.GONE);
				isWenjian = false;
				isOpenbiaoqing = false;
			} else if (isWenjian == true && isOpenbiaoqing == false) {
				main_qiehuan.setVisibility(View.GONE);
				isWenjian = false;
			} else if (isWenjian == false && isOpenbiaoqing == true) {
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				wenjian_lin.setVisibility(View.VISIBLE);
				biaoqin_lin.setVisibility(View.GONE);
				isOpenbiaoqing = false;
				isWenjian = true;
			}
		}

	}

	/**
	 * 表情选择
	 */
	private void BiaoQing() {
		// TODO Auto-generated method stub
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(neirong_ed.getWindowToken(), 0);
			if (isOpenbiaoqing == false && isWenjian == false) {
				main_qiehuan.setVisibility(View.VISIBLE);
				biaoqin_lin.setVisibility(View.VISIBLE);
				neirong_ed.setFocusable(true);
				neirong_ed.setFocusableInTouchMode(true);
				neirong_ed.requestFocus();
				wenjian_lin.setVisibility(View.GONE);
				biaoqing_ima.setImageResource(R.drawable.tongyong29);
				isOpenbiaoqing = true;
			} else if (isOpenbiaoqing == true && isWenjian == true) {
				main_qiehuan.setVisibility(View.GONE);
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				isOpenbiaoqing = false;
				isWenjian = false;
			} else if (isOpenbiaoqing == true && isWenjian == false) {
				main_qiehuan.setVisibility(View.GONE);
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				isOpenbiaoqing = false;
			} else if (isOpenbiaoqing == false && isWenjian == true) {
				biaoqing_ima.setImageResource(R.drawable.tongyong29);
				biaoqin_lin.setVisibility(View.VISIBLE);
				wenjian_lin.setVisibility(View.GONE);
				neirong_ed.setFocusable(true);
				neirong_ed.setFocusableInTouchMode(true);
				neirong_ed.requestFocus();
				isOpenbiaoqing = true;
				isWenjian = false;
			}
		} else {
			if (isOpenbiaoqing == false && isWenjian == false) {
				main_qiehuan.setVisibility(View.VISIBLE);
				biaoqin_lin.setVisibility(View.VISIBLE);
				wenjian_lin.setVisibility(View.GONE);
				biaoqing_ima.setImageResource(R.drawable.tongyong29);
				isOpenbiaoqing = true;
			} else if (isOpenbiaoqing == true && isWenjian == true) {
				main_qiehuan.setVisibility(View.GONE);
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				isOpenbiaoqing = false;
				isWenjian = false;
			} else if (isOpenbiaoqing == true && isWenjian == false) {
				main_qiehuan.setVisibility(View.GONE);
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				isOpenbiaoqing = false;
			} else if (isOpenbiaoqing == false && isWenjian == true) {
				biaoqing_ima.setImageResource(R.drawable.tongyong29);
				biaoqin_lin.setVisibility(View.VISIBLE);
				wenjian_lin.setVisibility(View.GONE);
				isOpenbiaoqing = true;
				isWenjian = false;
			}
		}

	}

	/**
	 * 语音选择
	 */
	private void Yuyin() {
		// TODO Auto-generated method stub
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(neirong_ed.getWindowToken(), 0);
		}
		if (isOpenbiaoqing == true || isWenjian == true) {
			if (isOpenbiaoqing == true) {
				biaoqing_ima.setImageResource(R.drawable.tongyong28);
				isOpenbiaoqing = false;
				isWenjian = false;
			}
			isOpenbiaoqing = false;
			isWenjian = false;
		}
		// 获取管理对象
		main_qiehuan.setVisibility(View.GONE);
		if (isOpenYunYing) {
			InputMethodManager im = (InputMethodManager) getApplicationContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			im.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

			yuyin_ima.setImageResource(R.drawable.tongyong27);
			shuru_lin.setVisibility(View.VISIBLE);
			neirong_ed.setFocusable(true);
			fengexian_view.setVisibility(View.VISIBLE);
			anzhushuohua_btn.setVisibility(View.GONE);
			String nei_str = neirong_ed.getText().toString();
			neirong_ed.setFocusable(true);
			neirong_ed.setFocusableInTouchMode(true);
			neirong_ed.requestFocus();
			neirong_ed.setSelection(nei_str.length());
			isOpenYunYing = false;
			listview.setSelection(listview.getCount() - 1);
		} else {
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(neirong_ed.getWindowToken(), 0);
			}
			yuyin_ima.setImageResource(R.drawable.tongyong31);
			shuru_lin.setVisibility(View.GONE);
			fengexian_view.setVisibility(View.GONE);
			anzhushuohua_btn.setVisibility(View.VISIBLE);
			isOpenYunYing = true;
		}
	}

	/**
	 * 选择图片的方法
	 */
	private ArrayList<String> mSelectPath;
	private static final int REQUEST_IMAGE = 2;

	private void MessPicture() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		// true 显示照相的功能 false不现实相机
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
		// 选择模式
		/**
		 * MultiImageSelectorActivity.MODE_MULTI -- 可以选择多张图片
		 * MultiImageSelectorActivity.MODE_SINGLE -- 只能显示一张图片
		 */
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE,
				MultiImageSelectorActivity.MODE_MULTI);
		// 默认选择
		// if (mSelectPath != null && mSelectPath.size() > 0)
		// {
		// intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
		// mSelectPath);
		// }
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	/**
	 * 图片地址的回掉存储
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.showLog(TAG, "onActivityResult");
		if (requestCode == REQUEST_IMAGE) {
			LogUtil.showLog(TAG, "REQUEST_IMAGE");
			if (resultCode == RESULT_OK) {
				LogUtil.showLog(TAG, "RESULT_OK");
				mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				StringBuilder sb = new StringBuilder();
				for (String p : mSelectPath) {
					sb.append(p);
					sb.append("\n");
				}
				LogUtil.showLog(TAG, "mSelectPath: " + mSelectPath.size());
				for (int i = 0; i < mSelectPath.size(); i++) {
					SDKCoreHelper.sendImgMessage(otherID, mSelectPath.get(i), otherName);
				}
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void sendImgSucceed(String imgPath) {
		ChatMsgEntity entity = new ChatMsgEntity();
		entity.setDate(getDate());
		entity.setId(otherID);
		entity.setName(Shared.getNickName());
		entity.setMsgType(false);
		entity.setType(ChatMsgEntity.PICTURE);
		entity.setSingle(true);
		entity.setText(imgPath);
		mDataArrays.add(entity);
		
		if (mAdapter == null) {
			mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
			listview.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
		listview.setSelection(listview.getCount() - 1);
	}

	/**
	 * 发送消息
	 */
	String fasongstr = "";

	private void SendMess() {
		// TODO Auto-generated method stub
		String contString = neirong_ed.getText().toString();
		if (!(contString.length() > 0)) {
			showToast("不能发送空信息");
			return;
		}
		SDKCoreHelper.sendMessage(otherID, contString, otherName);
	}

	@SuppressWarnings("static-access")
	public void sendTextSucceed(String contString) {
		LogUtil.showLog(TAG, "文字消息发送成功");
		ChatMsgEntity entity = new ChatMsgEntity();
		entity.setDate(getDate());
		entity.setId(otherID);
		entity.setName(Shared.getNickName());
		entity.setMsgType(false);
		entity.setType(ChatMsgEntity.TEXT);
		entity.setSingle(true);

		fasongstr = contString;
		if (hasp.size() != 0) {
			for (int i = 0; i < hasp.size(); i++) {
				fasongstr = fasongstr.replaceFirst("face",
						"<img  src='" + hasp.get(jiaobiao.get(i)) + "'  />");
			}
			entity.setText(fasongstr);
		} else {
			entity.setText(contString);
		}
		mDataArrays.add(entity);
		if (mAdapter == null) {
			mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
			listview.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
		neirong_ed.setText("");
		listview.setSelection(listview.getCount() - 1);
		// 刷新适配完成了清空集合
		hasp.clear();
		jiaobiao.clear();
		fasongstr = "";
	}

	public void getMsg(ChatMsgEntity chatMsgEntity) {
		chatMsgEntity.setId(otherID);
		if (mDataArrays == null) {
			mDataArrays = new ArrayList<ChatMsgEntity>();
		}
		mDataArrays.add(chatMsgEntity);
		if (mAdapter == null) {
			mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
			listview.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isOpenbiaoqing == true || isWenjian == true) {
				if (isOpenbiaoqing == true) {
					biaoqing_ima.setImageResource(R.drawable.tongyong28);
					isOpenbiaoqing = false;
					isWenjian = false;
				}
				isOpenbiaoqing = false;
				isWenjian = false;
				
				main_qiehuan.setVisibility(View.GONE);
				return false;
			}
		}
		if (keyCode == KeyEvent.KEYCODE_DEL) {
			if (hasp.size() != 0) {
				hasp.remove(jiaobiao.get(jiaobiao.size()));
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onEmojiDelete() {
		// TODO Auto-generated method stub
		String text = neirong_ed.getText().toString();
        if (text.isEmpty()) {
            return;
        }
        if ("]".equals(text.substring(text.length() - 1, text.length()))) {
            int index = text.lastIndexOf("[");
            if (index == -1) {
                int action = KeyEvent.ACTION_DOWN;
                int code = KeyEvent.KEYCODE_DEL;
                KeyEvent event = new KeyEvent(action, code);
                neirong_ed.onKeyDown(KeyEvent.KEYCODE_DEL, event);
                displayTextView();
                return;
            }
            neirong_ed.getText().delete(index, text.length());
            displayTextView();
            return;
        }
        int action = KeyEvent.ACTION_DOWN;
        int code = KeyEvent.KEYCODE_DEL;
        KeyEvent event = new KeyEvent(action, code);
        neirong_ed.onKeyDown(KeyEvent.KEYCODE_DEL, event);
        displayTextView();
	}

	@Override
	public void onEmojiClick(Emoji emoji) {
		// TODO Auto-generated method stub
		if (emoji != null) {
            int index = neirong_ed.getSelectionStart();
            Editable editable = neirong_ed.getEditableText();
            if (index < 0) {
                editable.append(emoji.getContent());
            } else {
                editable.insert(index, emoji.getContent());
            }
        }
        displayTextView();
	}
	
	private void displayTextView() {
        try {
            EmojiUtil.handlerEmojiText(neirong_ed, neirong_ed.getText().toString(), this);
            neirong_ed.setSelection(neirong_ed.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	private void initChat() {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
				 ArrayList<ChatMsgEntity> chatList = MyApplication.app.dbForQQ.queyChat(otherID, num);
				 if (chatList == null) {
					 message.what = -1;
				 } else {
					 for (int i = 0; i < chatList.size(); i++) {
						 chatList.get(i).setName(otherName);
					}
					 if (mDataArrays == null) {
							mDataArrays = new ArrayList<ChatMsgEntity>();
						}
					 	ArrayList<ChatMsgEntity> oldDataArrays = new ArrayList<ChatMsgEntity>();
					 	oldDataArrays.addAll(mDataArrays);
					 	mDataArrays.clear();
					 	for (int i = chatList.size() - 1; i >= 0; i--) {
					 		mDataArrays.add(chatList.get(i));
						}
					 	mDataArrays.addAll(oldDataArrays);
					 	oldDataArrays.clear();
					 message.what = 1;
				 }
				 handler.sendMessage(message);
			}
		}).start();
	}
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			if (msg.what == -1) {
				showToast("暂无消息记录");
			} else if (msg.what == 1) {
				if (mAdapter == null) {
					mAdapter = new ChatMsgViewAdapter(OnLineActivity.this, mDataArrays);
					listview.setAdapter(mAdapter);
				} else {
					mAdapter.notifyDataSetChanged();
				}
			}
		}
	};

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		 ArrayList<ChatMsgEntity> chatList = MyApplication.app.dbForQQ.queyChatHistory(otherID, mDataArrays.size());
		 if (chatList != null) {
			 for (int i = 0; i < chatList.size(); i++) {
				 chatList.get(i).setName(otherName);
			}
			 if (mDataArrays == null) {
					mDataArrays = new ArrayList<ChatMsgEntity>();
				}
			 	ArrayList<ChatMsgEntity> oldDataArrays = new ArrayList<ChatMsgEntity>();
			 	oldDataArrays.addAll(mDataArrays);
			 	mDataArrays.clear();
			 	for (int i = chatList.size() - 1; i >= 0; i--) {
			 		mDataArrays.add(chatList.get(i));
				}
			 	mDataArrays.addAll(oldDataArrays);
			 	oldDataArrays.clear();
				
				if (mAdapter == null) {
					mAdapter = new ChatMsgViewAdapter(OnLineActivity.this, mDataArrays);
					listview.setAdapter(mAdapter);
				} else {
					mAdapter.notifyDataSetChanged();
				}
		 } else {
			showToast("历史记录已加载完毕"); 
		 }
		listview.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		listview.stopLoadMore();
	}
}
