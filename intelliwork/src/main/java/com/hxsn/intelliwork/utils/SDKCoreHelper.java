package com.hxsn.intelliwork.utils;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

import com.hxsn.intelliwork.MainActivity;
import com.hxsn.intelliwork.MyApplication;
import com.hxsn.intelliwork.R;
import com.hxsn.intelliwork.base.BaseActivity;
import com.hxsn.intelliwork.beans.ChatMsgEntity;
import com.hxsn.intelliwork.beans.Contactor;
import com.hxsn.intelliwork.beans.OffLineMessage;
import com.hxsn.intelliwork.beans.SignBean;
import com.hxsn.intelliwork.beans.SignListBean;
import com.hxsn.intelliwork.comm.ComFragment;
import com.hxsn.intelliwork.comm.OnLineActivity;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECChatManager.OnSendMessageListener;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECInitParams.LoginMode;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.PersonInfo;
import com.yuntongxun.ecsdk.PersonInfo.Sex;
import com.yuntongxun.ecsdk.SdkErrorCode;
import com.yuntongxun.ecsdk.im.ECImageMessageBody;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.ECVoiceMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

@SuppressLint({ "HandlerLeak", "SimpleDateFormat" })
public class SDKCoreHelper {

	private static String TAG = "SDKCoreHelper----";
	private static Context context;
	private static boolean isShow = true;
	private static SimpleDateFormat format;

	/**
	 * 閿熸枻鎷峰閿熸枻鎷稴DK
	 */
	@SuppressWarnings("deprecation")
	public static void init(final Context context) {
		format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		SDKCoreHelper.context = context;
		if (!ECDevice.isInitialized()) {
			ECDevice.initial(context, new ECDevice.InitListener() {
				@Override
				public void onInitialized() {
					LogUtil.showLog(TAG, "SDKstart");
					init(SDKCoreHelper.context);
				}

				@Override
				public void onError(Exception exception) {
            	LogUtil.showLog(TAG, "onError: " + exception.toString());
				}
			});
		}

		ECInitParams params = new ECInitParams();
		params.setUserid(Shared.getUserID());
		params.setAppKey("8a48b551521b6d3c01521b7043b60005");
		params.setToken("9bf282a7113081197e566bcbdb5847fb");
		params.setMode(LoginMode.FORCE_LOGIN);

		params.setOnDeviceConnectListener(new ECDevice.OnECDeviceConnectListener() {
			public void onConnect() {

			}

			@Override
			public void onDisconnect(ECError error) {

			}

			@Override
			public void onConnectState(ECDevice.ECConnectState state,
					ECError error) {
				if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
					if (error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
						BaseActivity.baseActivity.clearAllActivity();
						if (OnLineActivity.onLineActivity != null) {
							OnLineActivity.onLineActivity.finish();
						}
						isShow = true;
						showOffLine(context);
					} else {
						LogUtil.showLog(TAG, "connect_failed");
					}
					return;
				} else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
					LogUtil.showLog(TAG, "CONNECT_SUCCESS");
				}
			}
		});

		params.setOnChatReceiveListener(new OnChatReceiveListener() {
			@SuppressLint("InlinedApi")
			@SuppressWarnings({ "static-access", "deprecation" })
			@Override
			public void OnReceivedMessage(ECMessage msg) {
				if (msg == null) {
					return;
				}
				LogUtil.showLog(TAG, msg.toString());
				Log.i("zxw", "get: " + msg.getForm());
				String userName = getUserName(msg.getForm());
				ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
				chatMsgEntity.setName(getUserName(msg.getForm()));
				chatMsgEntity.setSingle(true);
				chatMsgEntity.setMsgType(true);
				chatMsgEntity.setDate(format.format(new Date(msg.getMsgTime())));
				// 接收到的IM消息，根据IM消息类型做不同的处理(IM消息类型：ECMessage.Type)
				ECMessage.Type type = msg.getType();
				if (type == ECMessage.Type.TXT) {
					// 在这里处理文本消息
					ECTextMessageBody textMessageBody = (ECTextMessageBody) msg
							.getBody();
					
					String message = textMessageBody.getMessage();
					if (message.length() > 5) {
						LogUtil.showLog(TAG, message.subSequence(0, 5).toString());
						if (message.subSequence(0, 5).toString().equals("'^^':")) {
							try {
								JSONObject jsonObject = new JSONObject(message.replace("'^^':", ""));
								SignBean bean = new SignBean();
								String signType = jsonObject.getString("type");
								Log.i("3videoss",jsonObject.getString("type"));
								bean.setType(signType);
								if (signType.equals("1")) {
									bean.setText(jsonObject
											.getString("text"));
									bean.setDkcode(jsonObject
											.getString("dkcode"));
									NotificationManager mNotificationManager = (NotificationManager) SDKCoreHelper.context
											.getSystemService(SDKCoreHelper.context.NOTIFICATION_SERVICE);
									NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
											SDKCoreHelper.context);
									mBuilder.setContentTitle("三省农庄")
											// 设置通知栏标题
											.setContentText(
													jsonObject
															.getString("text")) // 设置通知栏显示内容
//											.setContentIntent(
//													getDefalutIntent(
//															Notification.FLAG_AUTO_CANCEL,
//															jsonObject
//																	.getString("dkcode")))// 设置通知栏点击意图
											// .setNumber(number)
											// //设置通知集合的数量
											.setTicker("三省农庄来通知了") // 通知首次出现在通知栏，带上升动画效果的
											.setWhen(
													System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
											.setPriority(
													Notification.PRIORITY_DEFAULT) // 设置该通知优先级
											// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
											.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
											.setDefaults(
													Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
											// Notification.DEFAULT_ALL
											// Notification.DEFAULT_SOUND
											// 添加声音 // requires VIBRATE
											// permission
											.setSmallIcon(R.drawable.icon);// 设置通知小ICON
									Notification notification = mBuilder
											.build();
									notification.flags = Notification.FLAG_AUTO_CANCEL;
									mNotificationManager.notify(10,
											mBuilder.build());

								} else if (signType.equals("2")) {
									NotificationManager mNotificationManager = (NotificationManager) SDKCoreHelper.context
											.getSystemService(SDKCoreHelper.context.NOTIFICATION_SERVICE);
									NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
											SDKCoreHelper.context);
									mBuilder.setContentTitle("三省农庄")
											// 设置通知栏标题
											.setContentText(
													jsonObject
															.getString("text")) // 设置通知栏显示内容
											// .setNumber(number)
											// //设置通知集合的数量
											.setTicker("报警信息") // 通知首次出现在通知栏，带上升动画效果的
											.setWhen(
													System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
											.setPriority(
													Notification.PRIORITY_DEFAULT) // 设置该通知优先级
											// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
											.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
											.setDefaults(
													Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
											// Notification.DEFAULT_ALL
											// Notification.DEFAULT_SOUND
											// 添加声音 // requires VIBRATE
											// permission
											.setSmallIcon(R.drawable.icon);// 设置通知小ICON
									Notification notification = mBuilder
											.build();
									notification.flags = Notification.FLAG_AUTO_CANCEL;
									mNotificationManager.notify(10,
											mBuilder.build());

								} 
								else if (signType.equals("3")) {
									JSONArray array = new JSONArray(jsonObject.getString("text"));
									ArrayList<SignListBean> signList = new ArrayList<SignListBean>();
									for (int i = 0; i < array.length(); i++) {
										JSONObject obj = new JSONObject(array.getString(i));
										SignListBean signListBean = new SignListBean();
										signListBean.setId(obj.getString("id"));
										signListBean.setName(obj.getString("name"));
										signListBean.setIsdef(obj.getString("isdef"));
										signListBean.setPosition(obj.getString("position"));
										signListBean.setEqtype(obj.getString("eqtype"));
										signListBean.setAddress(obj.getString("address"));
										signList.add(signListBean);
										
									}
									Log.i("3videosSize",signList.size()+"");
									bean.setSignList(signList);
//									RealVideos.beans=bean;
//									Intent inte=new Intent(SDKCoreHelper.context,NewRealPlayActivity.class);
//									SDKCoreHelper.context.startActivity(inte);
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							//----------------------跳转页面传递数据--------------------------
						
							
							return;
						}
					}
					chatMsgEntity.setType(ChatMsgEntity.TEXT);
					chatMsgEntity.setText(message);
					
					
					saveData(msg.getForm(), 0, message, userName, 0);
					
					if (OnLineActivity.onLineActivity != null) {
						OnLineActivity.onLineActivity.getMsg(chatMsgEntity);
						RemindManager.doRemind(false, context);
					} else {
						if (MyApplication.dbForQQ.upDataRemind(msg.getForm(), false) > 0) {
							if (ComFragment.comFragment != null) {
								ComFragment.comFragment.upDataRemind();
							}
							if (isBackgroundRunning()) {
								int num = MyApplication.dbForQQ.queryRemind(msg.getForm());
								NotificationUtil.sendNot(context, msg.getForm(), chatMsgEntity.getName(), getPhone(msg.getForm()), chatMsgEntity.getDate(), num);
							} else {
								RemindManager.doRemind(true, context);
							}
						}
					}
					
				} else {

					String thumbnailFileUrl = null;
					String remoteUrl = null;
					if (type == ECMessage.Type.IMAGE) {
						chatMsgEntity.setType(ChatMsgEntity.PICTURE);
						// 在这里处理图片消息
						ECImageMessageBody imageMsgBody = (ECImageMessageBody) msg
								.getBody();
						// 获得缩略图地址
						thumbnailFileUrl = imageMsgBody.getThumbnailFileUrl();
						// 获得原图地址
						remoteUrl = imageMsgBody.getRemoteUrl();
						download(remoteUrl, ".jpg", chatMsgEntity, 1);
						
						
						
					} else if (type == ECMessage.Type.VOICE) {
						chatMsgEntity.setType(ChatMsgEntity.VOICE);
						// 在这里处理语音消息
						ECVoiceMessageBody voiceMsgBody = (ECVoiceMessageBody) msg
								.getBody();
						// 获得下载地址
						remoteUrl = voiceMsgBody.getRemoteUrl();
						download(remoteUrl, ".amr", chatMsgEntity, 1);
			
					} else {
						Log.e("ECSDK_Demo",
								"Can't handle msgType=" + type.name()
										+ " , then ignore.");
					}

					if (TextUtils.isEmpty(remoteUrl)) {
						return;
					}
					if (!TextUtils.isEmpty(thumbnailFileUrl)) {
						// 先下载缩略图
					} else {
						// 下载附件

					}
				}

				// --------------------根据不同类型处理完消息之后，将消息序列化到本地存储（sqlite）
				
				// --------------------通知UI有新消息到达

			}

			@Override
			public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage notice) {

			}

			@Override
			public void onOfflineMessageCount(int count) {
				// 閿熸枻鎷烽檰閿熺即鐧告嫹涔嬮敓鏂ゆ嫹SDK閿熸埅纰夋嫹閿熺煫鎺ュ尅鎷烽�鐭ラ敓鍓跨尨鎷烽敓鏂ゆ嫹閿熸枻鎷烽敓鏂ゆ嫹鎭敓鏂ゆ嫹
				// Log.i("zxw", "閿熸枻鎷烽敓鏂ゆ嫹閿熸枻鎷锋伅閿熸枻鎷� + count);
			}

			@SuppressWarnings({ "rawtypes", "static-access" })
			@Override
			public void onReceiveOfflineMessage(List msgs) {
				ArrayList<OffLineMessage> whos;
				String message = "";
				if (msgs == null) {
					return;
				}
				LogUtil.showLog(TAG, "offline: " + msgs.size());
				whos = new ArrayList<OffLineMessage>();
				for (int i = 0; i < msgs.size(); i++) {
					String thumbnailFileUrl = null;
					String remoteUrl = null;
					ECMessage ecMessage = (ECMessage) msgs.get(i);
					ECMessage.Type type = ecMessage.getType();
					String userName = getUserName(ecMessage.getForm());
					ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
					chatMsgEntity.setName(getUserName(ecMessage.getForm()));
					chatMsgEntity.setSingle(true);
					chatMsgEntity.setMsgType(true);
					chatMsgEntity.setDate(format.format(new Date(ecMessage.getMsgTime())));
					
					if (type == ECMessage.Type.TXT) {
						ECTextMessageBody textMessageBody = (ECTextMessageBody) ecMessage
								.getBody();
						message = textMessageBody.getMessage();
						
						if (message.length() > 5) {
							LogUtil.showLog(TAG, message.subSequence(0, 5)
									.toString());
							if (message.subSequence(0, 5).toString()
									.equals("'^^':")) {
								try {
										JSONObject jsonObject = new JSONObject(
												message.replace("'^^':", ""));
										SignBean bean = new SignBean();
										String signType = jsonObject
												.getString("type");
										Log.i("3videoss",
												jsonObject.getString("type"));
										bean.setType(signType);
										if (signType.equals("1")) {
											bean.setText(jsonObject
													.getString("text"));
											bean.setDkcode(jsonObject
													.getString("dkcode"));
											NotificationManager mNotificationManager = (NotificationManager) SDKCoreHelper.context
													.getSystemService(SDKCoreHelper.context.NOTIFICATION_SERVICE);
											NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
													SDKCoreHelper.context);
											mBuilder.setContentTitle("三省农庄")
													// 设置通知栏标题
													.setContentText(
															jsonObject
																	.getString("text")) // 设置通知栏显示内容
//													.setContentIntent(
//															getDefalutIntent(
//																	Notification.FLAG_AUTO_CANCEL,
//																	jsonObject
//																			.getString("dkcode")))// 设置通知栏点击意图
													// .setNumber(number)
													// //设置通知集合的数量
													.setTicker("三省农庄来通知了") // 通知首次出现在通知栏，带上升动画效果的
													.setWhen(
															System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
													.setPriority(
															Notification.PRIORITY_DEFAULT) // 设置该通知优先级
													// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
													.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
													.setDefaults(
															Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
													// Notification.DEFAULT_ALL
													// Notification.DEFAULT_SOUND
													// 添加声音 // requires VIBRATE
													// permission
													.setSmallIcon(R.drawable.icon);// 设置通知小ICON
											Notification notification = mBuilder
													.build();
											notification.flags = Notification.FLAG_AUTO_CANCEL;
											mNotificationManager.notify(10,
													mBuilder.build());

										} else if (signType.equals("2")) {
											NotificationManager mNotificationManager = (NotificationManager) SDKCoreHelper.context
													.getSystemService(SDKCoreHelper.context.NOTIFICATION_SERVICE);
											NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
													SDKCoreHelper.context);
											mBuilder.setContentTitle("三省农庄")
													// 设置通知栏标题
													.setContentText(
															jsonObject
																	.getString("text")) // 设置通知栏显示内容
													// .setNumber(number)
													// //设置通知集合的数量
													.setTicker("报警信息") // 通知首次出现在通知栏，带上升动画效果的
													.setWhen(
															System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
													.setPriority(
															Notification.PRIORITY_DEFAULT) // 设置该通知优先级
													// .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
													.setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
													.setDefaults(
															Notification.DEFAULT_ALL)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
													// Notification.DEFAULT_ALL
													// Notification.DEFAULT_SOUND
													// 添加声音 // requires VIBRATE
													// permission
													.setSmallIcon(R.drawable.icon);// 设置通知小ICON
											Notification notification = mBuilder
													.build();
											notification.flags = Notification.FLAG_AUTO_CANCEL;
											mNotificationManager.notify(10,
													mBuilder.build());

										} else if (signType.equals("3")) {
											JSONArray array = new JSONArray(
													jsonObject.getString("text"));
											ArrayList<SignListBean> signList = new ArrayList<SignListBean>();
											for (int q = 0; q < array.length(); q++) {
												JSONObject obj = new JSONObject(
														array.getString(q));
												SignListBean signListBean = new SignListBean();
												signListBean.setId(obj
														.getString("id"));
												signListBean.setName(obj
														.getString("name"));
												signListBean.setIsdef(obj
														.getString("isdef"));
												signListBean.setPosition(obj
														.getString("position"));
												signListBean.setEqtype(obj
														.getString("eqtype"));
												signListBean.setAddress(obj
														.getString("address"));
												signList.add(signListBean);

											}
											Log.i("3videosSize", signList.size()
													+ "");
											bean.setSignList(signList);
											RealVideos.beans = bean;
											NotificationUtil.sendNot(context, format.format(new Date(System.currentTimeMillis())));
										}
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								// ----------------------跳转页面传递数据--------------------------

								break;
							}
						}
						
						chatMsgEntity.setType(ChatMsgEntity.TEXT);
						chatMsgEntity.setText(message);
						
						saveData(ecMessage.getForm(), 0, message, userName, 0);
						
					} else if (type == ECMessage.Type.VOICE) {
						chatMsgEntity.setType(ChatMsgEntity.VOICE);
						// 在这里处理语音消息
						ECVoiceMessageBody voiceMsgBody = (ECVoiceMessageBody) ecMessage
								.getBody();
						// 获得下载地址
						remoteUrl = voiceMsgBody.getRemoteUrl();
						download(remoteUrl, ".amr", chatMsgEntity, 0);
						
					} else if (type == ECMessage.Type.IMAGE) {
						chatMsgEntity.setType(ChatMsgEntity.PICTURE);
						// 在这里处理图片消息
						ECImageMessageBody imageMsgBody = (ECImageMessageBody) ecMessage
								.getBody();
						// 获得缩略图地址
						thumbnailFileUrl = imageMsgBody.getThumbnailFileUrl();
						// 获得原图地址
						remoteUrl = imageMsgBody.getRemoteUrl();
						download(remoteUrl, ".jpg", chatMsgEntity, 0);
					}
					String who = ecMessage.getForm();

					if (whos.size() == 0) {
						OffLineMessage offLineMessage = new OffLineMessage();
						offLineMessage.setUserPhone(who);
						offLineMessage.setMsgNum();
						whos.add(offLineMessage);
					} else {
						for (int j = 0; j < whos.size(); j++) {
							if (who.equals(whos.get(j).getUserPhone())) {
								whos.get(j).setMsgNum();
							} else if (j == whos.size() - 1) {
								OffLineMessage offLineMessage = new OffLineMessage();
								offLineMessage.setUserPhone(who);
								offLineMessage.setMsgNum();
								whos.add(offLineMessage);
							}
						}
					}
				}
				for (int i = 0; i < whos.size(); i++) {
					if (MyApplication.dbForQQ.upDataRemind(whos.get(i).getUserPhone(), whos.get(i).getMsgNum()) > 0) {
						RemindManager.doRemind(true, context);
					}
				}
			}

			@Override
			public void onReceiveOfflineMessageCompletion() {
			}

			@Override
			public void onServicePersonVersion(int version) {
			}

			@Override
			public int onGetOfflineMessage() {
				// TODO Auto-generated method stub
				return ECDevice.SYNC_OFFLINE_MSG_ALL;
			}

			@Override
			public void onReceiveDeskMessage(ECMessage arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSoftVersion(String arg0, int arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onReceiveMessageNotify(ECMessageNotify arg0) {
				// TODO Auto-generated method stub
				
			}
		});

		if (params.validate()) {
			ECDevice.login(params);
		}
	}

	public static void logout() {
		ECDevice.logout(new ECDevice.OnLogoutListener() {
			@Override
			public void onLogout() {
				LogUtil.showLog(TAG, "logout_succeed");
			}
		});
	}

	/**
	 * 发送文字（包含表情）
	 * 
	 * @param to
	 *            接收方用户id
	 * @param message
	 *            发送的内容
	 * @param userName
	 *            接收方用户昵称
	 */
	public static void sendMessage(String to, String message,
			final String userName) {
		Log.i("zxw", "to: " + to);
		Log.i("zxw", "from: " + Shared.getUserID());
		try {
			final ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);
			msg.setForm(Shared.getUserID());
			msg.setMsgTime(System.currentTimeMillis());
			msg.setTo(to);
			msg.setSessionId(to);
			msg.setDirection(ECMessage.Direction.SEND);

			final ECTextMessageBody msgBody = new ECTextMessageBody(message);
			msg.setBody(msgBody);
			ECChatManager manager = ECDevice.getECChatManager();
			manager.sendMessage(msg, new OnSendMessageListener() {
				@Override
				public void onSendMessageComplete(ECError error,
						ECMessage message) {
					if (message == null) {
						return;
					}
					if (saveData(msg.getTo(), 1, msgBody.getMessage(),
							userName, 0) > 0) {
						// -------------------------------------
						OnLineActivity.onLineActivity.sendTextSucceed(msgBody
								.getMessage());
					}
				}

				@Override
				public void onProgress(String msgId, int totalByte,
						int progressByte) {

				}

			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送语音
	 * 
	 * @param to
	 *            接收方用户id
	 * @param filePath
	 *            发送的语音文件路径
	 */
	public static void sendMessage(String to, final String filePath,
			final float seconds, final String userName) {
		Log.i("zxw", "to: " + to);
		try {
			// 组建一个待发送的ECMessage
			final ECMessage message = ECMessage
					.createECMessage(ECMessage.Type.VOICE);
			// 设置消息接收者
			message.setForm(Shared.getUserID());
			message.setMsgTime(System.currentTimeMillis());
			message.setTo(to);
			message.setSessionId(to);
			message.setDirection(ECMessage.Direction.SEND);
			// 设置语音包体,语音录制文件需要保存的目录
			ECVoiceMessageBody messageBody = new ECVoiceMessageBody(new File(
					filePath), 0);
			message.setBody(messageBody);
			// 仅录制语音消息，录制完成后需要调用发送接口发送消息
			ECChatManager manager = ECDevice.getECChatManager();
			manager.sendMessage(message, new OnSendMessageListener() {


				@Override
				public void onProgress(String arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSendMessageComplete(ECError arg0, ECMessage arg1) {
					// TODO Auto-generated method stub
					// ------------------发送成功，存入数据库后更新数据库
					if (saveData(message.getTo(), 1, filePath + ":" + seconds,
							userName, 1) > 0) {
						// -------------------------------------
						OnLineActivity.onLineActivity.sendVoiceSucceed(seconds,
								filePath);
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送图片
	 * 
	 * @param to
	 *            接收方用户id
	 * @param filePath
	 *            发送的语音文件路径
	 */
	public static void sendImgMessage(String to, final String filePath,
			final String userName) {
		Log.i("zxw", "to: " + to);
		try {
			// 组建一个待发送的ECMessage
			final ECMessage message = ECMessage
					.createECMessage(ECMessage.Type.IMAGE);
			// 设置消息接收者
			message.setForm(Shared.getUserID());
			message.setMsgTime(System.currentTimeMillis());
			message.setTo(to);
			message.setSessionId(to);
			message.setDirection(ECMessage.Direction.SEND);
			ECImageMessageBody messageBody = new ECImageMessageBody(new File(
					filePath));
			message.setBody(messageBody);
			ECChatManager manager = ECDevice.getECChatManager();
			manager.sendMessage(message, new OnSendMessageListener() {


				@Override
				public void onProgress(String arg0, int arg1, int arg2) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onSendMessageComplete(ECError arg0, ECMessage arg1) {
					// TODO Auto-generated method stub
					// ------------------发送成功，存入数据库后更新数据库
					if (saveData(message.getTo(), 1, filePath, userName, 2) > 0) {
						// -------------------------------------
						OnLineActivity.onLineActivity.sendImgSucceed(filePath);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public static void setUserMessage(String username, Sex sexes,
			String birthday) {
		String nickName = username;
		Sex sex = sexes;
		String birth = birthday;
		ECChatManager manager = ECDevice.getECChatManager();
		manager.setPersonInfo(nickName, sex, birth,
				new ECChatManager.OnSetPersonInfoListener() {
					@Override
					public void onSetPersonInfoComplete(ECError error,
							int version) {
						if (SdkErrorCode.REQUEST_SUCCESS == error.errorCode) {
							return;
						}
						// Log.e("ECSDK_Demo", "set person info fail  " +
						// ", errorCode=" + error.errorCode);
					}
				});
	}

	public static void getUserMessage() {
		ECChatManager manager = ECDevice.getECChatManager();
		manager.getPersonInfo(new ECChatManager.OnGetPersonInfoListener() {
			@Override
			public void onGetPersonInfoComplete(ECError error, PersonInfo p) {
				if (SdkErrorCode.REQUEST_SUCCESS == error.errorCode) {
					return;
				}
				// Log.e("ECSDK_Demo", "get person info fail  " +
				// ", errorCode=" + error.errorCode);
			}
		});
	}

	private static long saveData(String user, int fromOrTo, String message,
			String userName, int msgType) {
		long l = -1;
		l = MyApplication.dbForQQ.insert(user, userName, fromOrTo, message,
				msgType);
		return l;
	}

	private static void showOffLine(final Context ct) {
		final Dialog myDialog = new Dialog(ct);
		// myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialog.show();
		myDialog.setCanceledOnTouchOutside(false);
		myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		Window win = myDialog.getWindow();
		win.setContentView(R.layout.dialog_offline);

		Button setting = (Button) win.findViewById(R.id.offline_setting);
		Button close = (Button) win.findViewById(R.id.offline_close);

		setting.setOnTouchListener(ImgBtnEffact.btnTL);
		close.setOnTouchListener(ImgBtnEffact.btnTL);
		setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				init(ct);
				SDKCoreHelper.isShow = false;
				myDialog.dismiss();
			}
		});

		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MainActivity activity = (MainActivity) ct;
				activity.islogout = false;
				SDKCoreHelper.isShow = false;
				myDialog.dismiss();
				activity.finish();
			}
		});

		myDialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				if (isShow) {
					showOffLine(ct);
				}
			}
		});
	}

	private static void download(final String s_url, final String fileType,
			final ChatMsgEntity chatMsgEntity, final int isOffLine) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				File file = new File(Environment.getExternalStorageDirectory()
						.getPath()
						+ "/CloudyFarm/"
						+ new Date(System.currentTimeMillis()).getTime()
						+ fileType);
				// 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
				if (file.exists()) {
					file.delete();
				}
				try {
					// 构造URL
					URL url = new URL(s_url);
					// 打开连接
					URLConnection con = url.openConnection();
					// 获得文件的长度
					int contentLength = con.getContentLength();
					System.out.println("长度 :" + contentLength);
					// 输入流
					InputStream is = con.getInputStream();
					// 1K的数据缓冲
					byte[] bs = new byte[1024];
					// 读取到的数据长度
					int len;
					// 输出的文件流
					OutputStream os = new FileOutputStream(file);
					// 开始读取
					while ((len = is.read(bs)) != -1) {
						os.write(bs, 0, len);
					}
					// 完毕，关闭所有链接
					os.close();
					is.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
				Message message = new Message();
				LogUtil.showLog(TAG, file.getPath());
				chatMsgEntity.setText(file.getPath());
				message.obj = chatMsgEntity;
				message.arg1 = isOffLine;
				handler.sendMessage(message);
			}
		}).start();
	}

	private static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			ChatMsgEntity chatMsgEntity = (ChatMsgEntity) msg.obj;
			
			if (chatMsgEntity.getType() == ChatMsgEntity.VOICE) {
				try {
					chatMsgEntity.setVoiceleng((int)getAmrDuration(new File(chatMsgEntity.getText())));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				saveData(getUserID(chatMsgEntity.getName()), 0, chatMsgEntity.getText(), chatMsgEntity.getName(), 1);
				
			} else {
				saveData(getUserID(chatMsgEntity.getName()), 0, chatMsgEntity.getText(), chatMsgEntity.getName(), 2);
			}
			
			if (msg.arg1 == 1) {
				if (OnLineActivity.onLineActivity != null) {
					OnLineActivity.onLineActivity.getMsg(chatMsgEntity);
					RemindManager.doRemind(false, context);
				} else {
					if (MyApplication.dbForQQ.upDataRemind(getUserID(chatMsgEntity.getName()), false) > 0) {
						if (ComFragment.comFragment != null) {
							ComFragment.comFragment.upDataRemind();
						}
						if (isBackgroundRunning()) {
							int num = MyApplication.dbForQQ.queryRemind(chatMsgEntity.getId());
							NotificationUtil.sendNot(context, chatMsgEntity.getId(), chatMsgEntity.getName(), getPhone(chatMsgEntity.getId()), chatMsgEntity.getDate(), num);
						} else {
							RemindManager.doRemind(true, context);
						}
					}
				}
			}
		}
	};
	
	public static long getAmrDuration(File file) throws IOException {
		long duration = -1;
		int[] packedSize = { 12, 13, 15, 17, 19, 20, 26, 31, 5, 0, 0, 0, 0,
				0, 0, 0 };
		RandomAccessFile randomAccessFile = null;
		try {
			randomAccessFile = new RandomAccessFile(file, "rw");
			long length = file.length();// 文件的长度
			int pos = 6;// 设置初始位置
			int frameCount = 0;// 初始帧数
			int packedPos = -1;
			// ///////////////////////////////////////////////////
			byte[] datas = new byte[1];// 初始数据值
			while (pos <= length) {
				randomAccessFile.seek(pos);
				if (randomAccessFile.read(datas, 0, 1) != 1) {
					duration = length > 0 ? ((length - 6) / 650) : 0;
					break;
				}
				packedPos = (datas[0] >> 3) & 0x0F;
				pos += packedSize[packedPos] + 1;
				frameCount++;
			}
			// ///////////////////////////////////////////////////
			duration += frameCount * 20;// 帧数*20
		} finally {
			if (randomAccessFile != null) {
				randomAccessFile.close();
			}
		}
		return duration/1000;
	}
	
	private static String getUserID(String userName) {
		// TODO Auto-generated method stub
		String userID = null;
		List<Contactor> contactorList = MyApplication.servers;
		for (int i = 0; i < contactorList.size(); i++) {
			if (contactorList.get(i).getName().equals(userName)) {
				userID = contactorList.get(i).getId();
				break;
			}
		}
		LogUtil.showLog(TAG, "userID: " + userID);
		return userID;
	}
	
//	public static PendingIntent getDefalutIntent(int flags,String dkcode){
//		Intent inte =new Intent(SDKCoreHelper.context,ProductDetailActivity.class);
//		inte.putExtra("dkid",dkcode); 
//	      PendingIntent pendingIntent= PendingIntent.getActivity(SDKCoreHelper.context, 1, inte, flags);
//	      return pendingIntent;
//	}

	private static String getPhone(String userID) {
		// TODO Auto-generated method stub
		String phone = null;
		List<Contactor> contactorList = MyApplication.servers;
		for (int i = 0; i < contactorList.size(); i++) {
			if (contactorList.get(i).getId().equals(userID)) {
				phone = contactorList.get(i).getPhone();
				break;
			}
		}
		LogUtil.showLog(TAG, "phone: " + phone);
		return phone;
	}
	
	private static String getUserName(String userID) {
		// TODO Auto-generated method stub
		String userName = null;
		List<Contactor> contactorList = MyApplication.servers;
		for (int i = 0; i < contactorList.size(); i++) {
			if (contactorList.get(i).getId().equals(userID)) {
				userName = contactorList.get(i).getName();
				break;
			}
		}
		LogUtil.showLog(TAG, "userName: " + userName);
		return userName;
	}
	
	@SuppressWarnings("static-access")
	private static boolean isBackgroundRunning() {
		String processName = context.getPackageName();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		KeyguardManager keyguardManager = (KeyguardManager) context
				.getSystemService(context.KEYGUARD_SERVICE);

		if (activityManager == null)
			return false;
		// get running application processes
		List<ActivityManager.RunningAppProcessInfo> processList = activityManager
				.getRunningAppProcesses();
		for (ActivityManager.RunningAppProcessInfo process : processList) {
			if (process.processName.startsWith(processName)) {
				boolean isBackground = process.importance != IMPORTANCE_FOREGROUND
						&& process.importance != IMPORTANCE_VISIBLE;
				boolean isLockedState = keyguardManager
						.inKeyguardRestrictedInputMode();
				if (isBackground || isLockedState)
					return true;
				else
					return false;
			}
		}
		return false;
	}
}