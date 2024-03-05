package com.common.core.utils;

import cn.hutool.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http请求客户端工具类
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-01 16:06:46
 * @Version: v1.0.0
*/
public class HttpClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * Get 请求
     * @param url 请求地址
     * @return
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * Post 请求
     * @param url 请求地址
     * @return
     */
    public static String doPost(String url) {
        return doPost(url, null);
    }

    /**
     * 向目的 URL 发送 GET 请求
     * @param url 请求地址
     * @return
     */
    public static String sendGetRequest(String url) {
        return sendGetRequest(url, null);
    }

    /**
     * 向目的URL发送POST请求
     * @param url 请求地址
     * @return
     */
    public static Map<String, Object> sendPostRequest(String url) {
        return sendPostRequest(url, null);
    }

    /**
     * 向目的 URL 发送 GET 请求
     * @param url 请求地址
     * @param params 请求参数
     * @return
     */
    public static String sendGetRequest(String url, Object params) {
        RestTemplate client = new RestTemplate();
        // 新建Request请求头 add方法可以添加参数
        HttpHeaders httpHeaders = new HttpHeaders();
        HttpMethod method = HttpMethod.GET;
        // 以JSON的方式提交
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //将请求头部和参数合成一个请求
        HttpEntity<Object> requestEntity = new HttpEntity<>(params, httpHeaders);
        //执行HTTP请求，将返回的结构使用String 类格式化
        ResponseEntity<String> response = client.exchange(url, method, requestEntity, String.class);
        return response.getBody();
    }

    /**
     * 向目的URL发送POST请求
     *
     * @param url 目的url
     * @param jsonParam 发送的参数
     * @return
     */
    public static Map<String, Object> sendPostRequest(String url, String jsonParam){
        RestTemplate client = new RestTemplate();
        //新建Http头，add方法可以添加参数
        HttpHeaders httpHeaders = new HttpHeaders();
        //设置请求发送方式
        HttpMethod method = HttpMethod.POST;
        // 以表单的方式提交
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        //将请求头部和参数合成一个请求
        HttpEntity<String> requestBody = new HttpEntity<>(jsonParam, httpHeaders);
        //执行HTTP请求，将返回的结构使用Map 类格式化（可设置为对应返回值格式的类）
        ResponseEntity<Map> response = client.exchange(url, method, requestBody, Map.class);
        return response.getBody();
    }

    /**
     * Get 请求
     * @param url 请求地址
     * @param param 请求参数
     * @return
     */
    public static String doGet(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();
            // 创建http GET请求 设置请求地址
            HttpGet httpGet = new HttpGet(uri);
            // 执行请求
            response = httpclient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.OK.value()) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttp(httpclient, response);
        }
        return resultString;
    }

    /**
     * Post 请求
     * @param url 请求地址
     * @param param 请求参数 formDate格式
     * @return
     */
    public static String doPost(String url, Map<String, String> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeHttp(httpClient, response);
        }

        return resultString;
    }

    /**
     * Post 请求
     * @param url 请求地址
     * @param json 请求参数 json格式
     * @return
     */
    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           closeHttp(httpClient, response);
        }
        return resultString;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     */
    public static String sendPost(String url, String paramUrl) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            JSONObject param = new JSONObject(paramUrl);
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            LOGGER.error("发送POST请求出现异常：{}", e);
            e.printStackTrace();
        } finally {
            // 使用finally块来关闭输出流、输入流
            closeHttp(out, in);
        }
        return result;
    }

    private static void closeHttp(CloseableHttpClient httpClient, CloseableHttpResponse response) {
        closeHttp(httpClient, response, null, null);
    }

    private static void closeHttp(PrintWriter out, BufferedReader in) {
        closeHttp(null, null, out, in);
    }

    private static void closeHttp(CloseableHttpClient httpClient, CloseableHttpResponse response, PrintWriter out, BufferedReader in) {
        try {
            if (response != null) {
                response.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
