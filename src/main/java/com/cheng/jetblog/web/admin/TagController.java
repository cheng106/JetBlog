package com.cheng.jetblog.web.admin;

import com.cheng.jetblog.po.Tag;
import com.cheng.jetblog.service.TagService;
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
 * @since 2021/8/22 18:28
 **/
@Slf4j
@Controller
@RequestMapping("/admin")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String list(@PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable, Model model) {
        model.addAttribute("page", tagService.tagList(pageable));
        return "admin/tag-list";
    }

    @GetMapping("/tag/input")
    public String addTag(Model model) {
        model.addAttribute("tag", new Tag());
        return "admin/tag-add";
    }

    @GetMapping("/tag/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        Tag c = tagService.getTag(id);
        model.addAttribute("tag", c);
        return "admin/tag-add";
    }

    @PostMapping("/tagPost")
    public String postTag(@Valid Tag tag, BindingResult result, RedirectAttributes attributes) {
        Tag c1 = tagService.getTagByName(tag.getName());
        if (c1 != null) {
            result.rejectValue("name", "nameError", "該標籤已存在");
        }
        if (result.hasErrors()) {
            return "admin/tag-add";
        }

        Tag c = tagService.saveTag(tag);
        if (c == null) {
            attributes.addFlashAttribute("message", "新增失敗");
        } else {
            attributes.addFlashAttribute("message", "新增成功");
        }
        return "redirect:/admin/tags";
    }

    @PostMapping("/tag/{id}")
    public String editPostCategory(@Valid Tag tag, BindingResult result, @PathVariable Long id, RedirectAttributes attributes) {
        Tag t1 = tagService.getTagByName(tag.getName());
        if (t1 != null) {
            result.rejectValue("name", "nameError", "該標籤已存在");
        }
        if (result.hasErrors()) {
            return "admin/tag-add";
        }

        Tag c = tagService.updateTag(id, tag);
        if (c == null) {
            attributes.addFlashAttribute("message", "更新失敗");
        } else {
            attributes.addFlashAttribute("message", "更新成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("/tag/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes) {
        tagService.deleteTag(id);
        attributes.addFlashAttribute("message", "刪除成功");
        return "redirect:/admin/tags";
    }
}
