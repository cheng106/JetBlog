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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

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
    public String list(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", categoryService.categoryList(pageable));
        return "admin/category-list";
    }

    @GetMapping("/category/input")
    public String addCategory(Model model) {
        model.addAttribute("category", new Category());
        return "admin/category-add";
    }

    @GetMapping("/category/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        Category c = categoryService.getCategory(id);
        model.addAttribute("category", c);
        return "admin/category-add";
    }

    @PostMapping("/categoryPost")
    public String postCategory(@Valid Category category, BindingResult result, RedirectAttributes attributes) {
        Category c1 = categoryService.getCategoryByName(category.getName());
        if (c1 != null) {
            result.rejectValue("name", "nameError", "該分類已存在");
        }
        if (result.hasErrors()) {
            return "admin/category-add";
        }

        Category c = categoryService.saveCategory(category);
        if (c == null) {
            attributes.addFlashAttribute("message", "新增失敗");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/categoryList";
    }

    @PostMapping("/category/{id}")
    public String editPostCategory(@Valid Category category, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Category c1 = categoryService.getCategoryByName(category.getName());
        if (c1 != null) {
            result.rejectValue("name", "nameError", "該分類已存在");
        }
        if (result.hasErrors()) {
            return "admin/category-add";
        }

        Category c = categoryService.updateCategory(id, category);
        if (c == null) {
            attributes.addFlashAttribute("message", "更新失敗");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/categoryList";
    }

    @GetMapping("/category/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        categoryService.deleteCategory(id);
        attributes.addFlashAttribute("message", "刪除成功");
        return "redirect:/admin/categoryList";
    }
}
