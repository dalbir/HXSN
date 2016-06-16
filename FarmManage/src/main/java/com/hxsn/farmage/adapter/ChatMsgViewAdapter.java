
package com.hxsn.farmage.adapter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

import com.hxsn.farmage.R;
import com.hxsn.farmage.beans.ChatMsgEntity;
import com.hxsn.farmage.utils.EmojiUtil;
import com.hxsn.farmage.utils.HeadImgUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class ChatMsgViewAdapter extends BaseAdapter
{

	public static interface IMsgViewType
	{
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

	private List<ChatMsgEntity> coll;
	private Context ctx;
	private LayoutInflater mInflater;

	public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll)
	{
		ctx = context;
		this.coll = coll;
		mInflater = LayoutInflater.from(context);
	}

	public int getCount()
	{
		return coll.size();
	}

	public Object getItem(int position)
	{
		return coll.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	public int getItemViewType(int position)
	{
		// TODO Auto-generated method stub
		ChatMsgEntity entity = coll.get(position);

		if (entity.getMsgType())
		{
			return IMsgViewType.IMVT_COM_MSG;
		} else
		{
			return IMsgViewType.IMVT_TO_MSG;
		}
	}

	public int getViewTypeCount()
	{
		// TODO Auto-generated method stub
		return 2;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{

		ChatMsgEntity entity = coll.get(position);
		boolean isComMsg = entity.getMsgType();
		int type = entity.getType();
		ViewHolder viewHolder = null;
		if (viewHolder == null)
		{
			convertView = createViewByMessage(type, isComMsg);
			viewHolder = new ViewHolder();
			convertView.setTag(viewHolder);
		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		// ��ʼ���ؼ�
		if (type == ChatMsgEntity.TEXT && isComMsg == true)
		{
			//�����ı�����Ϣ��View��ʼ��
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
			viewHolder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);

		}else if (type == ChatMsgEntity.TEXT && isComMsg == false)
		{
			//�����ı���ϢView�ĳ�ʼ��
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
			viewHolder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
			
		} else if (type == ChatMsgEntity.PICTURE && isComMsg == true)
		{
			//����ͼƬ��View�ĳ�ʼ��
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.timestamp);
			viewHolder.tvUserName =(TextView) convertView.findViewById(R.id.tv_userid); 
			viewHolder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_sendPicture);
			
		} else if (type == ChatMsgEntity.PICTURE && isComMsg == false)
		{
			//����ͼƬ��View�ĳ�ʼ��
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.timestamp);
			viewHolder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_sendPicture);
			viewHolder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);

		} else if (type == ChatMsgEntity.VOICE && isComMsg == true)
		{
			//����������View�ĳ�ʼ��
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.timestamp);
			viewHolder.tvUserName =(TextView) convertView.findViewById(R.id.tv_userid); 
			viewHolder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_voice);
			viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_length);
			
		} else if (type == ChatMsgEntity.VOICE && isComMsg == false)
		{
			//��������View�ĳ�ʼ��
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.timestamp);
			viewHolder.head_iv = (ImageView) convertView.findViewById(R.id.iv_userhead);
			viewHolder.tv = (TextView) convertView.findViewById(R.id.tv_length);
			viewHolder.iv = (ImageView) convertView.findViewById(R.id.iv_voice);
			viewHolder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
			viewHolder.hahaha = (RelativeLayout) convertView.findViewById(R.id.hahahahahaha);
			
		}

		// �������������
		SendMess(isComMsg, type, entity, viewHolder);

		return convertView;
	}

	@SuppressLint("NewApi")
	private void SendMess(boolean zuo_you, int Type, final ChatMsgEntity entity, final ViewHolder holder)
	{
		new HeadImgUtil(entity.getId(), entity.getHeadUrl(), holder.head_iv, ctx);
		
		if (Type == ChatMsgEntity.TEXT && zuo_you == false)
		{
			holder.tvSendTime.setText(entity.getDate());
			try {
				EmojiUtil.handlerEmojiText(holder.tvContent, entity.getText(), ctx);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else if (Type == ChatMsgEntity.TEXT && zuo_you == true)
		{
			holder.tvSendTime.setText(entity.getDate());
			holder.tvUserName.setText(entity.getName());
			try {
				EmojiUtil.handlerEmojiText(holder.tvContent, entity.getText(), ctx);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} else if (Type == ChatMsgEntity.PICTURE  && zuo_you == false)
		{
			holder.tvSendTime.setText(entity.getDate());
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 6;// ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ
			Bitmap b = BitmapFactory.decodeFile(entity.getText(), options);
			holder.iv.setImageBitmap(b);
		}else if (Type == ChatMsgEntity.PICTURE  && zuo_you == true)
		{
			holder.tvSendTime.setText(entity.getDate());
			holder.tvUserName.setText(entity.getName());
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 6;// ͼƬ��߶�Ϊԭ���Ķ���֮һ����ͼƬΪԭ�����ķ�֮һ
			Bitmap b = BitmapFactory.decodeFile(entity.getText(), options);
			holder.iv.setImageBitmap(b);
		} else if (Type == ChatMsgEntity.VOICE && zuo_you == false)
		{
			holder.tvSendTime.setText(entity.getDate());
			holder.tv.setText(entity.getVoiceleng() + "\"");
			holder.iv.setBackgroundResource(R.drawable.voice_anim);
			holder.iv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					try
					{
						final MediaPlayer mediaPlayer = new MediaPlayer();
						final AnimationDrawable animationDrawable = (AnimationDrawable) holder.iv.getBackground();
						if (mediaPlayer.isPlaying())
						{
							mediaPlayer.reset();// ����Ϊ��ʼ״̬
						}
						mediaPlayer.setDataSource(entity.getText());
						mediaPlayer.prepare();// ����
						mediaPlayer.start();// ��ʼ��ָ�����
						animationDrawable.start();
						mediaPlayer.setOnCompletionListener(new OnCompletionListener()
						{
							@Override
							public void onCompletion(MediaPlayer mp)
							{
								// TODO Auto-generated method stub
								animationDrawable.stop();
								holder.iv.setBackgroundResource(R.drawable.voice_anim);
							}
						});
					} catch (Exception e)
					{
						// TODO: handle exception
					}
				}
			});
		}else if (Type == ChatMsgEntity.VOICE && zuo_you == true)
		{
			holder.tvSendTime.setText(entity.getDate());
			holder.tvUserName.setText(entity.getName());
			holder.tv.setText(entity.getVoiceleng() + "\"");
			holder.iv.setBackgroundResource(R.drawable.voice_left_anim);
			holder.iv.setOnClickListener(new OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					// TODO Auto-generated method stub
					try
					{
						final MediaPlayer mediaPlayer = new MediaPlayer();
						final AnimationDrawable animationDrawable = (AnimationDrawable) holder.iv.getBackground();
						if (mediaPlayer.isPlaying())
						{
							mediaPlayer.reset();// ����Ϊ��ʼ״̬
						}
						mediaPlayer.setDataSource(entity.getText());
						mediaPlayer.prepare();// ����
						mediaPlayer.start();// ��ʼ��ָ�����
						animationDrawable.start();
						mediaPlayer.setOnCompletionListener(new OnCompletionListener()
						{
							@Override
							public void onCompletion(MediaPlayer mp)
							{
								// TODO Auto-generated method stub
								animationDrawable.stop();
								holder.iv.setBackgroundResource(R.drawable.voice_left_anim);
							}
						});
					} catch (Exception e)
					{
						// TODO: handle exception
					}
				}
			});
		}
	}

	private int dizhi[] = { R.drawable.chatto_voice_playing_f1, R.drawable.chatto_voice_playing_f2,
			R.drawable.chatto_voice_playing_f3 };

	/**
	 * ��Ϣ���ͷ������Ӧ�Ĳ���
	 * 
	 * @������ 2016 1-7
	 * @param Type
	 * @param zuo_you
	 * @return
	 */
	private View createViewByMessage(int Type, boolean zuo_you)
	{
		View view = null;
		if (Type == ChatMsgEntity.TEXT && zuo_you == true)
		{
			view = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);

		} else if (Type == ChatMsgEntity.TEXT && zuo_you == false)
		{
			view = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);

		} else if (Type == ChatMsgEntity.PICTURE && zuo_you == true)
		{
			view = mInflater.inflate(R.layout.row_received_picture, null);
			
		} else if (Type == ChatMsgEntity.PICTURE && zuo_you == false)
		{
			view = mInflater.inflate(R.layout.row_sent_picture, null);

		} else if (Type == ChatMsgEntity.VOICE && zuo_you == true)
		{
			view = mInflater.inflate(R.layout.row_received_voice, null);

		} else if (Type == ChatMsgEntity.VOICE && zuo_you == false)
		{
			view = mInflater.inflate(R.layout.row_sent_voice, null);

		}
		return view;
	}

	/**
	 * ��ȡͼƬ��ID
	 * 
	 * @param name
	 * @return
	 */
	public int getResourceId(String name)// name�����ʶR.drawable�е�ͼ���ļ���
	{
		try
		{
			Field field = R.drawable.class.getField(name);// �����ԴID�ı���(Ҳ������Դ���ļ���)���ȡField����
			return Integer.parseInt(field.get(null).toString());// ȡ�ò�������ԴID�ֶ�(��̬����)��ֵ
		} catch (Exception e)
		{
		}
		return 0;
	}

	static class ViewHolder
	{
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public boolean isComMsg = true;

		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		ImageView head_iv;
		TextView tv_userId;
		ImageView playBtn;
		TextView timeLength;
		TextView size;
		LinearLayout container_status_btn;
		LinearLayout ll_container;
		ImageView iv_read_status;
		// ��ʾ�Ѷ���ִ״̬
		TextView tv_ack;
		// ��ʾ�ʹ��ִ״̬
		TextView tv_delivered;

		TextView tv_file_name;
		TextView tv_file_size;
		TextView tv_file_download_state;
		RelativeLayout hahaha;

	}
}
