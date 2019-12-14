package org.skh.blog.controller;

import org.skh.blog.entity.Comment;
import org.skh.blog.entity.User;
import org.skh.blog.service.BlogService;
import org.skh.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @Created IntelliJ IDEA.
 * @Author L_SKH
 * @Date 2019/11/10 9:50
 */
@Controller
public class CommentController {
    @Autowired
    private CommentService commentService ;

    @Autowired
    private BlogService blogService ;

    //在配置文件中取值 所有用户默认使用相同的头像
    @Value("${comment.avatar}")
    private String avatar ;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog :: commentList" ;
    }
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session){
        User user = (User) session.getAttribute("user");
        Long blogId = comment.getBlog().getId();
        comment.setBlog(blogService.getBlog(blogId));
        if (user!=null){
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else{
            comment.setAvatar(avatar) ;
        }
        commentService.saveComment(comment) ;
        return "redirect:/comments/" + blogId;
    }




}
