package com.meituan.qa.util;

import com.alibaba.fastjson.JSONObject;

import org.apache.http.*;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.springframework.stereotype.Service;

import java.io.IOException;

public class HttpClientUtil {

    private static final String CLIENT_ID = "crmtask";
    private static final String CLIENT_SECRET = "a360ef981d5a4157a0dfd29273838e40";
    private static final String SSO_URL = "http://ssosv.it.beta.sankuai.com/sson/api/auth";
    private static final String COOKIE_DOMAIN = "octo.sankuai.com";


    /**
     * get请求
     * @param
     * @return
     */
    public static JSONObject doGet(String url) {

        BasicCookieStore cookieStore = new BasicCookieStore();

        HttpClient ssoHttpClient = HttpClientBuilder.create().build();
        try {
            JSONObject postData = new JSONObject();
            postData.put("loginName", "zhangxin49");
            postData.put("password", "Mtzx9879");

            HttpPost httpPost = new HttpPost(SSO_URL);
            httpPost.setHeader("Content-Type", "application/json");
            String charSet = "UTF-8";
            StringEntity entity = new StringEntity(postData.toString(), charSet);
            httpPost.setEntity(entity);

            BAAuthUtil.authHttpRequest(httpPost, CLIENT_ID, CLIENT_SECRET);
            HttpResponse ssoResponse = ssoHttpClient.execute(httpPost);
            String ssoStr = EntityUtils.toString(ssoResponse.getEntity());
//            JSONObject ssoJson = JSONObject.parseObject(ssoStr);
//            String ssoid = JSONObject.parseObject(ssoJson.get("data").toString()).getString("accessToken");

            String ssoid = "316f5bcb28*84cb5841b21a5b5425865";
            BasicClientCookie cookie = new BasicClientCookie("msgp_ssoid", ssoid);
            cookie.setDomain(COOKIE_DOMAIN);
            cookie.setPath("/");
            cookieStore.addCookie(cookie);


            HttpClient httpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
            RequestBuilder requestBuilder = RequestBuilder.get().setUri(url);
            HttpUriRequest request = requestBuilder.build();

            HttpResponse response = httpClient.execute(request);

            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject jsonResult = JSONObject.parseObject(strResult);
                return jsonResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * post请求
     * @param
     * @return
     */
    public static JSONObject doPost(String url, String params) {

        HttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        httpPost.setEntity(entity);

        BAAuthUtil.authHttpRequest(httpPost, CLIENT_ID, CLIENT_SECRET);
        HttpResponse response = null;

        try {
            response = httpClient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String strResult = EntityUtils.toString(responseEntity);
                JSONObject jsonResult = JSONObject.parseObject(strResult);
                return jsonResult;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
