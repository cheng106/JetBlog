package com.cheng.jetblog.po;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cheng
 * @since 2021/8/17 21:38
 **/
@Data
@Entity
@Table(name = "t_category")
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Blog> blogs = new ArrayList<>();
}
