package com.cheng.jetblog.web;

import com.cheng.jetblog.service.PlannerService;
import com.cheng.jetblog.vo.ProjectData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Lists;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

import static com.cheng.jetblog.utils.HttpUtils.readCookieStore;

/**
 * @author cheng
 * @since 2022/7/5 22:30
 **/
@Slf4j
@Controller
public class PlanerController {

    @Autowired
    private PlannerService plannerService;

    @PostMapping("formData")
    public String formData(@RequestBody String data) {
        log.info("raw-data:{}", data);
        try {
            CookieStore cookie = readCookieStore("markcheng00806");
            List<Cookie> cookies = cookie.getCookies();
            for (Cookie c : cookies) {
                System.out.println("c.name = " + c.getName() + ", c.value = " + c.getValue());
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String[] splitData = data.split("&");
        List<String> stringList = Arrays.asList(splitData);

        int group = splitData.length / 3;
        List<List<String>> partition = Lists.partition(stringList, group);

        List<String> firstFilter = new ArrayList<>();
        int count = partition.get(0).size();
        for (int i = 0; i < count; i++) {
            for (List<String> list : partition) {
                String s = list.get(i);
                firstFilter.add(s);
            }
        }

        List<List<String>> result = Lists.partition(firstFilter, 3);
        log.info("filter params data:{}", result);

        for (List<String> list : result) {
            StringBuilder r = new StringBuilder();
            r.append(String.format("personId=248544&workingDate=%s%s", LocalDate.now(), "&"));
            try {
                for (String s : list) {
                    String decode = URLDecoder.decode(s, StandardCharsets.UTF_8.name());
                    r.append(decode).append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            r.append("action=UPDATE_TIME&submit=更新&rowcount=3");

            ProjectData projectData = new ProjectData().setParams(r.toString());
            try {
                sendReq(projectData);
            } catch (IOException | ClassNotFoundException | URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return "planner-schedule";
    }

    @GetMapping("planner")
    public String goPlanPage() {
        log.info("GO~ Planner page");
        return "planner";
    }

    @PostMapping("plan/login")
    public ModelAndView planLogin(@RequestParam String username,
                                  @RequestParam String password, Model model) throws IOException, ClassNotFoundException, URISyntaxException {
        boolean login = plannerService.login(username, password);
        if (!login) {
            log.error("ERROR");
            model.addAttribute("msg", "error");
        } else {
            model.addAttribute("msg", "success");
        }
        ModelAndView mv = new ModelAndView("planner-job");
        String url = "http://xplanner.intumit.com/xplanner/do/edit/nonProjectTime";
        CookieStore cookie = readCookieStore("planner");
        HttpClient newHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookie).build();
        List<NameValuePair> loginNV = new ArrayList<>();
        LocalDate date = LocalDate.now();
        loginNV.add(new BasicNameValuePair("workingDate", date.toString()));
        URI uri = new URIBuilder(url).addParameters(loginNV).build();
        HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(uri).build();
        HttpResponse execute = newHttpClient.execute(httpUriRequest, HttpClientContext.create());

        String s = EntityUtils.toString(execute.getEntity());
        Document html = Jsoup.parse(s);

        Element select = html.getElementsByAttributeValueStarting("name", "nonProjectId[0]").get(0);
        Elements option = select.getElementsByTag("option");
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Element e : option) {
            result.put(e.attr("value"), e.text());
        }

        mv.addObject("projectMap", result);
        return mv;
    }


    // TEST
    @GetMapping("job")
    public ModelAndView goJob() throws IOException, ClassNotFoundException, URISyntaxException {
        ModelAndView mv = new ModelAndView("planner-job");
        String url = "http://xplanner.intumit.com/xplanner/do/edit/nonProjectTime";
        CookieStore cookie = readCookieStore("planner");
        HttpClient newHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookie).build();
        List<NameValuePair> loginNV = new ArrayList<>();
        LocalDate date = LocalDate.now();
        loginNV.add(new BasicNameValuePair("workingDate", date.toString()));
        URI uri = new URIBuilder(url).addParameters(loginNV).build();
        HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(uri).build();
        HttpResponse execute = newHttpClient.execute(httpUriRequest, HttpClientContext.create());

        String s = EntityUtils.toString(execute.getEntity());
        Document html = Jsoup.parse(s);

        Element select = html.getElementsByAttributeValueStarting("name", "nonProjectId[0]").get(0);
        Elements option = select.getElementsByTag("option");
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Element e : option) {
            result.put(e.attr("value"), e.text());
        }

        mv.addObject("projectMap", result);
        return mv;
    }


    //TEST
    @SneakyThrows
    @GetMapping("daily/work/{date}")
    @ResponseBody
    public Object getDailyWork(@PathVariable String date) {
        String url = "http://xplanner.intumit.com/xplanner/do/edit/nonProjectTime";
        CookieStore cookie = readCookieStore("planner");
        HttpClient newHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookie).build();
        List<NameValuePair> loginNV = new ArrayList<>();
        loginNV.add(new BasicNameValuePair("workingDate", date));
        URI uri = new URIBuilder(url).addParameters(loginNV).build();
        HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(uri).build();
        HttpResponse execute = newHttpClient.execute(httpUriRequest, HttpClientContext.create());

        String s = EntityUtils.toString(execute.getEntity());
        Document html = Jsoup.parse(s);
        Elements elementsByAttributeValueStarting = html.getElementsByAttributeValueStarting("name", "description");
        for (Element element : elementsByAttributeValueStarting) {
            log.info("element:{}", element.attr("value"));
        }

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("result", s);
        return objectNode;
    }

    //TEST
    @SneakyThrows
    @GetMapping("project/list/{date}")
    @ResponseBody
    public Object getProjectList(@PathVariable String date) {
        String url = "http://xplanner.intumit.com/xplanner/do/edit/nonProjectTime";
        CookieStore cookie = readCookieStore("planner");
        HttpClient newHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookie).build();
        List<NameValuePair> loginNV = new ArrayList<>();
        loginNV.add(new BasicNameValuePair("workingDate", date));
        URI uri = new URIBuilder(url).addParameters(loginNV).build();
        HttpUriRequest httpUriRequest = RequestBuilder.get().setUri(uri).build();
        HttpResponse execute = newHttpClient.execute(httpUriRequest, HttpClientContext.create());

        String s = EntityUtils.toString(execute.getEntity());
        Document html = Jsoup.parse(s);

        Element select = html.getElementsByAttributeValueStarting("name", "nonProjectId[0]").get(0);
        Elements option = select.getElementsByTag("option");
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        for (Element e : option) {
            result.put(e.attr("value"), e.text());
        }

//        stringList.forEach(log::info);
        return result;
    }

    public static void sendReq(ProjectData data) throws IOException, ClassNotFoundException, URISyntaxException {
        log.info("data.getParams={}", data.getParams());
        String url = "http://xplanner.intumit.com/xplanner/do/edit/nonProjectTime";
//        CookieStore cookie = readCookieStore("markcheng00806");
//        HttpClient newHttpClient = HttpClientBuilder.create().setDefaultCookieStore(cookie).build();
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        httpPost.setHeader("Cookie", "password=bWFya2NoZW5nMDA4MDY=; userid=markcheng00806; JSESSIONID=2A6D15E193B1948CAF7DCD5CEA25A8B7");
        StringEntity entity = new StringEntity(data.getParams(), StandardCharsets.UTF_8);
        httpPost.setEntity(entity);
        CloseableHttpResponse response = client.execute(httpPost);
//        HttpUriRequest httpUriRequest = RequestBuilder.post().setUri(url).setEntity(entity).build();
//        HttpResponse execute = httpPost.execute(httpUriRequest, HttpClientContext.create());

        int statusCode = response.getStatusLine().getStatusCode();
        log.info("statusCode:{}", statusCode);

    }

}
