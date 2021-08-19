package com.cheng.jetblog.web.admin;

import com.cheng.jetblog.po.Category;
import com.cheng.jetblog.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author cheng
 * @since 2021/8/19 22:16
 **/
@Slf4j
@Controller
@RequestMapping("/admin")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/categoryList")
    public String list(@PageableDefault(size = 3, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", categoryService.categoryList(pageable));
        return "admin/category-list";
    }

    @GetMapping("/category/input")
    public String addCategory() {
        return "admin/category-add";
    }

    @PostMapping("/categoryPost")
    public String postCategory(Category category) {
        Category c = categoryService.saveCategory(category);
        if (c == null) {
        } else {
            String[] s = new String[]{"1", "2"};
            System.out.println(Stream.of(s).map("TEST-"::concat));

        }
        return "redirect:/admin/categoryList";
    }
}
