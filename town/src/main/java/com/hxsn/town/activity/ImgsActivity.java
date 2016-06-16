package com.hxsn.town.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hxsn.town.AppApplication;
import com.hxsn.town.R;
import com.hxsn.town.data.IConnPars;
import com.hxsn.town.fragment.MyPageFragment;
import com.hxsn.town.selectimage.FileTraversal;
import com.hxsn.town.selectimage.ImgCallBack;
import com.hxsn.town.selectimage.ImgsAdapter;
import com.hxsn.town.selectimage.UploadUtil;
import com.hxsn.town.selectimage.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;


public class ImgsActivity extends Activity {

	Bundle bundle;
	FileTraversal fileTraversal;
	GridView imgGridView;
	ImgsAdapter imgsAdapter;
	LinearLayout select_layout;
	Util util;
	RelativeLayout relativeLayout2;
	HashMap<Integer, ImageView> hashImage;
//	Button choise_button;
	ArrayList<String> filelist;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photogrally);
		AppApplication.getInstance().addActivity(this);
		imgGridView=(GridView) findViewById(R.id.gridView1);
		bundle= getIntent().getExtras();
		fileTraversal=bundle.getParcelable("data");
		imgsAdapter=new ImgsAdapter(this, fileTraversal.filecontent,onItemClickClass);
		imgGridView.setAdapter(imgsAdapter);
		select_layout=(LinearLayout) findViewById(R.id.selected_image_layout);
		relativeLayout2=(RelativeLayout) findViewById(R.id.relativeLayout2);
//		choise_button=(Button) findViewById(R.id.button3);
		hashImage=new HashMap<Integer, ImageView>();
		filelist=new ArrayList<String>();
//		imgGridView.setOnItemClickListener(this);
		util=new Util(this);
	}
	
	class BottomImgIcon implements OnItemClickListener{
		
		int index;
		public BottomImgIcon(int index) {
			this.index=index;
		}
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			
		}
	}
	
	public ImageView iconImage(String filepath,int index,CheckBox checkBox) throws FileNotFoundException{
		LayoutParams params=new LayoutParams(relativeLayout2.getMeasuredHeight()-10, relativeLayout2.getMeasuredHeight()-10);
		ImageView imageView=new ImageView(this);
		params.rightMargin = 5;
		imageView.setLayoutParams(params);
		imageView.setBackgroundResource(R.drawable.imgbg);
		float alpha=0.5f;
		imageView.setAlpha(alpha);
		util.imgExcute(imageView, imgCallBack, filepath);
		imageView.setOnClickListener(new ImgOnclick(filepath,checkBox));
		return imageView;
	}
	
	ImgCallBack imgCallBack=new ImgCallBack() {
		@Override
		public void resultImgCall(ImageView imageView, Bitmap bitmap) {
			imageView.setImageBitmap(bitmap);
		}
	};
	
	class ImgOnclick implements OnClickListener{
		String filepath;
		CheckBox checkBox;
		public ImgOnclick(String filepath,CheckBox checkBox) {
			this.filepath=filepath;
			this.checkBox=checkBox;
		}
		@Override
		public void onClick(View arg0) {
			checkBox.setChecked(false);
			select_layout.removeView(arg0);
//			choise_button.setText("已选择("+select_layout.getChildCount()+")张");
			filelist.remove(filepath);
		}
	}
	
	ImgsAdapter.OnItemClickClass onItemClickClass=new ImgsAdapter.OnItemClickClass() {
		@Override
		public void OnItemClick(View v, int Position, CheckBox checkBox) {
			String filapath=fileTraversal.filecontent.get(Position);
			if (checkBox.isChecked()) {
				checkBox.setChecked(false);
				select_layout.removeView(hashImage.get(Position));
				filelist.remove(filapath);
//				choise_button.setText("已选择("+select_layout.getChildCount()+")张");
			}else {
				try {
					checkBox.setChecked(true);
					Log.i("img", "img choise position->"+Position);
					ImageView imageView=iconImage(filapath, Position,checkBox);
					if (imageView !=null) {
						hashImage.put(Position, imageView);
						filelist.add(filapath);
						select_layout.addView(imageView);
//						choise_button.setText("已选择("+select_layout.getChildCount()+")张");
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	public void tobreak(View view){
		finish();
	}
	
	/*
	 * 只需要在这个方法把选中的文档目录已list的形式传过去即可
	 */
	public void sendfiles(View view){
		if(filelist.size()>3){
			Toast.makeText(this, "选择上限为三张，请去除多余图片", Toast.LENGTH_LONG).show();
			return;
		} 
		if(filelist.size()<=0){
			Toast.makeText(this, "请选择上传图片", Toast.LENGTH_LONG).show();
			return;
		} 
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < filelist.size(); i++) {
			File file = new File(filelist.get(i));
			if (file != null) {
                String request = UploadUtil.uploadFile(file, IConnPars.UPLOAD_URL, filelist.get(i));
                if(request != null){
                	try {
    					JSONObject jsonObject = new JSONObject(request);
    					String imageValue = jsonObject.getString("imageValue");
    					sb.append(imageValue).append(";");
    				} catch (JSONException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
                } 
                
            }
		}
		if(sb.length()>0){
			String mUrl = sb.toString();
			String result = mUrl.substring(0, mUrl.length()-2);
			MyPageFragment.sendUrlToServer(result);
			Toast.makeText(this, "上传成功", Toast.LENGTH_LONG).show();
			AppApplication.getInstance().exit();
		} else {
			Toast.makeText(this, "上传失败，请联系管理人员", Toast.LENGTH_LONG).show();
		}
	}
}
