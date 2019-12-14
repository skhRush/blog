package org.skh.blog.controller;

import org.skh.blog.entity.Blog;
import org.skh.blog.listener.MyListener;
import org.skh.blog.service.BlogService;
import org.skh.blog.service.CommentService;
import org.skh.blog.service.TagService;
import org.skh.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {


    @Autowired
    private BlogService blogService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private CommentService commentService;

    @RequestMapping("/")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model,
                        HttpServletRequest request) {
        HttpSession session;

        List<Integer> c_count = new ArrayList<>();
        if (request.getSession(false) == null) {
            session = request.getSession();
        } else {
            session = request.getSession(false);
        }
        session.setAttribute("name", "游客");
        session.setMaxInactiveInterval(10 * 60);
        Page<Blog> listBlog = blogService.listBlog(pageable);
        List<Blog> content = listBlog.getContent();
        for (int i=0;i<content.size();i++){
            content.get(i).setC_count(commentService.getBlogId(content.get(i).getId()));
        }
        model.addAttribute("online", MyListener.online);
        model.addAttribute("page", listBlog);
        model.addAttribute("types", typeService.listTypeTop(5));
        model.addAttribute("tags", tagService.listTagTop(5));
        model.addAttribute("recommendBlogs", blogService.listRecommendBlogTop(5));


        return "index";
    }

    @PostMapping("/search")
    public String index(@PageableDefault(size = 8, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                        Model model,
                        @RequestParam("query") String query) {
        model.addAttribute("online", MyListener.online);
        Page<Blog> listBlog = blogService.listBlogByQuery(query, pageable) ;
        List<Blog> content = listBlog.getContent();
        for (int i=0;i<content.size();i++){
            content.get(i).setC_count(commentService.getBlogId(content.get(i).getId()));
        }
        model.addAttribute("page", blogService.listBlogByQuery(query, pageable));
        model.addAttribute("query", query);
        return "search";
    }

    @GetMapping("/blog/{id}")
    public String getBlogMsg(@PathVariable Long id,
                             Model model, HttpSession session) {
        model.addAttribute("online", MyListener.online);
        model.addAttribute("blog", blogService.getAndConvert(id));
        return "blog";
    }

    @GetMapping("/about")
    public String getMe(Model model) {
        model.addAttribute("online", MyListener.online);
        return "about";
    }
}
