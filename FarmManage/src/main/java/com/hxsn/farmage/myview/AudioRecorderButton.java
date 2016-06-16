package com.hxsn.farmage.myview;

import com.hxsn.farmage.R;
import com.hxsn.farmage.myview.AudioManager.AudioStateListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

@SuppressLint({ "ClickableViewAccessibility", "HandlerLeak" })
public class AudioRecorderButton extends Button
{

	private static final int STATE_NORMAL = 1;// Ĭ�ϵ�״̬
	private static final int STATE_RECORDING = 2;// ����¼��
	private static final int STATE_WANT_TO_CANCEL = 3;// ϣ��ȡ��

	private static final int DISTANCE_Y_CANCEL = 50;
	private int mCurrentState = STATE_NORMAL; // ��ǰ��״̬
	private boolean isRecording = false;// �Ѿ���ʼ¼��

	private DialogManager mDialogManager;
	private AudioManager mAudioManager;

	private float mTime;
	// �Ƿ񴥷�longclik
	private boolean mReady;

	public AudioRecorderButton(Context context)
	{
		this(context, null);

	}

	public AudioRecorderButton(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mDialogManager = new DialogManager(context);

		String dir = Environment.getExternalStorageDirectory() + "/CloudyFarm";
		mAudioManager = AudioManager.getInstance(dir);
		mAudioManager.setOnAudioStateListener(new AudioStateListener()
		{
			@Override
			public void wellPrepared()
			{
				// TODO Auto-generated method stub
				handler.sendEmptyMessage(MSG_AUDIO_PREPARED);
			}
		});

		setOnLongClickListener(new OnLongClickListener()
		{
			@Override
			public boolean onLongClick(View v)
			{
				mReady = true;
				mAudioManager.prepareAudio();
				return false;
			}
		});
	}
	
	/**
	 * ¼����ɺ�Ļص�
	 * @author ����
	 *
	 */
	public interface AudioFinishRecorderListener
	{
		void onFinish(float seconds, String filePath);
	}
	
	private AudioFinishRecorderListener mListener;
	
	public void setAudioFinishRecorderListener(AudioFinishRecorderListener listener)
	{
		this.mListener = listener;
	}

	private static final int MSG_AUDIO_PREPARED = 0x110;
	private static final int MSG_VOICE_CHANGED = 0x111;
	private static final int MSG_DIALOG_DIMISS = 0x112;

	/**
	 * ��ȡ������С��Runnable
	 */
	private Runnable mGetVoiceLevelRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			while (isRecording)
			{
				try
				{
					Thread.sleep(100);
					mTime += 0.1f;
					handler.sendEmptyMessage(MSG_VOICE_CHANGED);
				} catch (Exception e)
				{
					// TODO: handle exception
					Log.i("Mess", e.getMessage());
				}
			}
		}
	};

	private Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			switch (msg.what)
			{
			case MSG_AUDIO_PREPARED:

				// Auto-generated method stub
				mDialogManager.showRecordingDialog();
				isRecording = true;
				new Thread(mGetVoiceLevelRunnable).start();

				break;
			case MSG_VOICE_CHANGED:
				mDialogManager.updateVoiceLevel(mAudioManager.getVoiceLevel(7));
				break;
			case MSG_DIALOG_DIMISS:
				mDialogManager.dimissDialog();
				break;
			}
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		// TODO Auto-generated method stub
		int action = event.getAction();
		int x = (int) event.getX();// ���x������
		int y = (int) event.getY();// ���y������

		switch (action)
		{
		case MotionEvent.ACTION_DOWN:
			changeState(STATE_RECORDING);
			break;
		case MotionEvent.ACTION_MOVE:
			if (isRecording)
			{
				if (wantToCancle(x, y))
				{
					changeState(STATE_WANT_TO_CANCEL);
				} else
				{
					changeState(STATE_RECORDING);
				}
			}
			break;
		case MotionEvent.ACTION_UP:

			if (!mReady)
			{
				reset();
				return super.onTouchEvent(event);
			}

			if (!isRecording || mTime < 0.6f)
			{
				mDialogManager.tooShort();
				mAudioManager.cancel();
				handler.sendEmptyMessageDelayed(MSG_DIALOG_DIMISS, 1300);
			}else if (mCurrentState == STATE_RECORDING)
			{ // ����¼����ʱ�򣬽���
				mDialogManager.dimissDialog();
				mAudioManager.release();
				if (mListener != null)
				{
					mListener.onFinish(mTime, mAudioManager.getCurrentFilePath());
				}
				
				
			} else if (mCurrentState == STATE_WANT_TO_CANCEL)
			{ // ��Ҫȡ��
				mDialogManager.dimissDialog();
				mAudioManager.cancel();
			}
			reset();
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * �؏͘��Iλ
	 */
	private void reset()
	{
		// TODO Auto-generated method stub
		isRecording = false;
		mReady = false;
		changeState(STATE_NORMAL);
		mTime = 0;
	}

	/**
	 * ����XY�������ж��Ƿ�ȡ��
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean wantToCancle(int x, int y)
	{
		// TODO Auto-generated method stub
		if (x < 0 || x > getWidth())
		{ // ������ť�Ŀ��
			return true;
		}
		// ������ť�ĸ߶�
		if (y < -DISTANCE_Y_CANCEL || y > getHeight() + DISTANCE_Y_CANCEL)
		{
			return true;
		}

		return false;
	}

	private void changeState(int state)
	{
		// TODO Auto-generated method stub
		if (mCurrentState != state)
		{
			mCurrentState = state;
			switch (state)
			{
			case STATE_NORMAL:
				setBackgroundResource(R.drawable.btn_recorder_normal);
				setText(R.string.str_recorder_normal);
				break;
			case STATE_RECORDING:
				setBackgroundResource(R.drawable.btn_recorder);
				setText(R.string.str_recorder_recorder);
				if (isRecording)
				{
					mDialogManager.recording();
				}
				break;
			case STATE_WANT_TO_CANCEL:
				setBackgroundResource(R.drawable.btn_recorder);
				setText(R.string.str_recorder_want_cancel);
				mDialogManager.wantToCancel();
				break;
			}
		}

	}
}
