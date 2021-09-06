package com.cheng.jetblog.service;

import com.cheng.jetblog.po.Comment;

import java.util.List;

/**
 * @author cheng
 * @since 2021/9/6 23:39
 **/
public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment);
}
