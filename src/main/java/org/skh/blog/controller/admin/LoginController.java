package org.skh.blog.controller.admin;

import org.skh.blog.entity.User;
import org.skh.blog.service.UserService;
import org.skh.blog.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String login() {
        return "admin/login";
    }

    @PostMapping("/login")
    public String login1(@RequestParam("username") String username,
                         @RequestParam("password") String password,
                         HttpSession session,
                         RedirectAttributes attributes) {
        //采用Md5加密机制
        User user = userService.checkUser(username, MD5Utils.code(password));
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名和密码错误");

            return "redirect:/admin"; //重定向转发
        }

    }
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        session.invalidate();
        return "redirect:/admin";
    }
}
