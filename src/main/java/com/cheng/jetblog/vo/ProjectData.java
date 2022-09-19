package com.cheng.jetblog.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author cheng
 * @since 2022/8/16 23:23
 **/
@Data
@Accessors(chain = true)
public class ProjectData {
    private String personId;
    private String workingDate;
    private String params;
}
