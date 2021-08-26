package com.cheng.jetblog.service;

import com.cheng.jetblog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author cheng
 * @since 2021/8/22 18:29
 **/
public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> tagList(Pageable pageable);

    List<Tag> findAll();

    List<Tag> findAllByIds(String ids);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);
}
