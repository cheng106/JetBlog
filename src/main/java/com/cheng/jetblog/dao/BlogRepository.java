package com.cheng.jetblog.dao;

import com.cheng.jetblog.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author cheng
 * @since 2021/8/23 22:14
 **/
public interface BlogRepository extends JpaRepository<Blog, Long>, JpaSpecificationExecutor<Blog> {

    @Query("select b from Blog b where b.isRecommend = true")
    List<Blog> findTop(Pageable pageable);

    // select * from t_blog where title like '%內容%'
    @Query("select b from Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views + 1 where b.id = ?1")
    int updateViews(Long id);

    /**
     * SELECT DATE_FORMAT(update_time, '%Y') AS year
     * FROM t_blog GROUP BY year ORDER BY year DESC;
     */
    @Query("select function('DATE_FORMAT', b.updateTime, '%Y') as year from Blog b " +
            "group by function('DATE_FORMAT', b.updateTime, '%Y') order by year desc ")
    List<String> findGroupYear();

    /**
     * SELECT * FROM t_blog  WHERE DATE_FORMAT(update_time, '%Y') = '2017';
     */
    @Query("select b from Blog b where function('DATE_FORMAT', b.updateTime, '%Y') = ?1")
    List<Blog> findByYear(String year);
}
