package com.cheng.jetblog.service.impl;

import com.cheng.jetblog.service.PlannerService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static com.cheng.jetblog.utils.HttpUtils.readCookieStore;
import static com.cheng.jetblog.utils.HttpUtils.saveCookieStore;

/**
 * @author cheng
 * @since 2022/7/20 23:54
 **/
@Slf4j
@Service
public class PlannerServiceImpl implements PlannerService {

    @Override
    public boolean login(String userId, String password) {
        // 請求的地址
        String url = "http://xplanner.intumit.com/xplanner/do/login";
        // 請求參數
        try {
            List<NameValuePair> loginNV = new ArrayList<>();
            loginNV.add(new BasicNameValuePair("userId", userId));
            loginNV.add(new BasicNameValuePair("password", password));
            loginNV.add(new BasicNameValuePair("remember", "Y"));
            loginNV.add(new BasicNameValuePair("action", "登入"));
            // 請求資源地址
            URI uri = new URIBuilder(url).addParameters(loginNV).build();
            // 建立一個HttpContext對象，用來儲存Cookie
            HttpClientContext httpClientContext = HttpClientContext.create();
            // 自定義Header訊息
            List<Header> headerList = Lists.newArrayList();
            headerList.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9," +
                    "image/webp,image/apng,*/*;q=0.8"));
            headerList.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"));
            headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
            headerList.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age=0"));
            headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
            headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2,de;q=0.2"));
            // 自定義的HttpClient對象
            HttpClient httpClient = HttpClients.custom().setDefaultHeaders(headerList).build();
            // 請求對象
            HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(uri).build();
            // 執行請求，傳入HttpContext，將會得到請求結果的訊息
            httpClient.execute(httpUriRequest, httpClientContext);
            // 取得請求結果中Cookie，此時的Cookie已經帶有登入訊息了
            CookieStore cookieStore = httpClientContext.getCookieStore();
            // 這個CookieStore儲存了我們的登入訊息，我們可以先將它儲存到某個地方，後面直接讀取使用
            saveCookieStore(cookieStore, userId);
            // 使用Cookie來請求，首先讀取之前的Cookie
            CookieStore cookieStore1 = readCookieStore(userId);
            // 一個帶這個Cookie的HttpClient
            HttpClient newHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore1).build();
            // 使用新的HttpClient請求。此時HttpClient已經帶有了之前的登入訊息，再爬取就不用登入了
            newHttpClient.execute(httpUriRequest, httpClientContext);
        } catch (Exception e) {
            log.error("ERR:{}", e.getMessage(), e);
            return false;
        }
        return true;
    }
}
