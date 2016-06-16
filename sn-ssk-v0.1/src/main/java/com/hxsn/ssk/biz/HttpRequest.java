package com.hxsn.ssk.biz;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hxsn.ssk.TApplication;
import com.hxsn.ssk.utils.AndroidUtil;
import com.hxsn.ssk.utils.DebugUtil;
import com.hxsn.ssk.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Map;


/**
 *  Created by jiely on 2016/4/18.
 */
public abstract class HttpRequest {

    public static String result;

    public HttpRequest() {
    }

    public String getResult() {
        return result;
    }

    public abstract void getResponse(String response);

    public void doGet(final String url) {
        DebugUtil.d("HttpRequest", "url=" + url);
        if (!NetUtil.isConnect(TApplication.context)) {
            AndroidUtil.show(TApplication.context, "没有网络连接");
            return;
        }

        MyStringRequest stringRequest = new MyStringRequest(Request.Method.GET, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                codeSwitch(response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleError(volleyError);
            }
        });

        TApplication.requestQueue.add(stringRequest);
    }

    public void doPostRequest(final String url) {
        DebugUtil.d("HttpRequest", "url=" + url);
        if (!NetUtil.isConnect(TApplication.context)) {
            AndroidUtil.show(TApplication.context, "没有网络连接");
            return;
        }
        MyStringRequest stringRequest = new MyStringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                codeSwitch(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleError(volleyError);
            }
        });
        TApplication.requestQueue.add(stringRequest);
    }


    /**
     * post传参
     * @param url 接口
     * @param map 参数
     */
    public void doPostRequest(final String url,Map<String,String> map) {
        DebugUtil.d("HttpRequest", "url=" + url);
        if (!NetUtil.isConnect(TApplication.context)) {
            AndroidUtil.show(TApplication.context, "没有网络连接");
            return;
        }
        MyStringRequest postRequest = new MyStringRequest(Request.Method.POST,url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                codeSwitch(response);
            }
        },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleError(volleyError);
            }
        });
        postRequest.setpMap(map);
        TApplication.requestQueue.add(postRequest);
    }


    /**
     * post 传参和上传文件
     * @param url      接口
     * @param fileName 上传文件的命名空间
     * @param file      上传的文件
     * @param map      参数
     */
    public void doPostRequest(final String url,String fileName,File file,Map<String,String> map){
        DebugUtil.d("HttpRequest", "url=" + url);
        if (!NetUtil.isConnect(TApplication.context)) {
            AndroidUtil.show(TApplication.context, "没有网络连接");
            return;
        }

        MultipartRequest postRequest = new MultipartRequest(url, fileName,file,map,
            new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        codeSwitch(response);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handleError(volleyError);
            }
        });

        TApplication.requestQueue.add(postRequest);
    }


    private void codeSwitch(String response) {
        DebugUtil.d("HttpRequest", "response=" + response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.getInt("code");
            switch (code) {
                case 200:
                    this.result = jsonObject.getString("result");
                    getResponse(response);
                    break;
                case 101:
                    AndroidUtil.show(TApplication.context, "参数错误");
                    break;
                case 301:
                    AndroidUtil.show(TApplication.context, "用户名不存在或密码错误");
                    break;
                case 302:
                    AndroidUtil.show(TApplication.context, "账号被停用，该账号被管理员停用");
                    break;
                case 320:
                    AndroidUtil.show(TApplication.context, "原始密码错误");
                    break;
                default:
                    AndroidUtil.show(TApplication.context, "未知错误");
                    break;
            }
        } catch (JSONException e) {
            AndroidUtil.show(TApplication.context, "json格式不正确");
            e.printStackTrace();
        }
    }


    private void handleError(VolleyError volleyError){
        NetworkResponse networkResponse = volleyError.networkResponse;
        if (networkResponse != null) {
            switch (networkResponse.statusCode) {
                case 408:
                    AndroidUtil.show(TApplication.context, "超时");
                    break;
                case 401:
                    break;
            }
        }
    }

}
