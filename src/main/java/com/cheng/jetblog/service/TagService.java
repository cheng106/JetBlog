package com.cheng.jetblog.service;

import com.cheng.jetblog.po.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author cheng
 * @since 2021/8/22 18:29
 **/
public interface TagService {
    Tag saveTag(Tag tag);

    Tag getTag(Long id);

    Tag getTagByName(String name);

    Page<Tag> tagList(Pageable pageable);

    Tag updateTag(Long id, Tag tag);

    void deleteTag(Long id);
}
