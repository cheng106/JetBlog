package com.cheng.jetblog.web;

import com.cheng.jetblog.service.BlogService;
import com.cheng.jetblog.service.CategoryService;
import com.cheng.jetblog.service.TagService;
import com.cheng.jetblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author cheng
 * @since 2021/8/16 21:52
 **/
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    @GetMapping("/")
    public String index(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                Pageable pageable, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable));
        model.addAttribute("categoryList", categoryService.listCategoryTop(6));
        model.addAttribute("tags", tagService.listTagTop(10));
        model.addAttribute("recommend", blogService.ListRecommendBlogTop(8));
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
