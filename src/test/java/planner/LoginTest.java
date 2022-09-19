package planner;

import com.cheng.jetblog.utils.HttpUtils;
import com.cheng.jetblog.utils.JacksonUtils;
import com.cheng.jetblog.utils.dto.ApiInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * @author cheng
 * @since 2022/7/20 23:01
 **/
@SpringBootTest(classes = LoginTest.class)
public class LoginTest {

    @Value(ResourceUtils.CLASSPATH_URL_PREFIX + "cookie")
    private Resource resource;

    @Test
    public void testPlannerLogin() {
        String filePath = Objects.requireNonNull(getClass().getResource("/")).getPath();
        String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath();
        System.out.println("path = " + path);
        System.out.println("filePath = " + filePath);
        System.out.println("resource = " + resource);
        ObjectNode obj = (new ObjectMapper()).createObjectNode();
        obj.put("userId", "markcheng00806");
        obj.put("password", "markcheng00806");
        obj.put("remember", "Y");
        obj.put("action", "登入");

        try {
            HttpClientContext httpClientContext = HttpClientContext.create();
            ApiInfo info = ApiInfo.builder()
                    .url("http://xplanner.intumit.com/xplanner/do/login")
                    .paramsNode(obj)
                    .headers(new HashMap<String, String>() {{
                        put(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9," +
                                "image/webp,image/apng,*/*;q=0.8");
                        put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
                        put(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
                        put(HttpHeaders.CONNECTION, "keep-alive");
                        put(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2,de;q=0.2");
                        put(HttpHeaders.CACHE_CONTROL, "max-age=0");
                        put(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
                    }})
                    .build();

            // 自定義Header訊息
            List<Header> headerList = Lists.newArrayList();
            headerList.add(new BasicHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9," +
                    "image/webp,image/apng,*/*;q=0.8"));
            headerList.add(new BasicHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36"));
            headerList.add(new BasicHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate"));
            headerList.add(new BasicHeader(HttpHeaders.CACHE_CONTROL, "max-age=0"));
            headerList.add(new BasicHeader(HttpHeaders.CONNECTION, "keep-alive"));
            headerList.add(new BasicHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4,ja;q=0.2," +
                    "de;q=0.2"));
            String savePath = "src/main/resources/cookie/plannerCustom";
            // 自定義的HttpClient對象
            HttpClient httpClient = HttpClients.custom().setDefaultHeaders(headerList).build();
            // 請求對象
            HttpUriRequest httpUriRequest = RequestBuilder.get().setUri("http://xplanner.intumit.com/xplanner/do/login").build();
            // 執行請求，傳入HttpContext，將會得到請求結果的訊息
            httpClient.execute(httpUriRequest, httpClientContext);
            // 取得請求結果中Cookie，此時的Cookie已經帶有登入訊息了
            CookieStore cookieStore = httpClientContext.getCookieStore();
            // 這個CookieStore保存了我們的登入訊息，我們可以先將它保存到某個本地文件，後面直接讀取使用
            saveCookieStore(cookieStore, savePath);
            // 使用Cookie來請求，首先讀取之前的Cookie
            CookieStore cookieStore1 = readCookieStore(savePath);
            // 一個帶這個Cookie的HttpClient
            HttpClient newHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookieStore1).build();
            // 使用新的HttpClient請求。此時HttpClient已經帶有了之前的登入訊息，再爬取就不用登入了
            HttpResponse response = newHttpClient.execute(httpUriRequest, httpClientContext);
            System.out.println("response = " + response);
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
            System.out.println("entity = " + entity);
//            String result = HttpUtils.doPost();

//            ObjectNode jsonNodes = JacksonUtils.decodeObject(result);
//            System.out.println("jsonNodes = " + jsonNodes);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //使用序列化的方式保存CookieStore到本地文件，方便后续的读取使用
    private static void saveCookieStore(CookieStore cookieStore, String savePath) throws IOException {
        System.out.println("cookieStore = " + cookieStore.getCookies());
        FileOutputStream fs = new FileOutputStream(savePath);
        System.out.println("fs = " + fs);
        ObjectOutputStream os = new ObjectOutputStream(fs);
        os.writeObject(cookieStore);
        os.close();
    }

    //读取Cookie的序列化文件，读取后可以直接使用
    private static CookieStore readCookieStore(String savePath) throws IOException, ClassNotFoundException {
        FileInputStream fs = new FileInputStream(savePath);//("foo.ser");
        ObjectInputStream ois = new ObjectInputStream(fs);
        CookieStore cookieStore = (CookieStore) ois.readObject();
        ois.close();
        return cookieStore;

    }
}
