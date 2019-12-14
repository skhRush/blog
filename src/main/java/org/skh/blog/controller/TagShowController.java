package org.skh.blog.controller;

import org.skh.blog.entity.Blog;
import org.skh.blog.entity.BlogQuery;
import org.skh.blog.entity.Tag;
import org.skh.blog.listener.MyListener;
import org.skh.blog.service.BlogService;
import org.skh.blog.service.CommentService;
import org.skh.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @Created IntelliJ IDEA.
 * @Author L_SKH
 * @Date 2019/11/10 17:18
 */
@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/tags/{id}")
    public String Tags(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                       @PathVariable Long id,
                       Model model) {
        List<Tag> Tags = tagService.listTagTop(10000);
        if (id == -1) {
            id = Tags.get(0).getId();
        }
        Page<Blog> listBlog = blogService.listBlogByTag(id, pageable);
        List<Blog> content = listBlog.getContent();
        for (int i = 0; i < content.size(); i++) {
            content.get(i).setC_count(commentService.getBlogId(content.get(i).getId()));
        }
        model.addAttribute("online", MyListener.online);
        model.addAttribute("tags", Tags);
        model.addAttribute("page", blogService.listBlogByTag(id, pageable));
        model.addAttribute("activeTagId", id);

        return "tags";
    }
}
