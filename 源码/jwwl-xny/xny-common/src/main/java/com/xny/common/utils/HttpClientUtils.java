package com.xny.common.utils;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpClientUtils {

    /**
     * URL编码指定字符集
     *
     * @param url      资源地址定位
     * @param urlParam url?后的参数
     * @param header
     * @param charSet  URL编码使用字符集,例如:UTF-8
     * @param ssl
     * @return
     */
    public static String get(String url, Map<String, String> urlParam, Map<String, String> header, String charSet, boolean ssl) {
        CloseableHttpClient httpClient = ssl ? getHttpClient() : HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(charSet == null ? addParams2Url(url, urlParam) : addParams2UrlWithCharSet(url, urlParam, charSet));
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000 * 2).setConnectionRequestTimeout(6000 * 2).setSocketTimeout(6000 * 2).build();
        httpGet.setConfig(requestConfig);

        try {
            httpGet.setHeaders(createHeader(header));
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) return null;
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            return res;
        } catch (Exception e) {

        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {

            }
        }
        return null;
    }

    /**œ
     * URL编码指定字符集
     * @param url 资源地址定位
     * @return String
     */
    public static String get(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(addParams2Url(url, null));
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000 * 2).setConnectionRequestTimeout(6000 * 2).setSocketTimeout(6000 * 2).build();
        httpGet.setConfig(requestConfig);
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) return null;
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            return res;
        } catch (Exception e) {
            e.getMessage();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return null;
    }

    private static String addParams2Url(String url, Map<String, String> params) {
        return addParams2UrlWithCharSet(url, params, null);
    }

    private static String addParams2UrlWithCharSet(String url, Map<String, String> params, String charSet) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder sb = new StringBuilder();
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                sb.append("&").append(entry.getKey()).append("=");
                sb.append(charSet == null ? entry.getValue() : URLEncoder.encode(entry.getValue(), charSet));
            }
            if (!url.contains("?")) {
                sb.deleteCharAt(0).insert(0, "?");
            }
        } catch (Exception e) {

        }
        return url + sb;
    }

    /**
     * POST 以请求体JSON发送数据
     *
     * @param url
     * @param urlParam
     * @param header
     * @param data
     * @param ssl
     * @return
     */
    public static String postJson(String url, Map<String, String> urlParam, Map<String, String> header, String data, boolean ssl) throws Exception {
        CloseableHttpClient httpClient = ssl ? getHttpClient() : HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(addParams2Url(url, urlParam));
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000 * 2).setConnectionRequestTimeout(6000 * 2).setSocketTimeout(6000 * 2).build();
        httpPost.setConfig(requestConfig);
        try {
            httpPost.setHeaders(createHeader(header));
            httpPost.setEntity(new StringEntity(data, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) return null;
            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            response.close();
            return res;
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * POST 以表单的形式发送数据
     *
     * @param url
     * @param urlParam
     * @param header
     * @param data
     * @param ssl
     * @return
     */
    public static String postForm(String url, Map<String, String> urlParam, Map<String, String> header, Map<String, String> data, boolean ssl) {
        CloseableHttpClient httpClient = ssl ? getHttpClient() : HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(addParams2Url(url, urlParam));
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(6000 * 2).setConnectionRequestTimeout(6000 * 2).setSocketTimeout(6000 * 2).build();
        httpPost.setConfig(requestConfig);

        try {
            // config header content-type is application/x-www-form-urlencoded : 配置HTTP的Header:content-type
            header = header != null ? header : new HashMap<>();
            ContentType APPLICATION_FORM_URLENCODED_UTF8 = ContentType.create("application/x-www-form-urlencoded", Consts.UTF_8);
            header.put("Content-Type", APPLICATION_FORM_URLENCODED_UTF8.toString());
            httpPost.setHeaders(createHeader(header));

            // add key-value pair to entity : 设置参数到请求体中
            List<NameValuePair> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                list.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            if (list.size() > 0) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, "UTF-8");
                httpPost.setEntity(entity);
            }

            CloseableHttpResponse response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) return null;

            HttpEntity entity = response.getEntity();
            String res = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            response.close();
            return res;
        } catch (Exception e) {

        } finally {
            try {
                httpClient.close();
            } catch (Exception e) {

            }
        }
        return null;
    }


    /**
     * HTTPS client 创建
     *
     * @return
     */
    public static CloseableHttpClient getHttpClient() {
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, new TrustManager[]{trustManager}, null);
            return HttpClients.custom().setSSLSocketFactory(new SSLConnectionSocketFactory(context)).build();
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 批量创建请求头
     *
     * @param header
     * @return
     */
    private static Header[] createHeader(Map<String, String> header) {
        if (header == null || header.isEmpty()) return null;
        List<Header> headers = new ArrayList<>();
        for (String key : header.keySet()) {
            headers.add(new BasicHeader(key, header.get(key)));
        }
        return headers.toArray(new Header[]{});
    }

}