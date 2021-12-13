package com.wx.wxdemo.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtil {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36";

    // 创建并配置HttpClient
    public static final CloseableHttpClient httpClient = HttpClients.custom().setUserAgent(USER_AGENT)
            .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build()).build();

    /************************************* 基本请求 *************************************/
    /**
     * 基本GET请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static String httpGet(String url, Map<String, String> headers) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }
            response = httpClient.execute(httpGet);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 基本POST请求
     *
     * @param url
     * @param params
     * @param headers
     * @return
     */
    public static String httpPost(String url, Map<String, String> params, Map<String, String> headers) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpPost httpPost = new HttpPost(url);

            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }

            if (params != null && params.size() > 0) {
                List<NameValuePair> nvps = new ArrayList<>();

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }

                UrlEncodedFormEntity formEntity = null;
                try {
                    formEntity = new UrlEncodedFormEntity(nvps, "utf-8");
                } catch (UnsupportedEncodingException e1) {
                }
                httpPost.setEntity(formEntity);
            }
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 基本DELETE请求
     *
     * @param url
     * @param headers
     * @return
     */
    public static String httpDelete(String url, Map<String, String> headers) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpDelete httpDelete = new HttpDelete(url);
            if (null != headers && !headers.isEmpty()) {
                for (Map.Entry<String, String> oEntry : headers.entrySet()) {
                    httpDelete.setHeader(oEntry.getKey(), oEntry.getValue());
                }
            }
            response = httpClient.execute(httpDelete);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 基本PUT请求
     *
     * @param url
     * @param params
     * @return
     */
    protected static String httpPut(String url, Map<String, String> params) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpPut oHttpPut = new HttpPut(url);
            if (params != null && params.size() > 0) {
                List<NameValuePair> nvps = new ArrayList<NameValuePair>();
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                UrlEncodedFormEntity formEntity = null;
                try {
                    formEntity = new UrlEncodedFormEntity(nvps);
                } catch (UnsupportedEncodingException e1) {
                }
                oHttpPut.setEntity(formEntity);
            }
            response = httpClient.execute(oHttpPut);
            result = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /********************************* 高级请求 *******************************************/
    public static String httpGetWithBody(String url, String jsonBody) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            HttpGetWithEntity httpget = new HttpGetWithEntity(url);
            HttpEntity httpEntity = new StringEntity(jsonBody);
            httpget.setEntity(httpEntity);
            httpget.addHeader("Content-Type", "application/json");
            StringEntity entity = new StringEntity(jsonBody, "utf-8");
            httpget.setEntity(entity);
            response = httpClient.execute(httpget);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static String httpPostWithBody(String url, JSONObject jsonBody) {
        CloseableHttpResponse oResponse = null;
        String sResult = null;
        try {
            HttpPost oHttpPost = new HttpPost(url);
            StringEntity oEntity = new StringEntity(jsonBody.toString(), "utf-8");//解决中文乱码问题
            oEntity.setContentEncoding("UTF-8");
            oEntity.setContentType("application/json");
            oHttpPost.setEntity(oEntity);
            oResponse = httpClient.execute(oHttpPost);
            sResult = EntityUtils.toString(oResponse.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oResponse != null) {
                try {
                    oResponse.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sResult;
    }

    public static String httpUpload(String url, InputStream inputStream, Map<String, String> headers) {
        String result = null;
        CloseableHttpResponse response = null;
        try {
            MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
            mEntityBuilder.addBinaryBody("file", inputStream);
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && headers.size() > 0) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpPost.setHeader(entry.getKey(), entry.getValue());
                }
            }
            httpPost.setEntity(mEntityBuilder.build());
            response = httpClient.execute(httpPost);
            result = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    public static InputStream httpDownload(String url) {
        CloseableHttpResponse response = null;
        InputStream in = null;
        try {
            HttpGet httpGet = new HttpGet(url);
            response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            in = entity.getContent();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return in;
    }
}

class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {
    public final static String METHOD_NAME = "GET";

    @Override
    public String getMethod() {
        return METHOD_NAME;
    }

    public HttpGetWithEntity(URI url) {
        this.setURI(url);
    }

    public HttpGetWithEntity(String uri) {
        this.setURI(URI.create(uri));
    }
}
