package com.cheng.jetblog.service.impl;

import com.cheng.jetblog.dao.TagRepository;
import com.cheng.jetblog.po.Tag;
import com.cheng.jetblog.service.TagService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * @author cheng
 * @since 2021/8/22 18:29
 **/
@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagRepository tagRepository;

    @Override
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Tag getTagByName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public Page<Tag> tagList(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Tag updateTag(Long id, Tag tag) {
        Tag t = tagRepository.getOne(id);
        BeanUtils.copyProperties(tag, t);
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }
}
