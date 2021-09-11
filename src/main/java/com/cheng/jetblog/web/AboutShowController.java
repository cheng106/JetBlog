package com.cheng.jetblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author cheng
 * @since 2021/9/11 23:26
 **/
@Controller
public class AboutShowController {

    @GetMapping("/about")
    public String about() {
        return "about";
    }

}
