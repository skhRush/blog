package org.skh.blog.service;

import org.skh.blog.entity.Comment;

import java.util.List;

/**
 * @Created IntelliJ IDEA.
 * @Author L_SKH
 * @Date 2019/11/10 9:55
 */
public interface CommentService {
    List<Comment> listCommentByBlogId(Long id) ;
    Comment saveComment(Comment comment) ;
    int getBlogId(Long id);
}
