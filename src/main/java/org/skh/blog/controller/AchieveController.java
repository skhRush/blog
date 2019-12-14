package org.skh.blog.controller;

import org.skh.blog.listener.MyListener;
import org.skh.blog.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Created IntelliJ IDEA.
 * @Author L_SKH
 * @Date 2019/11/11 14:20
 */
@Controller
public class AchieveController {
    @Autowired
    private BlogService blogService ;
    @GetMapping("/archives")
    public String achives(Model model){
        model.addAttribute("online",MyListener.online) ;
        model.addAttribute("archiveMap",blogService.achiveBlog());
        model.addAttribute("blogCount",blogService.CountBlog());
        return "archives" ;
    }
}
