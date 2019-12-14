package org.skh.blog.dao;

import org.skh.blog.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Created IntelliJ IDEA.
 * @Author L_SKH
 * @Date 2019/11/10 9:57
 */
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort) ;

    int countByBlogId(Long id) ;
}
