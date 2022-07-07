package com.cheng.jetblog.po;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author cheng
 * @since 2021/8/17 21:29
 **/
@Getter
@Setter
@Entity
@Table(name = "t_blog")
public class Blog {

    @Id
    @GeneratedValue
    private Long id;
    private String title;

    //    @Basic(fetch = FetchType.LAZY)
//    @Lob
    private String content;
    private String firstPicture;
    private String flag;
    private Integer views;
    private boolean isAppreciation;
    private boolean isShareStatements;
    private boolean isCommentBoard;
    private boolean isPublished;
    private boolean isRecommend;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ManyToOne
    private Category category;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    private List<Tag> tags = new ArrayList<>();

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments = new ArrayList<>();

    @Transient
    private String tagIds;

    private String description;

    public boolean isRecommend() {
        return isRecommend;
    }

    public void setRecommend(boolean recommend) {
        isRecommend = recommend;
    }

    public boolean isPublished() {
        return isPublished;
    }

    public void setPublished(boolean published) {
        isPublished = published;
    }

    public void init() {
        tagIds = tagsToIds(getTags());
    }

    private String tagsToIds(List<Tag> tags) {
        return tags.stream().map(e -> String.valueOf(e.getId()))
                .collect(Collectors.joining(","));
    }

}
