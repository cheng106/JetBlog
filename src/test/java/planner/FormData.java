package planner;

import com.cheng.jetblog.vo.ProjectData;
import com.google.common.collect.Lists;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author cheng
 * @since 2022/8/16 22:23
 **/
public class FormData {

    public static void main(String[] args) {
//        String data = "projectCode=203&integer=1&workDesc=富邦例會";
        String data = "projectCode=100&projectCode=200&projectCode=300&projectCode=400&integer=1&integer=2&integer=3&integer=4&workDesc=aa&workDesc=bb&workDesc=cc&workDesc=dd";

        String[] splitData = data.split("&");
        // 將參數陣列放到List
        List<String> stringList = Arrays.asList(splitData);
        System.out.println("split = " + stringList);

        // 因為參數是三種，所以可以除三後取得共有幾組參數
        int group = splitData.length / 3;
        List<List<String>> partition = Lists.partition(stringList, group);

        // 分類後的多組參數
        System.out.println("partition = " + partition);

        List<String> firstFilter = new ArrayList<>();
        // 因為至少會有一組，所以取第一組看有多少個參數
        int count = partition.get(0).size();
        for (int i = 0; i < count; i++) {
            // 將不同種類的參數List取第i個後就放到篩選的List
            for (List<String> list : partition) {
                String s = list.get(i);
                firstFilter.add(s);
            }
        }
        System.out.println("firstFilter = " + firstFilter);

        // 篩選後的List再做一次分組，因為參數只有三種，所以除以三
        List<List<String>> result = Lists.partition(firstFilter, 3);

        // 分類後的總結果
        System.out.println("result = " + result);

        LocalDate today = LocalDate.now();
        System.out.println("today = " + today);

        for (List<String> list : result) {
            StringBuilder rs = new StringBuilder();
            for (String s : list) {
                rs.append(s).append("&");
            }
            System.out.println("rs = " + rs);
//            ProjectData projectData = new ProjectData().setPersonId("248544").setWorkingDate("2022-08-18");
//            try {
//                sendReq(projectData);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    public static void sendReq(ProjectData data) throws IOException {
//        String s = "personId=248544&workingDate=2022-08-20&nonProjectId[1]=203&duration[1]=0.5&description[1]=富邦例會&action=UPDATE_TIME&submit=更新&rowcount=3";
        System.out.println("data = " + data);
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://xplanner.intumit.com/xplanner/do/edit/nonProjectTime");
        httpPost.setHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        httpPost.setHeader("Cookie", "password=bWFya2NoZW5nMDA4MDY=; userid=markcheng00806; JSESSIONID=2A6D15E193B1948CAF7DCD5CEA25A8B7");
        StringEntity entity = new StringEntity(data.getParams(), StandardCharsets.UTF_8);
//        List<NameValuePair> params = new ArrayList<>();
//        params.add(new BasicNameValuePair("personId", data.getPersonId()));
//        params.add(new BasicNameValuePair("workingDate", data.getWorkingDate()));
//        params.add(new BasicNameValuePair("nonProjectId[1]", data.getProjectCode()));
//        params.add(new BasicNameValuePair("duration[1]", data.getHours()));
//        params.add(new BasicNameValuePair("description[1]", data.getWorkDesc()));
//        params.add(new BasicNameValuePair("action", "UPDATE_TIME"));
//        params.add(new BasicNameValuePair("submit", "更新"));
//        params.add(new BasicNameValuePair("rowcount", "3"));
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("statusCode = " + statusCode);
        client.close();


    }
}
