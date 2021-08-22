package com.cheng.jetblog.dao;

import com.cheng.jetblog.po.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author cheng
 * @since 2021/8/22 18:30
 **/
public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
