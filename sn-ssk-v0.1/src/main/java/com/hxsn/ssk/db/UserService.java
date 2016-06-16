package com.hxsn.ssk.db;

import android.database.sqlite.SQLiteDatabase;

import com.hxsn.ssk.TApplication;
import com.hxsn.ssk.beans.User;


public class UserService {
    private static UserService instance;
    UserDao userdao;
    private DBHelper helper;
    private SQLiteDatabase db;//DatabaseManager.getInstance().openDatabase();

    //写入 ，不然会是出错，是空指针
    private UserService() {
        helper = new DBHelper(TApplication.context);
        //LogUtil.i("UserService", "helper"+helper);
        DatabaseManager.initialize(TApplication.context, helper);
        db = DatabaseManager.getInstance().openDatabase();
        userdao = new UserDao(db);
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void clear() {
        db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();
        try {
            userdao.clear();

            db.setTransactionSuccessful();// 设置事务标志为成功，当结束事务时就会提交事务
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // 结束事务
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void refresh(User user) {
        db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();
        try {
            userdao.clear();
            userdao.add(user);
            db.setTransactionSuccessful();// 设置事务标志为成功，当结束事务时就会提交事务
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // 结束事务
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public User getUser() {
        db = DatabaseManager.getInstance().openDatabase();
        User user = null;
        try {
            user = userdao.find();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return user;
    }

    public void modify(User user) {
        db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();

        try {
            userdao.update(user);

            db.setTransactionSuccessful();// 设置事务标志为成功，当结束事务时就会提交事务
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // 结束事务
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

}
