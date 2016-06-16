package com.hxsn.ssk.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 *  Created by jiely on 2016/5/19.
 */
public class HttpUtil {

    /**
     * 以POST方式提交文件
     * param url
     * param param
     * param file
     * return
     * throws Exception
     */
    public static String doPostMapFile(String url, String pathKey,String path,Map<String,String> map)
            throws Exception {
        HttpPost post = new HttpPost(url);
        HttpClient client = new DefaultHttpClient();

        MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
        entityBuilder.setCharset(Charset.forName(HTTP.UTF_8));

        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.forName("UTF-8"));
        entity.addPart("content",new StringBody(url,Charset.forName("UTF-8")));
        DebugUtil.d("httpUtil", "doPostFile url=" + url);

        //MultipartEntity entity = new MultipartEntity();
        // 添加文件参数
        if (path != null) {

            File file = new File(path);
            if (file.exists()) {
                entity.addPart(pathKey, new FileBody(file));
            }

        }

        Set<String> keys = map.keySet();
        for (String key : keys) {
            String value = map.get(key);
            Log.i("HttpUtil", "value = " + value);
            StringBody stringBody = new StringBody(value);
            entity.addPart(key,stringBody);
        }

        post.setEntity(entity);

        HttpResponse response = client.execute(post);
        int stateCode = response.getStatusLine().getStatusCode();
        StringBuilder sb = new StringBuilder();
        if (stateCode == HttpStatus.SC_OK) {
            HttpEntity result = response.getEntity();
            if (result != null) {
                InputStream is = result.getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String tempLine;
                while ((tempLine = br.readLine()) != null) {
                    sb.append(tempLine);
                }
            }
        }
        post.abort();
        DebugUtil.d("httpUtils", "doPostFile jsonString=" + sb.toString());
        return sb.toString();
    }


    public static String post(String url, String pathKey,String path,Map<String, String> datas)  {
        HttpPost post = new HttpPost(url);
        // 初始化客户端请求
        //this.initHttpClient();
        Iterator iterator = datas.entrySet().iterator();

        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        multipartEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        multipartEntityBuilder.setCharset(Charset.forName(HTTP.UTF_8));
        // 发送的数据
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue(), ContentType.create("text/plain", Charset.forName(HTTP.UTF_8)));
        }

        File file = new File(path);
        multipartEntityBuilder.addBinaryBody(pathKey, file);

        // 生成 HTTP 实体
        HttpEntity httpEntity = multipartEntityBuilder.build();

        // 设置 POST 请求的实体部分
        post.setEntity(httpEntity);
        // 发送 HTTP 请求
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        StringBuilder sb = new StringBuilder();
        try {
            response = client.execute(post);
            int stateCode = response.getStatusLine().getStatusCode();

            if (stateCode == HttpStatus.SC_OK) {
                HttpEntity result = response.getEntity();
                if (result != null) {
                    InputStream is = result.getContent();
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String tempLine;
                    while ((tempLine = br.readLine()) != null) {
                        sb.append(tempLine);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        post.abort();

        DebugUtil.d("httpUtils", "doPostFile jsonString=" + sb.toString());
        return sb.toString();
    }
}
