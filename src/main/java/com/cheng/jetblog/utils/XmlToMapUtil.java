package com.cheng.jetblog.utils;

import lombok.experimental.UtilityClass;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 目前做的測試取得的值都正確，只要從map取要的標籤名稱即可
 *
 * @author cheng
 * @see <a href="https://stackoverflow.com/questions/38457460/parse-a-simple-xml-string">parse xml</a>
 * @since 2022/6/18 20:21
 **/
@UtilityClass
public class XmlToMapUtil {

    public static Map<String, String> parse(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException {
        DataCollector handler = new DataCollector();
        SAXParserFactory.newInstance().newSAXParser().parse(inputSource, handler);
        return handler.result;
    }

    private static class DataCollector extends DefaultHandler {
        private final StringBuilder bufferStr = new StringBuilder();
        private final Map<String, String> result = new HashMap<>();

        @Override
        public void endElement(String uri, String localName, String qName) {
            final String value = bufferStr.toString().trim();
            if (value.length() > 0) {
                result.put(qName, value);
            }
            bufferStr.setLength(0);
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            bufferStr.append(ch, start, length);
        }
    }

}
