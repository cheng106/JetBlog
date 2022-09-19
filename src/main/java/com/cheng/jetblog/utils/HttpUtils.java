package com.cheng.jetblog.utils;

import com.cheng.jetblog.utils.dto.ApiInfo;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpRequest;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author cheng
 * @since 2022/6/16 13:31
 **/
@Slf4j
@UtilityClass
public class HttpUtils {
    private static final PoolingHttpClientConnectionManager clientConnectionManager;

    static {
        SSLContext sslContext = SSLContexts.createDefault();
        log.info("sslContext:{}", sslContext.getProtocol());
        SSLConnectionSocketFactory sslFactory = new SSLConnectionSocketFactory(sslContext,
                NoopHostnameVerifier.INSTANCE);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", sslFactory)
                .build();
        clientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        clientConnectionManager.setMaxTotal(100);
        // 設定每個主機最大連線數
        clientConnectionManager.setDefaultMaxPerRoute(10);
    }

    /**
     * 建立連線
     *
     * @return org.apache.http.impl.client.CloseableHttpClient
     **/
    private static CloseableHttpClient createClient() {
        return HttpClients.custom().setConnectionManager(clientConnectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setSocketTimeout(5 * TimeUnit.SECONDS.ordinal())
                        .setConnectTimeout(3 * TimeUnit.SECONDS.ordinal())
                        .setConnectionRequestTimeout(5 * TimeUnit.SECONDS.ordinal())
                        .build()
                ).build();
    }

    public static String doGet(ApiInfo apiInfo) throws IOException {
        String url = apiInfo.getUrl();
        log.info("api url:{}", url);
        CloseableHttpClient client = createClient();
        Map<String, String> paramsMap = apiInfo.getParamsMap();
        log.info("paramsMap:{}", paramsMap);

        String query = encodingParams(paramsMap);
        query = (StringUtils.isBlank(query) ? "" : "?") + query;
        log.info("##query:{}", query);
        url += query;

        HttpGet httpGet = new HttpGet(url);
        lineApiSetHeader(httpGet);

        return getHttpResult(client, httpGet);
    }

    /**
     * 通用POST
     *
     * @param apiInfo 要連線的資訊
     * @return java.lang.String
     **/
    public static String doPost(ApiInfo apiInfo) throws IOException {
        CloseableHttpClient client = createClient();
        StringEntity entity = new StringEntity(apiInfo.getParamsNode().toString(),
                StandardCharsets.UTF_8);
        entity.setContentType(MediaType.APPLICATION_JSON_VALUE);

        HttpPost httpPost = new HttpPost(apiInfo.getUrl());
        log.info("[POST] ===> {}", httpPost.getURI());

        lineApiSetHeader(httpPost);
        Map<String, String> headers = apiInfo.getHeaders();
        if (headers != null) {
            headers.forEach(httpPost::setHeader);
        }
        httpPost.setEntity(entity);
        return getHttpResult(client, httpPost);
    }

    /**
     * 使用XML傳輸
     *
     * @param apiInfo 要連線的資訊
     * @return java.lang.String
     **/
    public static String doPostByXml(ApiInfo apiInfo) throws IOException {
        String url = apiInfo.getUrl();
        CloseableHttpClient client = url.startsWith("http") ? HttpClients.createDefault() : createClient();
        
        HttpPost requestPost = new HttpPost(url);
        StringEntity entity = new StringEntity(apiInfo.getSoapParam(), StandardCharsets.UTF_8);
        entity.setContentType(MediaType.TEXT_XML_VALUE);
        requestPost.setEntity(entity);

        return getHttpResult(client, requestPost);
    }

    /**
     * 返回結果
     *
     * @param client  CloseableHttpClient
     * @param request Http request
     * @return java.lang.String
     **/
    private static String getHttpResult(CloseableHttpClient client, HttpUriRequest request)
            throws IOException {
        try (CloseableHttpResponse response = client.execute(request)) {
        client.close();
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("getHttpResult ERR:{}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * add line default header
     */
    private static void lineApiSetHeader(HttpRequest req) {
        String channelToken = "";
        req.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        req.setHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        req.setHeader(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", channelToken));

    }

    /**
     * 使用UTF-8將參數編碼
     *
     * @param params 參數
     * @return 編碼後字串
     **/
    private static String encodingParams(Map<String, ?> params) {
        if (params == null) {
            return "";
        }
        return params.entrySet().stream()
                .map(e -> {
                    try {
                        return String.format("%s=%s", e.getKey(),
                                URLEncoder.encode(e.getValue().toString(),
                                        StandardCharsets.UTF_8.name()));
                    } catch (UnsupportedEncodingException unsupportedEncodingException) {
                        log.warn("###url encode error ===> {}:{}", e.getKey(), e.getValue());
                        return null;
                    } catch (Exception ex) {
                        log.error("ERROR:", ex);
                        return null;
                    }
                })
                .filter(StringUtils::isNoneBlank)
                .collect(Collectors.joining("&"));
    }

    /**
     * 使用序列化的方式保存CookieStore到本地文件，方便後續的讀取使用
     *
     * @param cookieStore CookieStore
     * @param savePath    儲存路徑
     * @throws IOException io exception
     **/
    public static void saveCookieStore(CookieStore cookieStore, String savePath) throws IOException {
        try (FileOutputStream fs = new FileOutputStream(savePath);
             ObjectOutputStream os = new ObjectOutputStream(fs)) {
            os.writeObject(cookieStore);
        }
    }

    /**
     * 讀取Cookie的序列化文件，讀取後可以直接使用
     *
     * @param savePath 儲存路徑
     * @throws IOException io exception
     **/
    public static CookieStore readCookieStore(String savePath) throws IOException, ClassNotFoundException {
        try (FileInputStream fs = new FileInputStream(savePath);
             ObjectInputStream ois = new ObjectInputStream(fs)) {
            return (CookieStore) ois.readObject();
        }
    }
}
