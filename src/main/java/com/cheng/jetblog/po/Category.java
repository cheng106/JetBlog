package com.cheng.jetblog.po;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cheng
 * @since 2021/8/17 21:38
 **/
@Getter
@Setter
@Entity
@Table(name = "t_category")
public class Category {
    @Id
    @GeneratedValue
    private Long id;
    @NotBlank(message = "請輸入分類名稱")
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Blog> blogs = new ArrayList<>();
}
