package com.cheng.jetblog.web.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cheng
 * @since 2021/8/19 20:37
 **/
@Controller
@RequestMapping("/admin")
public class JetBlogController {

    @GetMapping("/blogs")
    public String blogs() {
        return "admin/blogs";
    }
}
