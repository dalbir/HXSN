package com.hxsn.iot.fragment.expert;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hxsn.iot.R;
import com.hxsn.iot.control.DataController;
import com.hxsn.iot.util.MessDialog;
import com.hxsn.iot.data.ParseDatas;
import com.hxsn.iot.fragment.AbsBaseFgt;
import com.hxsn.iot.activity.AbsFgtActivity;
import com.hxsn.iot.util.NetworkUtil;
import com.hxsn.iot.util.OnArticleSelectedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * 在线咨询提问界面
 * @author liuzhiyuan
 *
 */
public class AskQuestionFragment extends AbsBaseFgt {
	public static final int NONE = 0;  
    public static final int PHOTOHRAPH = 1;// 拍照   
    public static final int PHOTOZOOM = 2; // 缩放   
    public static final int PHOTORESOULT = 3;// 结果   
  
    public static final String IMAGE_UNSPECIFIED = "image/*";  
	private View view;
	private OnArticleSelectedListener mListener;  
	private List<HashMap<String,String>> list;
	private int position;
	private ParseDatas data;
	private LinearLayout group;
	private EditText title;
	private EditText content;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.expert_ask_question_layout, container, false);
		initData();
		initView();
		return view;
	}
	
	private void initData() {
		position = getArguments().getInt("position");
		data = (ParseDatas) getArguments().getSerializable("parseDatas");
		list = data.getList();
	}
	
	private void initView() {
		Button backBtn = (Button) view.findViewById(R.id.expert_askq_back_btn);
		Button submit = (Button) view.findViewById(R.id.expert_askq_submit);
		title = (EditText) view.findViewById(R.id.expert_askq_title);
		content = (EditText) view.findViewById(R.id.expert_askq_content);
		group = (LinearLayout) view.findViewById(R.id.expert_askq_imagegroup);
		Button addImg = (Button) view.findViewById(R.id.expert_askq_addimage);
		
		backBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Bundle bundle = new Bundle();
				//bundle.putSerializable("parseDatas", data);
				//bundle.putInt("position", position);
				//mListener.onArticleSelectedBack("com.snsoft.aiot.phone.expert.ExpertDetailFragment", bundle);
				((AbsFgtActivity)getActivity()).popFragment(2);
			}
		});
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(title.getText().toString().equals("")){
					Toast.makeText(getActivity(), "标题为空,请输入！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(content.getText().toString().equals("")){
					Toast.makeText(getActivity(), "内容为空,请输入！", Toast.LENGTH_SHORT).show();
					return;
				}
				HashMap<String,String> map = DataController.getMyQuestion(list.get(position).get("id"), title.getText().toString(),
						content.getText().toString());
				if(map != null){
					if(!NetworkUtil.isErrorCode(map.get("code"), getActivity())){
						//进行文件上传点操作
						Toast.makeText(getActivity(), "提交成功！", Toast.LENGTH_LONG).show();
					} 
				} else {
					MessDialog.show(getResources().getString(R.string.server_unusual_msg), getActivity());
				}
			}
		});
		
		addImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final CharSequence[] items =
					{ "相册", "拍照" };
				AlertDialog dlg = new AlertDialog.Builder(getActivity()).setTitle("选择图片").setItems(items,
						new DialogInterface.OnClickListener()
						{
							public void onClick ( DialogInterface dialog , int item )
							{
								if (item == 0)
								{
									Intent intent = new Intent(Intent.ACTION_PICK, null);  
					                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);  
					                startActivityForResult(intent, PHOTOZOOM);  
								} else
								{
									Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
									if(Environment.getExternalStorageDirectory()!=null){
										File file = new File(Environment.getExternalStorageDirectory(), "/tmp");
										if(!file.exists()){
											file.mkdir();
										}
										intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "/tmp/temp.jpg"))); 
									}
					                startActivityForResult(intent, PHOTOHRAPH);  
								}
							}
						}).create();
				dlg.show();
			}
		});
	}
	
	@Override  
    public void onAttach(Activity activity) {  
        super.onAttach(activity);  
        try {  
            mListener = (OnArticleSelectedListener) activity;
         } catch (ClassCastException e) {  
            throw new ClassCastException(activity.toString() + " must implementOnArticleSelectedListener");  
        }  
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (resultCode == NONE)  
            return;  
        // 拍照   
        if (requestCode == PHOTOHRAPH) {  
            //设置文件保存路径这里放在跟目录下   
            File picture = new File(Environment.getExternalStorageDirectory() + "/tmp/temp.jpg");  
            startPhotoZoom(Uri.fromFile(picture));  
        }  
          
        if (data == null)  
            return;  
          
        // 读取相册缩放图片   
        if (requestCode == PHOTOZOOM) {  
            startPhotoZoom(data.getData());  
        }  
        // 处理结果   
        if (requestCode == PHOTORESOULT) {  
            Bundle extras = data.getExtras();  
            if (extras != null) {  
                Bitmap photo = extras.getParcelable("data"); 
                saveMyBitmap("jdj",photo);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();  
                photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0 - 100)压缩文件   
                LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
      				  LinearLayout.LayoutParams.WRAP_CONTENT);
                mParams.leftMargin=10;
                ImageView imageView = new ImageView(getActivity());
               //imageView.setId(1);
                imageView.setLayoutParams(mParams);
                imageView.setImageBitmap(photo);  
                group.addView(imageView);
            }  
  
        }  
  
        super.onActivityResult(requestCode, resultCode, data);  
    }  
  
    public void startPhotoZoom(Uri uri) {  
        Intent intent = new Intent("com.android.camera.action.CROP");  
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);  
        intent.putExtra("crop", "true");  
        // aspectX aspectY 是宽高的比例   
        intent.putExtra("aspectX", 1);  
        intent.putExtra("aspectY", 1);  
        // outputX outputY 是裁剪图片宽高   
        intent.putExtra("outputX", 70);  
        intent.putExtra("outputY", 70);  
        intent.putExtra("return-data", true);  
        startActivityForResult(intent, PHOTORESOULT);  
    }  
    
    public void saveMyBitmap(String bitName,Bitmap mBitmap){
    	  if(isFolderExists("/sdcard/aiot")){
    		  File f = new File("/sdcard/aiot/" + bitName + ".png");
        	  try {
        		  f.createNewFile();
        	  } catch (IOException e) {
        		  Log.i("","在保存图片时出错："+e.toString());
        	  }
        	  FileOutputStream fOut = null;
        	  try {
        		  fOut = new FileOutputStream(f);
        		  mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        		  fOut.flush();
        		  fOut.close();
        	  } catch (FileNotFoundException e) {
        		  e.printStackTrace();
        	  } catch (IOException e) {
        		  e.printStackTrace();
        	  }
    	  }
    }
    
    private boolean isFolderExists(String strFolder)
    {
        File file = new File(strFolder);
        
        if (!file.exists())
        {
            if (file.mkdir())
            {
                return true;
            }
            else
                return false;
        }
        return true;
    }
}
