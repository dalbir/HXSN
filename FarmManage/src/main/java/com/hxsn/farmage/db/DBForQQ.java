package com.hxsn.farmage.db;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.hxsn.farmage.beans.ChatMsgEntity;
import com.hxsn.farmage.utils.LogUtil;
import com.hxsn.farmage.utils.SDKCoreHelper;

@SuppressLint("SimpleDateFormat")
public class DBForQQ extends BaseDB {

	SimpleDateFormat format;

	public DBForQQ(Context ctx) {
		super(ctx);
		format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	}

	public long insert(String userPhone, String userName, int fromOrTo,
			String message, int msgType) {
		long l = -1;
		openDB();

		String name = queryDBByUserPhone(userPhone);
		if (name.equals("")) {
			String sql = "create table if not exists " + "p" + userPhone
					+ "(id integer primary key autoincrement, " + FROMORTO
					+ " integer not null, " + MESSAGE + " String not null, "
					+ MESSAGE_TYPE + " Integer not null, " + TIME
					+ " datetime not null)";
			db.beginTransaction();
			db.execSQL(sql);
			insert(userPhone, userName);
			name = "p" + userPhone;
			db.setTransactionSuccessful();
			db.endTransaction();
		}

		ContentValues values = new ContentValues();
		values.put(FROMORTO, fromOrTo);
		values.put(MESSAGE, message);
		values.put(MESSAGE_TYPE, msgType);
		Date date = new Date(System.currentTimeMillis());
		values.put(TIME, format.format(date));
		l = db.insert(name, null, values);
		clearDB(name);
		closeDB();
		return l;
	}

	public String queryDBByUserPhone(String userPhone) {
		openDB();
		String name = "";
		Cursor cursor = db.query(DBHelper.TABLE_NAME, null,
				DBHelper.TABLE_USERPHONE + "=?", new String[] { userPhone },
				null, null, null);

		if (cursor.moveToFirst()) {
			name = cursor.getString(cursor
					.getColumnIndex(DBHelper.TABLE_DBNAME));
		}
		cursor.close();
		return name;
	}

	private void clearDB(String name) {
		Cursor cursor = db.query(name, null, null, null, null, null, TIME
				+ " desc");
		if (cursor.getCount() > 1000) {
			if (cursor.moveToPosition(50)) {
				String time = cursor.getString(cursor.getColumnIndex(TIME));
				db.delete(name, TIME + " < '" + time + "'", null);
			}
		}
	}

	private long insert(String userPhone, String userName) {
		ContentValues values = new ContentValues();
		values.put(DBHelper.TABLE_USERPHONE, userPhone);
		values.put(DBHelper.TABLE_USERNAME, userName);
		values.put(DBHelper.TABLE_DBNAME, "p" + userPhone);
		return db.insert(DBHelper.TABLE_NAME, null, values);
	}

	public String queryUserPic(String userPhone) {
		String userPic = "";
		openDB();
		Cursor cursor = db.query(DBHelper.TABLE_NAME, null,
				DBHelper.TABLE_USERPHONE + "=?", new String[] { userPhone },
				null, null, null);
		if (cursor.moveToFirst()) {
			userPic = cursor.getString(cursor
					.getColumnIndex(DBHelper.TABLE_USERPIC));
		}
		closeDB();
		return userPic;
	}

	public int upDataRemind(String userPhone, int num) {
		int l = -1;

		int remind = queryRemind(userPhone);
		openDB();
		ContentValues values = new ContentValues();
		values.put(DBHelper.TABLE_OFFLINES, remind + num);
		l = db.update(DBHelper.TABLE_NAME, values, DBHelper.TABLE_USERPHONE
				+ "=?", new String[] { userPhone });

		closeDB();
		return l;
	}

	public int upDataRemind(String userPhone, boolean isClear) {
		int l = -1;
		if (!isClear) {
			int remind = queryRemind(userPhone);
			openDB();
			ContentValues values = new ContentValues();
			values.put(DBHelper.TABLE_OFFLINES, remind + 1);
			l = db.update(DBHelper.TABLE_NAME, values, DBHelper.TABLE_USERPHONE
					+ "=?", new String[] { userPhone });

		} else {
			openDB();

			ContentValues values = new ContentValues();
			values.put(DBHelper.TABLE_OFFLINES, 0);
			l = db.update(DBHelper.TABLE_NAME, values, DBHelper.TABLE_USERPHONE
					+ "=?", new String[] { userPhone });

		}

		closeDB();
		return l;
	}

	public int queryRemind(String userPhone) {
		int remind = 0;
		openDB();
		Cursor cursor = db.query(DBHelper.TABLE_NAME, null,
				DBHelper.TABLE_USERPHONE + "=?", new String[] { userPhone },
				null, null, null);
		if (cursor.moveToFirst()) {
			remind = cursor.getInt(cursor
					.getColumnIndex(DBHelper.TABLE_OFFLINES));
		}
		cursor.close();
		closeDB();
		return remind;
	}

	public ChatMsgEntity queyOneChat(String userPhone) {
		ChatMsgEntity chatMsgEntity = null;
		openDB();

		String dbName = queryDBByUserPhone(userPhone);
		if (dbName.length() > 0) {
			Cursor cuosor = db.query(dbName, null, null, null, null, null, TIME
					+ " desc");
			if (cuosor.moveToFirst()) {
				chatMsgEntity = new ChatMsgEntity();
				String time = cuosor.getString(cuosor.getColumnIndex(TIME));
				chatMsgEntity.setDate(time.substring(0, time.length() - 3));
				chatMsgEntity.setText(cuosor.getString(cuosor
						.getColumnIndex(MESSAGE)));
				int type = cuosor.getInt(cuosor.getColumnIndex(FROMORTO));
				if (type == 0) {
					chatMsgEntity.setMsgType(true);
				} else if (type == 1) {
					chatMsgEntity.setMsgType(false);
				}
				chatMsgEntity.setType(cuosor.getInt(cuosor.getColumnIndex(MESSAGE_TYPE)));
			}
			cuosor.close();
		}
		closeDB();
		return chatMsgEntity;
	}

	@SuppressWarnings("static-access")
	public ArrayList<ChatMsgEntity> queyChat(String userPhone, int num) {
		ArrayList<ChatMsgEntity> chatList = null;
		openDB();

		String dbName = queryDBByUserPhone(userPhone);
		if (dbName.length() > 0) {
			Cursor cuosor = db.query(dbName, null, null, null, null, null, TIME
					+ " desc");
			if (cuosor.moveToFirst()) {
				chatList = new ArrayList<ChatMsgEntity>();
				if (num > 20) {
					num = 20;
				}
				for (int i = 0; i < num; i++) {
					ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
					String time = cuosor.getString(cuosor.getColumnIndex(TIME));
					chatMsgEntity.setDate(time.substring(0, time.length() - 3));
					chatMsgEntity.setId(userPhone);
					chatMsgEntity.setSingle(true);
					chatMsgEntity.setText(cuosor.getString(cuosor
							.getColumnIndex(MESSAGE)));
					int type = cuosor.getInt(cuosor.getColumnIndex(FROMORTO));
					if (type == 0) {
						chatMsgEntity.setMsgType(true);
					} else if (type == 1) {
						chatMsgEntity.setMsgType(false);
					}
					int msgtype = cuosor.getInt(cuosor
							.getColumnIndex(MESSAGE_TYPE));
					LogUtil.showLog("DBForQQ", "msgtype: " + msgtype);
					if (msgtype == 0) {
						chatMsgEntity.setType(ChatMsgEntity.TEXT);
					} else if (msgtype == 1) {
						chatMsgEntity.setType(ChatMsgEntity.VOICE);
						try {
							chatMsgEntity.setVoiceleng((int) SDKCoreHelper
									.getAmrDuration(new File(chatMsgEntity
											.getText())));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (msgtype == 2) {
						chatMsgEntity.setType(ChatMsgEntity.PICTURE);
					}
					chatList.add(chatMsgEntity);
					if (!cuosor.moveToNext()) {
						break;
					}
				}
			}
			cuosor.close();
		}
		closeDB();
		return chatList;
	}

	@SuppressWarnings("static-access")
	public ArrayList<ChatMsgEntity> queyChatHistory(String userPhone,
			int location) {
		ArrayList<ChatMsgEntity> chatList = null;
		openDB();

		String dbName = queryDBByUserPhone(userPhone);
		if (dbName.length() > 0) {
			Cursor cuosor = db.query(dbName, null, null, null, null, null, TIME
					+ " desc");
			if (cuosor.moveToPosition(location)) {
				chatList = new ArrayList<ChatMsgEntity>();

				for (int i = 0; i < 20; i++) {
					ChatMsgEntity chatMsgEntity = new ChatMsgEntity();
					String time = cuosor.getString(cuosor.getColumnIndex(TIME));
					chatMsgEntity.setDate(time.substring(0, time.length() - 3));
					chatMsgEntity.setId(userPhone);
					chatMsgEntity.setSingle(true);
					chatMsgEntity.setText(cuosor.getString(cuosor
							.getColumnIndex(MESSAGE)));
					int type = cuosor.getInt(cuosor.getColumnIndex(FROMORTO));
					if (type == 0) {
						chatMsgEntity.setMsgType(true);
					} else if (type == 1) {
						chatMsgEntity.setMsgType(false);
					}
					int msgtype = cuosor.getInt(cuosor
							.getColumnIndex(MESSAGE_TYPE));
					LogUtil.showLog("DBForQQ", "msgtype: " + msgtype);
					if (msgtype == 0) {
						chatMsgEntity.setType(ChatMsgEntity.TEXT);
					} else if (msgtype == 1) {
						chatMsgEntity.setType(ChatMsgEntity.VOICE);
						try {
							chatMsgEntity.setVoiceleng((int) SDKCoreHelper
									.getAmrDuration(new File(chatMsgEntity
											.getText())));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (msgtype == 2) {
						chatMsgEntity.setType(ChatMsgEntity.PICTURE);
					}
					chatList.add(chatMsgEntity);
					if (!cuosor.moveToNext()) {
						break;
					}
				}
			}
			cuosor.close();
		}
		closeDB();
		return chatList;
	}

	public int delete(String userPhone) {
		int i = -1;
		openDB();
		String name = queryDBByUserPhone(userPhone);
		String sql = "drop table if exists " + name;
		db.beginTransaction();
		db.execSQL(sql);
		i = db.delete(DBHelper.TABLE_NAME, DBHelper.TABLE_DBNAME + "=?",
				new String[] { name });
		db.setTransactionSuccessful();
		db.endTransaction();
		closeDB();
		return i;
	}

	public void deleteAll() {
		openDB();
		Cursor cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			for (int i = 0; i < cursor.getCount(); i++) {
				String tableName = cursor.getString(cursor
						.getColumnIndex(DBHelper.TABLE_DBNAME));
				String sql = "drop table if exists " + tableName;
				db.execSQL(sql);
				cursor.moveToNext();
			}

			db.delete(DBHelper.TABLE_NAME, null, null);
		}
	}
}