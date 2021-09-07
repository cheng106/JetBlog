package com.cheng.jetblog.dao;

import com.cheng.jetblog.po.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author cheng
 * @since 2021/9/6 23:41
 **/
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

}
