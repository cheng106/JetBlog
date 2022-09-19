package com.cheng.jetblog.utils.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 * @since 2022/6/16 13:35
 **/
@Data
@Builder
public class ApiInfo {
    private String url;
    // 記錄user id
    private String uid;
    // 重試的起始次數，由0開始的話最多3次
    private Integer retryStartNum;
    // get parameters
    private Map<String, String> paramsMap;
    // post parameters
    private ObjectNode paramsNode;
    // request headers
    private Map<String, String> headers;
    // web service
    private String soapParam;
}
