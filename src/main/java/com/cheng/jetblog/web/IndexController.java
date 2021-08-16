package com.cheng.jetblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author cheng
 * @since 2021/8/16 21:52
 **/
@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        System.out.println("--------index--------");
//        String blog = null;
//        if (blog == null) {
//            throw new NotFoundException("Blog not found !!!");
//        }
        return "index";
    }

    @GetMapping("/blog")
    public String blog() {
        System.out.println("--------blog--------");
//        String blog = null;
//        if (blog == null) {
//            throw new NotFoundException("Blog not found !!!");
//        }
        return "blog";
    }
}
