package com.cheng.jetblog.web;

import com.cheng.jetblog.exception.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author cheng
 * @since 2021/8/16 21:52
 **/
@Controller
public class IndexController {

    @GetMapping("/{id}/{name}")
    public String index(@PathVariable Integer id, @PathVariable String name) {
        System.out.println("--------index--------");
//        String blog = null;
//        if (blog == null) {
//            throw new NotFoundException("Blog not found !!!");
//        }
        return "index";
    }
}
