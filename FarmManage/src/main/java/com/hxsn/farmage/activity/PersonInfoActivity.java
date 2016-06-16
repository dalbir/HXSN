package com.hxsn.farmage.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.hxsn.farmage.MainActivity;
import com.hxsn.farmage.MyApplication;
import com.hxsn.farmage.R;
import com.hxsn.farmage.base.BaseActivity;
import com.hxsn.farmage.myview.LoadingDialog;
import com.hxsn.farmage.utils.BitmapUtil;
import com.hxsn.farmage.utils.GetURL;
import com.hxsn.farmage.utils.HeadImgUtil;
import com.hxsn.farmage.utils.ImgBtnEffact;
import com.hxsn.farmage.utils.LogUtil;
import com.hxsn.farmage.utils.SDCardUtil;
import com.hxsn.farmage.utils.Shared;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 *
 *
 */
@SuppressLint("ShowToast")
public class PersonInfoActivity extends BaseActivity {

	ImageButton personBackBtn;
	ImageView pheadIV;
	TextView pnameTV, psexTV, paddressTV, pmessageTV, psetTV, pfarmTV;
	TextView myswitch;

	private Bitmap bmp;
	String path;
	Bitmap bitmaps;
	public static String filePath;
	Button pexitBtn;
	Dialog myNameDialog = null;
	Dialog myAddressDialog = null;

	LinearLayout tab1Layout, tab2Layout, tab3Layout, tab4Layout;
	Intent tabIntent;
	public final static int CONSULT_DOC_PICTURE = 1000;
	public final static int CONSULT_DOC_CAMERA = 1001;
	private int SELECT_PICTURE = 0;
	private int SELECT_CAMERA = 1;

	public static Uri outputFileUri;
	ImageView tab1IV, tab4IV;
	TextView tab1TV, tab4TV;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personinfor);

		initView();
		getMyInfor();
		initListener();
	}

	// 获取个人信息
	private void getMyInfor() {
		// TODO Auto-generated method stub
		String uid = "";
		if (Shared.getUserID().length() > 0)
			uid = Shared.getUserID();
		LoadingDialog.showLoading(this);
		try {
			StringRequest stringRequest = new StringRequest(GetURL.GETMYINFO
					+ uid, new Response.Listener<String>() {
				@Override
				public void onResponse(String response) {

					JSONObject j = null;
					try {
						try {
							j = new JSONObject(new String(response.getBytes(),
									"UTF-8"));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (j.getString("code").equals("200")) {

							JSONArray arrayObject = j.getJSONArray("result");
							for (int i = 0; i < arrayObject.length(); i++) {
								JSONObject newObject = (JSONObject) arrayObject
										.get(i);
								String sex = newObject.getString("sex");
								String address = newObject.getString("address");
								String nickname = newObject
										.getString("nickname");
								String photo = newObject.getString("photo");
									new HeadImgUtil(Shared.getUserID(), photo, pheadIV, PersonInfoActivity.this);
									if (nickname.length() > 0)
									pnameTV.setText(nickname);
								else
									pnameTV.setText("暂无，请填写");
								if (sex.length() > 0)
									if (sex.equals("1"))
										psexTV.setText("女");
									else
										psexTV.setText("男");
								else
									psexTV.setText("男");
								if (address.length() > 0)
									paddressTV.setText(address);
								else
									paddressTV.setText("暂无，请填写");
							}

						}
						LoadingDialog.dissmissLoading();
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e("eeeeeeee", error.getMessage(), error);
				}
			});
			mQueue.add(stringRequest);
		} catch (Exception w) {

		}

	}
	
	
	private void initListener() {
		// TODO Auto-generated method stub
		// 点击更换头像
		pheadIV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				CharSequence[] items = { "相册", "拍摄" };
				new Builder(PersonInfoActivity.this)
						.setTitle("请选择")
						.setItems(items, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == SELECT_PICTURE) {
									Intent intent = new Intent(
											Intent.ACTION_PICK);
									intent.setDataAndType(
											MediaStore.Images.Media.INTERNAL_CONTENT_URI,
											"image/*");
									startActivityForResult(intent,
											CONSULT_DOC_PICTURE);
								} else if (which == SELECT_CAMERA) {
									File file = new File(Environment
											.getExternalStorageDirectory(),
											"/head.jpg");
									outputFileUri = Uri.fromFile(file);
									Intent intent = new Intent(
											MediaStore.ACTION_IMAGE_CAPTURE);
									intent.putExtra(MediaStore.EXTRA_OUTPUT,
											outputFileUri);
									startActivityForResult(intent,
											CONSULT_DOC_CAMERA);
								}
							}
						}).create().show();

			}
		});

		// 点击修改姓名
		pnameTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				myNameDialog = new Dialog(PersonInfoActivity.this);
				myNameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				myNameDialog.show();
				myNameDialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(0));
				myNameDialog.setCanceledOnTouchOutside(false);
				Window win = myNameDialog.getWindow();
				win.setContentView(R.layout.dialog_name);

				Button setting = (Button) win.findViewById(R.id.p_nmsetBtn);
				Button close = (Button) win.findViewById(R.id.pnm_cancleBtn);
				final EditText nameET = (EditText) win
						.findViewById(R.id.p_nameET);
				setting.setOnTouchListener(ImgBtnEffact.btnTL);
				close.setOnTouchListener(ImgBtnEffact.btnTL);

				setting.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (nameET.getText().toString().length() > 0) {
							pnameTV.setText(nameET.getText().toString());
							try {
								// updateName(new String(
								// nameET.getText().toString().getBytes(),
								// "UTF-8"));
								updateName(nameET.getText().toString());
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						myNameDialog.dismiss();
					}

					private void updateName(String name) {
						// TODO Auto-generated method stub
						String uid = "";
						if (Shared.getUserID().length() > 0)
							uid = Shared.getUserID();
						try {
							name = URLEncoder.encode(name, "utf-8");
							StringRequest stringRequest = new StringRequest(
									GetURL.UPDATENAME + uid + "&nickname="
											+ name,
									new Response.Listener<String>() {
										@Override
										public void onResponse(String response) {

											JSONObject j;
											try {
												j = new JSONObject(response);
												if (j.getString("code").equals(
														"200")) {

													showToast("修改昵称成功");

												}

											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									}, new Response.ErrorListener() {
										@Override
										public void onErrorResponse(
												VolleyError error) {
											Log.e("eeeeeeee",
													error.getMessage(), error);
										}
									});
							mQueue.add(stringRequest);
						} catch (Exception w) {

						}

					}
				});

				close.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						myNameDialog.dismiss();
					}
				});

				myNameDialog.setOnDismissListener(new OnDismissListener() {
					public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						myNameDialog = null;
					}
				});

			}
		});

		// 点击修改性别
		psexTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Builder builder = new Builder(
						PersonInfoActivity.this);
				builder.setTitle("请选择您的性别");
				// 定义单选的选项
				final String[] items = new String[] { "男", "女" };
				// 设置单选选项
				// arg1：表示默认选中哪一项，-1表示没有默认选中项
				builder.setSingleChoiceItems(items, 0,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 执行选中某个选项后的业务逻辑
								// 点击某个选项后，触发onClick执行，要让对话框消失
								psexTV.setText(items[which]);
								updateSex(which + "");
								dialog.dismiss();
							}

							private void updateSex(String sex) {
								// TODO Auto-generated method stub
								String uid = "";
								if (Shared.getUserID().length() > 0)
									uid = Shared.getUserID();
								try {
									StringRequest stringRequest = new StringRequest(
											GetURL.UPDATESEX + uid + "&sex="
													+ sex,
											new Response.Listener<String>() {
												@SuppressWarnings("unused")
												@Override
												public void onResponse(
														String response) {

													JSONObject j;
													try {
														j = new JSONObject(
																response);
														if (j.getString("code")
																.equals("200")) {

															showToast("修改性别成功");
															String fileName1 = Shared.getUserID() + ".jpeg";
															String filePath2 = SDCardUtil.getInnerSDCardPath() + "/CloudyFarm/head/";
															if(new File(filePath2, fileName1)==null)
																pheadIV.setImageResource(R.drawable.nv);

														}

													} catch (JSONException e) {
														e.printStackTrace();
													}
												}
											}, new Response.ErrorListener() {
												@Override
												public void onErrorResponse(
														VolleyError error) {
													Log.e("eeeeeeee",
															error.getMessage(),
															error);
												}
											});
									mQueue.add(stringRequest);
								} catch (Exception w) {

								}
							}
						});

				// 直接调用builder的show方法同样可以显示对话框，其内部也是先创建对话框对象，然后调用对话框的show()
				builder.show();

			}
		});

		// 点击修改地址
		paddressTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				myAddressDialog = new Dialog(PersonInfoActivity.this);
				myAddressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				myAddressDialog.show();
				myAddressDialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(0));
				myAddressDialog.setCanceledOnTouchOutside(false);
				Window win = myAddressDialog.getWindow();
				win.setContentView(R.layout.dialog_address);

				Button setting = (Button) win.findViewById(R.id.p_adsetBtn);
				Button close = (Button) win.findViewById(R.id.pad_cancleBtn);
				final EditText addressET = (EditText) win
						.findViewById(R.id.p_addressET);
				setting.setOnTouchListener(ImgBtnEffact.btnTL);
				close.setOnTouchListener(ImgBtnEffact.btnTL);

				setting.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (addressET.getText().toString().length() > 0) {
							paddressTV.setText(addressET.getText().toString());
							updateAddress(addressET.getText().toString());
						}
						myAddressDialog.dismiss();
					}

					private void updateAddress(String address) {
						// TODO Auto-generated method stub
						String uid = "";
						if (Shared.getUserID().length() > 0)
							uid = Shared.getUserID();
						try {
							address = URLEncoder.encode(address, "utf-8");
							StringRequest stringRequest = new StringRequest(
									GetURL.UPDATEADDRESS + uid + "&address="
											+ address,
									new Response.Listener<String>() {
										@Override
										public void onResponse(String response) {

											JSONObject j;
											try {
												j = new JSONObject(response);
												if (j.getString("code").equals(
														"200")) {
													showToast("修改地址成功");
												}

											} catch (JSONException e) {
												e.printStackTrace();
											}
										}
									}, new Response.ErrorListener() {
										@Override
										public void onErrorResponse(
												VolleyError error) {
											Log.e("eeeeeeee",
													error.getMessage(), error);
										}
									});
							mQueue.add(stringRequest);
						} catch (Exception w) {

						}
					}
				});

				close.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						myAddressDialog.dismiss();
					}
				});

				myAddressDialog.setOnDismissListener(new OnDismissListener() {
                	public void onDismiss(DialogInterface dialog) {
						// TODO Auto-generated method stub
						myNameDialog = null;
					}
				});
			}
		});
		// 退出
		pexitBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		pexitBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				Shared.setAddress("");
				Shared.setNickName("");
				Shared.setPassword("");
				Shared.setSex("");
				Shared.setUserID("");
				Shared.setUserName("");
				Shared.setCode("");
				MyApplication.app.servers.clear();
				Intent inte = new Intent(PersonInfoActivity.this,
						LoginActivity.class);
				startActivity(inte);
				finish();
			}
		});
		personBackBtn.setOnTouchListener(ImgBtnEffact.btnTL);
		personBackBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 模块跳转
		tab1Layout.setOnClickListener(new NewIVListener());
		tab2Layout.setOnClickListener(new NewIVListener());
		tab3Layout.setOnClickListener(new NewIVListener());
		tab4Layout.setOnClickListener(new NewIVListener());
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/**
	 
	 */
	private void initView() {
		// TODO Auto-generated method stub
		tab1Layout = (LinearLayout) findViewById(R.id.tab1);
		tab2Layout = (LinearLayout) findViewById(R.id.tab2);
		tab3Layout = (LinearLayout) findViewById(R.id.tab3);
		tab4Layout = (LinearLayout) findViewById(R.id.tab4);
		tabIntent = new Intent(PersonInfoActivity.this, MainActivity.class);
		tab1IV=(ImageView)findViewById(R.id.tab1IV);
		tab4IV=(ImageView)findViewById(R.id.tab4IV);
		tab1TV=(TextView)findViewById(R.id.hometltv);
		tab4TV=(TextView)findViewById(R.id.tab4IVltv);
		tab1IV.setBackgroundColor(Color.TRANSPARENT);
		tab1IV.setImageResource(R.drawable.jian);
		tab1TV.setTextColor(Color.rgb(91, 91, 99));
		tab4IV.setImageResource(R.drawable.wo01);
		tab4TV.setTextColor(Color.rgb(139, 193, 17));
		
		TextView dsfhTV=(TextView)findViewById(R.id.dsfhTV);
		dsfhTV.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				finish();
			}
		});
		
		personBackBtn = (ImageButton) findViewById(R.id.personBackBtn);
		pheadIV = (ImageView) findViewById(R.id.pheadIV);
		pnameTV = (TextView) findViewById(R.id.pnameTV);
		psexTV = (TextView) findViewById(R.id.psexTV);
		paddressTV = (TextView) findViewById(R.id.paddressTV);
		pmessageTV = (TextView) findViewById(R.id.pmessageTV);

		pfarmTV = (TextView) findViewById(R.id.pfarmTV);
		pexitBtn = (Button) findViewById(R.id.pexitBtn);
		myswitch = (TextView) findViewById(R.id.myswitchBtn);
		pfarmTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// Intent inte=new
				// Intent(PersonInfoActivity.this,AboutUsActivity.class);
				// inte.putExtra("aboutUs", GetURL.ABOUTFARM);
				// startActivity(inte);

			}
		});

		// showToast("messageNow:"+Shared.getMessageAlert());
		if (Shared.getMessageAlert().equals("1"))
			myswitch.setText("打开");
		else
			myswitch.setText("关闭");

		myswitch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (Shared.getMessageAlert().equals("1")) {
					myswitch.setText("关闭");
					Shared.setMessageAlert("0");
				} else {
					myswitch.setText("打开");
					Shared.setMessageAlert("1");
				}

			}
		});
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CONSULT_DOC_PICTURE) {
			if (data == null) {
				return;
			}

			Uri uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			@SuppressWarnings("deprecation")
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			path = cursor.getString(column_index);

			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;
			bitmaps = BitmapFactory.decodeFile(path, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			@SuppressWarnings("deprecation")
			float hh = getWindowManager().getDefaultDisplay().getHeight();
			@SuppressWarnings("deprecation")
			float ww = getWindowManager().getDefaultDisplay().getWidth();
			int be = 1;
			if (w > h && w > ww) {
				be = (int) (w / ww);
			} else if (w < h && h > hh) {
				be = (int) (h / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;
			bitmaps = BitmapFactory.decodeFile(path, newOpts);
			if (be == 1)
				bitmaps = BitmapFactory.decodeFile(path);
			bitmaps = BitmapUtil.centerSquareScaleBitmap(bitmaps, 100);
			HeadImgUtil.headDelete(Shared.getUserID());
			HeadImgUtil.save(bitmaps, HeadImgUtil.getImgParentPath(), Shared.getUserID() + ".jpeg");
			uploadFile(HeadImgUtil.getImgParentPath() + Shared.getUserID() + ".jpeg");
			
		} else if (requestCode == CONSULT_DOC_CAMERA) {

			if (bmp != null)
				bmp.recycle();
			String path = Environment.getExternalStorageDirectory()
					+ "/head.jpg";
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			newOpts.inJustDecodeBounds = true;
			bmp = BitmapFactory.decodeFile(path, newOpts);
			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
			@SuppressWarnings("deprecation")
			float hh = getWindowManager().getDefaultDisplay().getHeight();// 这里设置高度
			@SuppressWarnings("deprecation")
			float ww = getWindowManager().getDefaultDisplay().getWidth();// 这里设置宽度
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;// be=1表示不缩放
			if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
				be = (int) (w / ww);
			} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) (h / hh);
			}
			if (be <= 0)
				be = 1;
			newOpts.inSampleSize = be;// 设置缩放比例
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			bmp = BitmapFactory.decodeFile(path, newOpts);
			bmp = BitmapUtil.centerSquareScaleBitmap(bmp, 100);
			HeadImgUtil.headDelete(Shared.getUserID());
			HeadImgUtil.save(bmp, HeadImgUtil.getImgParentPath(), Shared.getUserID() + ".jpeg");
			uploadFile(HeadImgUtil.getImgParentPath() + Shared.getUserID() + ".jpeg");

		} else {
			// showToast("");
		}

	}

	private void uploadFile(final String uri) {
		LoadingDialog.showLoading(this);
		AsyncHttpClient httpClient = new AsyncHttpClient();
		RequestParams param = new RequestParams();
		try {
			param.put("app", "2");
			param.put("uid", Shared.getUserID());
			param.put("filedata", new File(uri));

			httpClient.post(GetURL.UPDATEHEAD, param,
					new AsyncHttpResponseHandler() {

						public void onStart() {
						}

						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							LoadingDialog.dissmissLoading();

						}

						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							String s;
							try {
								s = new String(arg2, "GB2312");
								// Log.i("dd", s);
								JSONObject js;
								LogUtil.showLog("upload: ", s);
								try {
									js = new JSONObject(s);
									if (js.getInt("code") == 200) {
										new HeadImgUtil(Shared.getUserID(), null, pheadIV, PersonInfoActivity.this);
									} else {
										showToast("上传失败");
									}
									LoadingDialog.dissmissLoading();
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									LoadingDialog.dissmissLoading();
								}

							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
								LoadingDialog.dissmissLoading();
							}

						}
					});
		} catch (Exception e) {
			e.printStackTrace();
			LoadingDialog.dissmissLoading();
		}
	}

	class NewIVListener implements OnClickListener {
	@Override
		public void onClick(View view) {
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
}
