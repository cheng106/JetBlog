package com.fubon.test;

import com.cheng.jetblog.utils.HttpUtils;
import com.cheng.jetblog.utils.XmlToMapUtil;
import com.cheng.jetblog.utils.dto.ApiInfo;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

/**
 * @author cheng
 * @since 2022/8/29 16:08
 **/
public class WebServiceTest {

    public static void main(String[] args) {
        String soapStr = soapParams(2000, 1222);
        String url = "http://www.dneonline.com/calculator.asmx?op=Add";
        try {
            String s = HttpUtils.doPostByXml(ApiInfo.builder()
                    .url(url)
                    .soapParam(soapStr).build());

            Map<String, String> parse = XmlToMapUtil.parse(new InputSource(new StringReader(s)));
            String addResult = parse.get("AddResult");
            System.out.println("addResult = " + addResult);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }

    }

    public static String soapParams(int intA, int intB) {
        return "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Body>\n" +
                "    <Add xmlns=\"http://tempuri.org/\">\n" +
                "      <intA>" + intA + "</intA>\n" +
                "      <intB>" + intB + "</intB>\n" +
                "    </Add>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
    }
}
