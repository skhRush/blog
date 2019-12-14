package org.skh.blog.service;

import org.skh.blog.entity.Blog;
import org.skh.blog.entity.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getBlog(Long id) ;
    Page<Blog> listBlog(Pageable pageable,BlogQuery blog);
    Page<Blog> listBlogByTag(Long tagId,Pageable pageable) ;
    Blog saveBlog(Blog blog) ;
    Blog updateBlog(Long id,Blog blog) ;
    void deleteBlog(Long id) ;
    List<Blog> listRecommendBlogTop(Integer size) ;
    Page<Blog> listBlog(Pageable pageable);
    Page<Blog> listBlogByQuery(String query,Pageable pageable);
    Blog getAndConvert(Long id) ;
    Map<String,List<Blog>> achiveBlog();
    Long CountBlog();
}
