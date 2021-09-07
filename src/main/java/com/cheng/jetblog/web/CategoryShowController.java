package com.cheng.jetblog.web;

import com.cheng.jetblog.po.Category;
import com.cheng.jetblog.service.BlogService;
import com.cheng.jetblog.service.CategoryService;
import com.cheng.jetblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author cheng
 * @since 2021/9/7 22:39
 **/
@Controller
public class CategoryShowController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BlogService blogService;

    @GetMapping("/category/{id}")
    public String category(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                   Pageable pageable, Model model, @PathVariable Long id) {
        List<Category> categoryList = categoryService.listCategoryTop(100);
        if (id == -1) {
            id = categoryList.get(0).getId();
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setCategoryId(id);
        model.addAttribute("categoryList", categoryList);
        model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("activeCategoryId", id);
        return "category";
    }

}
