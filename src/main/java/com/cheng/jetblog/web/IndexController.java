package com.cheng.jetblog.web;

import com.cheng.jetblog.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author cheng
 * @since 2021/8/16 21:52
 **/
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        String blog = null;
        if (blog == null) {
            throw new NotFoundException("Blog not found !!!");
        }
        return "index";
    }
}
