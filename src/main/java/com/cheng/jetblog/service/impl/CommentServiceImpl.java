package com.cheng.jetblog.service.impl;

import com.cheng.jetblog.dao.CommentRepository;
import com.cheng.jetblog.po.Comment;
import com.cheng.jetblog.service.CommentService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author cheng
 * @since 2021/9/6 23:40
 **/
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    // 存放迴圈找出的所有子節點的List
    private List<Comment> tempReplyList = new ArrayList<>();

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachComment(comments);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();

        if (parentCommentId != -1) {
            comment.setParentComment(commentRepository.getOne(parentCommentId));
        } else {
            comment.setParentComment(null);
        }

        comment.setCreateTime(new Date());
        return commentRepository.save(comment);
    }


    /**
     * 循環每個第一層的留言節點
     *
     * @param comments
     * @return void
     **/
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        comments.forEach(e -> {
            Comment c = new Comment();
            BeanUtils.copyProperties(e, c);
            commentsView.add(c);
        });
        // 合併留言的各層子節點到第一級集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * root節點，blog不是空的物件List
     *
     * @param comments
     * @return void
     **/
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replyList = comment.getReplyComments();
            for (Comment reply : replyList) {
                // 跑迴圈找出子節點放到tempReplyList
                recursively(reply);
            }
            // 修改最上層節點的reply List為迴圈處理後的List
            comment.setReplyComments(tempReplyList);
            tempReplyList = new ArrayList<>();
        }
    }

    /**
     * 遞迴
     *
     * @param comment
     * @return void
     **/
    private void recursively(Comment comment) {
        // 最上層節點新增到臨時存放的List
        tempReplyList.add(comment);
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replyList = comment.getReplyComments();
            for (Comment reply : replyList) {
                tempReplyList.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }


}
