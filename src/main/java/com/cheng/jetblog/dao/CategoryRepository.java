package com.cheng.jetblog.dao;

import com.cheng.jetblog.po.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cheng
 * @since 2021/8/19 22:06
 **/
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
