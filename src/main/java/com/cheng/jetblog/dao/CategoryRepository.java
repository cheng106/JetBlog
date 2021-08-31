package com.cheng.jetblog.dao;

import com.cheng.jetblog.po.Category;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author cheng
 * @since 2021/8/19 22:06
 **/
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByName(String name);

    @Query("select c from Category c")
    List<Category> findTop(Pageable pageable);
}
