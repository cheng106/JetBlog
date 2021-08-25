package com.cheng.jetblog.service;

import com.cheng.jetblog.po.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author cheng
 * @since 2021/8/19 21:18
 **/
public interface CategoryService {
    Category saveCategory(Category category);

    Category getCategory(Long id);

    Category getCategoryByName(String name);

    Page<Category> categoryList(Pageable pageable);

    List<Category> getAll();

    Category updateCategory(Long id, Category category);

    void deleteCategory(Long id);
}
