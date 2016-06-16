package com.hxsn.ssk.db;

import android.database.sqlite.SQLiteDatabase;

import com.hxsn.ssk.TApplication;
import com.hxsn.ssk.beans.Nongsh;

import java.util.List;


public class NongshService {
    private static NongshService instance;
    NongshDao nongshDao;
    private DBHelper helper;
    private SQLiteDatabase db;//DatabaseManager.getInstance().openDatabase();

    //写入 ，不然会是出错，是空指针
    private NongshService() {
        helper = new DBHelper(TApplication.context);
        //LogUtil.i("UserService", "helper"+helper);
        DatabaseManager.initialize(TApplication.context, helper);
        db = DatabaseManager.getInstance().openDatabase();
        nongshDao = new NongshDao(db);
    }

    public static NongshService getInstance() {
        if (instance == null) {
            instance = new NongshService();
        }
        return instance;
    }

    public void clear() {
        db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();
        try {
            nongshDao.clear();

            db.setTransactionSuccessful();// 设置事务标志为成功，当结束事务时就会提交事务
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // 结束事务
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public void add(Nongsh nongsh) {
        db = DatabaseManager.getInstance().openDatabase();
        db.beginTransaction();
        try {
            nongshDao.insert(nongsh);
            db.setTransactionSuccessful();// 设置事务标志为成功，当结束事务时就会提交事务
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // 结束事务
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public List<Nongsh> getNongshList() {
        db = DatabaseManager.getInstance().openDatabase();
        List<Nongsh> nongshList = null;
        try {
            nongshList = nongshDao.findAll();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DatabaseManager.getInstance().closeDatabase();
        }
        return nongshList;
    }

}
