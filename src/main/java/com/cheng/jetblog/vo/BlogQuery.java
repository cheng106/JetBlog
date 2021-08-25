package com.cheng.jetblog.vo;

import lombok.Data;

/**
 * @author cheng
 * @since 2021/8/25 23:54
 **/
@Data
public class BlogQuery {
    private String title;
    private Long categoryId;
    private boolean recommend;
}
