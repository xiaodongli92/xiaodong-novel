package com.xiaodong.novel.service.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description: http公用方法
 * @Author: xiaodongli92@gmail.com
 * @CreateDate: 2016/6/7
 */
public class HttpUtil {

    private static final int MAX_TOTAL = 100;
    private static final int DEFAULT_MAX_PER_ROUTE = 200;
    private static final int CONNECT_TIME_OUT = 60000;
    private static final int SOCKET_TIME_OUT = 60000;

    private static CloseableHttpClient httpClient;

    static {
        PoolingHttpClientConnectionManager http = new PoolingHttpClientConnectionManager();
        http.setMaxTotal(MAX_TOTAL);
        http.setDefaultMaxPerRoute(DEFAULT_MAX_PER_ROUTE);
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIME_OUT)
                .setSocketTimeout(SOCKET_TIME_OUT).build();
        httpClient = HttpClients.custom().setDefaultRequestConfig(requestConfig)
                .setConnectionManager(http).build();
    }

    private HttpUtil(){

    }

    /**
     * post json 请求
     */
    public static String postJson(final String url, final String jsonStr) throws IOException {
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-type", "application/json");
        HttpEntity httpEntity = new StringEntity(jsonStr, "UTF-8");
        post.setEntity(httpEntity);
        HttpResponse response = httpClient.execute(post);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * post 请求
     */
    public static String post(final String url, final Map<String,String> paramMap) throws IOException {
        HttpPost post = new HttpPost(url);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        if (null != paramMap && !paramMap.isEmpty()) {
            List<NameValuePair> params = getNameValuePair(paramMap);
            post.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));
        }
        HttpResponse response = httpClient.execute(post);
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * get 请求
     * 参数传递
     */
    public static String get(final String url, final Map<String, String> paramMap) {
        String queryUrl = url + buildQuery(paramMap);
        return get(queryUrl);
    }

    /**
     * get 请求
     */
    public static String get(String url){
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpClient.execute(get);
            return EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 参数转换
     * Map<String,String> ==》List<NameValuePair>
     */
    private static List<NameValuePair> getNameValuePair(final Map<String,String> paramMap) {
        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String,String> entry:paramMap.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
        }
        return params;
    }

    private static String buildQuery(final Map<String,String> params) {
        if (null == params || params.isEmpty()) {
            return "";
        }
        StringBuilder query = new StringBuilder();
        boolean hasParam = false;
        Set<Map.Entry<String,String>> entries = params.entrySet();
        for (Map.Entry<String,String> entry:entries) {
            String name = entry.getKey();
            String value = entry.getValue();
            if (StringUtils.isNoneBlank(name, value)) {
                if (hasParam) {
                    query.append("&");
                } else {
                    hasParam = true;
                }
                query.append(name).append("=").append(value);
            }
        }
        return query.toString();
    }
}
