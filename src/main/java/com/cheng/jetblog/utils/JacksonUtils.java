package com.cheng.jetblog.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author cheng
 * @since 2022/6/13 10:25
 **/
@Slf4j
public class JacksonUtils {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public static ObjectNode getObject(){
        return MAPPER.createObjectNode();
    }

    /**
     * 序列化
     */
    public static <T> String encode(T obj) {
        if (Objects.isNull(obj)) {
            return null;
        }
        try {
            return MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("json encode error, obj={}", obj, e);
            return null;
        }
    }

    /**
     * 反序列化
     */
    public static <T> T decode(String json, Class<T> valueType) {
        if (!StringUtils.isBlank(json) && !Objects.isNull(valueType)) {
            try {
                return MAPPER.readValue(json, valueType);
            } catch (Exception e) {
                log.error("json decode fail,jsonString={}, type={}", json, valueType.getName(), e);
            }
        }
        return null;
    }

    public static ObjectNode decodeObject(String json) throws JsonProcessingException {
        return (ObjectNode) MAPPER.readTree(json);
    }

    /**
     * 反序列化成list
     *
     * @param <T>
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> decode2List(String json, Class<T> clazz) {
        if (!StringUtils.isBlank(json) && !Objects.isNull(clazz)) {
            try {
                return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            } catch (Exception e) {
                log.error("json decode2list fail,json={},classType={}", json, clazz.getName(), e);
            }
        }
        return null;
    }

    /**
     * map轉class物件
     *
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T mapToObject(Map<String, String> map, Class<T> clazz) {
        String json = null;
        try {
            json = MAPPER.writeValueAsString(map);
            return MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("json map2object fail,map={},ClassName={}", json, clazz.getName());
            return null;
        }
    }

    /**
     * 將JSONObject解析成map
     *
     * @param prefix key名稱前綴
     * @param obj    JSONObject物件
     * @param map
     */
    @Deprecated
    public static void decodeJSONObject(String prefix, Object obj, Map<String, Object> map) {
//        if (obj instanceof JSONObject) {
//            JSONObject json = (JSONObject) obj;
//            for (String key : ((JSONObject) obj).keySet()) {
//                Object o = json.get(key);
//                String pre = String.join(".", prefix, key);
//                if (!(o instanceof JSONObject) && !(o instanceof JSONArray)) {
//                    map.put(pre, o);
//                } else {
//                    decodeJSONObject(pre, o, map);
//                }
//            }
//        } else if (obj instanceof JSONArray) {
//            for (Object ob : (JSONArray) obj) {
//                if (!(ob instanceof JSONObject) && !(ob instanceof JSONArray)) {
//                    map.put(prefix, ob);
//                } else {
//                    decodeJSONObject(prefix, ob, map);
//                }
//            }
    }

}
