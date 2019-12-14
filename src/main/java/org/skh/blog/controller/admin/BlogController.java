package org.skh.blog.controller.admin;

import org.skh.blog.entity.Blog;
import org.skh.blog.entity.BlogQuery;
import org.skh.blog.entity.User;
import org.skh.blog.service.BlogService;
import org.skh.blog.service.TagService;
import org.skh.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;


    @GetMapping("/blogs")
    public String selectAllBlog(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                                BlogQuery blog,
                                Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        model.addAttribute("types", typeService.listType());

        return "admin/blogs";
    }


    @PostMapping("/blogs/search")
    public String selectAll(@PageableDefault(size = 5, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
                            BlogQuery blog,
                            Model model) {
        model.addAttribute("page", blogService.listBlog(pageable, blog));

        return "admin/blogs :: blogList";  //类似于Ajax只向前端页面的blogList页面传递数据 也就是只刷新表格 起到ajax的作用
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
        model.addAttribute("blog", new Blog());
        return "admin/blogs-input";
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());

    }

    @GetMapping("/blogs/{id}/input")  //修改
    public String editInput(@PathVariable Long id,Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "admin/blogs-input";
    }

    @PostMapping("/blogs")
    public String post(Blog blog,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        blog.setType(typeService.getType(blog.getType().getId()));

        Blog b ;
        if (blog.getId()==null){
            b = blogService.saveBlog(blog) ;
        }else{
            b = blogService.updateBlog(blog.getId(),blog) ;
        }
        if (b == null) {
            redirectAttributes.addFlashAttribute("message", "操作失败");
        }else{
            redirectAttributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/admin/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功") ;
        return "redirect:/admin/blogs" ;
    }
}
