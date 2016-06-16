package com.hxsn.ssk.biz;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import com.hxsn.ssk.TApplication;
import com.hxsn.ssk.beans.AppVersion;
import com.hxsn.ssk.utils.AndroidUtil;
import com.hxsn.ssk.utils.Const;
import com.hxsn.ssk.utils.DebugUtil;
import com.hxsn.ssk.utils.DownloadUtil;
import com.hxsn.ssk.utils.ExceptionUtil;
import com.hxsn.ssk.utils.JsonUtil;
import com.hxsn.ssk.utils.MyFileUtil;

import java.io.File;

/**
 * Created by jiely on 2016/4/19.
 */
public class AppBiz {

    private static Context context;
    private String urlString;

    public AppBiz(Context context) {
        AppBiz.context = context;
    }

    /**
     * desc:获取划客最新APPVersion，如果版本旧了就更新
     * auther:jiely
     * create at 2015/10/10 19:54
     */
    public void downApkAndInstall() {
        new HttpRequest() {
            @Override
            public void getResponse(String response) {
                String jsonString = HttpRequest.result;
                DebugUtil.d("AppBiz", "result=" + jsonString);
                AppVersion appVersion = JsonUtil.getAppVersion(jsonString);
                int nshVersion = appVersion.getNshversion();

                //农事汇图标更新
                TApplication.isUpdateNongshImage = false;
                if(TApplication.getValue(Const.CODE_NSH_VERSION_KEY) == null){
                    TApplication.setValue(Const.CODE_NSH_VERSION_KEY,String.valueOf(nshVersion));
                    TApplication.isUpdateNongshImage = true;
                }else {
                    int oldNshVersion = Integer.parseInt(TApplication.getValue(Const.CODE_NSH_VERSION_KEY));
                    if(nshVersion > oldNshVersion){
                        TApplication.setValue(Const.CODE_NSH_VERSION_KEY,String.valueOf(nshVersion));
                        TApplication.isUpdateNongshImage = true;
                    }
                }

                DebugUtil.d("nshVersion="+nshVersion+"+oldNshVersion="+TApplication.getValue(Const.CODE_NSH_VERSION_KEY)
                        +"+TApplication.isNongshImage"+TApplication.isUpdateNongshImage);

                urlString = appVersion.getUrl();

                if (appVersion != null) {
                    String thisAppVersion = AndroidUtil.getThisAppVersion();
                    if (AndroidUtil.isNewVersion(appVersion.getVersion(), thisAppVersion)) {
                        new AlertDialog.Builder(context)
                                .setMessage("是否升级")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AndroidUtil.show(context, "APP在下载中，请稍等");
                                        try {
                                            new DownApkTask().execute(urlString);
                                        } catch (Exception e) {
                                            ExceptionUtil.handle(e);
                                        }
                                    }
                                }).setNegativeButton("取消", null).show();
                    }
                }
            }
        }.doPostRequest(TApplication.URL_UPDATE);
    }

    class DownApkTask extends AsyncTask<String, Void, Integer> {
        private String absolutePath;

        @Override
        protected Integer doInBackground(String... params) {
            long appLength;
            try {
                Boolean isSuccess = MyFileUtil.deleteAllFile(Const.PATH_APK);
                if (!isSuccess) {
                    return 600;
                }
                String fileName = MyFileUtil.getFileName(urlString);
                //MyFileUtil.deleteAndCreateFile(Const.UPDATE_APK_PATH, fileName);
                absolutePath = Const.PATH_APK + fileName;
                appLength = DownloadUtil.downloadFileToLocal(urlString, absolutePath, null);//.downloadFileToLocal(urlString, absolutePath, null);
                if (appLength > 0) {
                    return 200;
                }
            } catch (Exception e) {
                ExceptionUtil.handle(e);
                return 500;
            }

            return 500;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if (result == 200) {
                // 下载完成，点击安装
                File file = new File(absolutePath);
                Uri uri = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                context.startActivity(intent);
            } else if (result == 500) {
                AndroidUtil.show(context, "下载失败");
            } else {
                AndroidUtil.show(context, "文件错误");
            }
        }
    }
}
