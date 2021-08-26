package com.cheng.jetblog.web.admin;

import com.cheng.jetblog.po.Blog;
import com.cheng.jetblog.po.User;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * @author cheng
 * @since 2021/8/19 20:37
 **/
@Controller
@RequestMapping("/admin")
public class JetBlogController {

    private static final String Blog_Add = "admin/blog-add";
    private static final String Blog_List = "admin/blogs";
    private static final String Redirect_List = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("categoryList", categoryService.getAll());
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return Blog_List;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC)
                                 Pageable pageable, BlogQuery blog, Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/add")
    public String blogAdd(Model model) {
        model.addAttribute("categoryList", categoryService.getAll());
        model.addAttribute("tags", tagService.findAll());
        model.addAttribute("blog", new Blog());
        return Blog_Add;
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setCategory(categoryService.getCategory(blog.getCategory().getId()));
        blog.setTags(tagService.findAllByIds(blog.getTagIds()));

        Blog b = blogService.saveBlog(blog);
        if (b == null) {
            attributes.addFlashAttribute("message", "操作失敗");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }

        return Redirect_List;
    }
}
