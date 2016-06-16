package com.hxsn.ssk.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.hxsn.ssk.beans.User;

/**
 * String realName;
 * String nickName;
 * String email;
 * String phone;
 * String address;
 */
public class UserDao {

    private final String SQL_ADD = "Insert into user(userId,userName,realName,nickName,email,phone,address) values(?,?,?,?,?,?,?)";
    private final String SQL_SELECT = "select userId,userName,realName,nickName,email,phone,address from user";
    private final String SQL_DELETE = "delete from user";
    private final String[] USER_STRINGS = {"userId", "userName", "realName", "nickName", "email", "phone", "address"};
    private SQLiteDatabase db;

    public UserDao(SQLiteDatabase db) {
        super();
        this.db = db;
    }


    public void clear() throws Exception {
        db.execSQL(SQL_DELETE);
    }


    public void add(User user) throws Exception {
        db.execSQL(SQL_ADD, new Object[]{user.getId(), user.getName(), user.getRealName(), user.getNickName(), user.getEmail(), user.getPhone(), user.getAddress()});
    }

    public void update(User user) throws Exception {
        ContentValues cv = new ContentValues();
        cv.put(USER_STRINGS[0], user.getId());
        cv.put(USER_STRINGS[1], user.getName());
        cv.put(USER_STRINGS[2], user.getRealName());
        cv.put(USER_STRINGS[3], user.getNickName());
        cv.put(USER_STRINGS[4], user.getEmail());
        cv.put(USER_STRINGS[5], user.getPhone());
        cv.put(USER_STRINGS[6], user.getAddress());

        String[] args = {String.valueOf(user.getId())};
        db.update("user", cv, "userId=?", args);
    }

    public User find() throws Exception {

        User user = null;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(SQL_SELECT, new String[]{});
            user = null;
            if (cursor.moveToFirst()) {
                user = new User();
                user.setId(cursor.getString(cursor.getColumnIndex(USER_STRINGS[0])));
                user.setName(cursor.getString(cursor.getColumnIndex(USER_STRINGS[1])));
                user.setRealName(cursor.getString(cursor.getColumnIndex(USER_STRINGS[2])));
                user.setNickName(cursor.getString(cursor.getColumnIndex(USER_STRINGS[3])));
                user.setEmail(cursor.getString(cursor.getColumnIndex(USER_STRINGS[4])));
                user.setPhone(cursor.getString(cursor.getColumnIndex(USER_STRINGS[5])));
                user.setAddress(cursor.getString(cursor.getColumnIndex(USER_STRINGS[6])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }
}
