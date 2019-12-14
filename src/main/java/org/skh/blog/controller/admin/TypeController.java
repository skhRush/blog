package org.skh.blog.controller.admin;

import org.skh.blog.entity.Type;
import org.skh.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Id;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {


    @Autowired
    private TypeService typeService ;

    @GetMapping("/types")
    public String listType(@PageableDefault(size = 4,
            sort = {"id"},
            direction = Sort.Direction.DESC) Pageable pageable,
                           Model model) {
        model.addAttribute("page",typeService.listType(pageable)) ; //Page<Type>
        return "admin/types" ;
    }

    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type()) ; //方便thymeleaf接收
        return "admin/types-input" ;
    }

    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id)) ;
        return "/admin/types-input" ;
    }
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult bindingResult,RedirectAttributes redirectAttributes){
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName !=null){
            //自定义错误消息
            bindingResult.rejectValue("name","nameError","不能重复添加分类");

        }
        if (bindingResult.hasErrors()){
            return "/admin/types-input" ;
        }
        Type type1 = typeService.addType(type);
        if (type1 ==null){
            redirectAttributes.addFlashAttribute("message","新增失败") ;
        }else{
            redirectAttributes.addFlashAttribute("message","新增成功") ;
        }
        return "redirect:/admin/types" ; //重定向
    }
    @PostMapping("/types/{id}")
    public String post(@Valid Type type, BindingResult bindingResult,@PathVariable Long id, RedirectAttributes redirectAttributes){
        Type typeByName = typeService.getTypeByName(type.getName());
        if (typeByName !=null){
            //自定义错误消息
            bindingResult.rejectValue("name","nameError","不能重复添加分类");

        }
        if (bindingResult.hasErrors()){
            return "/admin/types-input" ;
        }
        Type type1 = typeService.updateType(id,type) ;
        if (type1 ==null){
            redirectAttributes.addFlashAttribute("message","更新失败") ;
        }else{
            redirectAttributes.addFlashAttribute("message","更新成功") ;
        }
        return "redirect:/admin/types" ; //重定向
    }

    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable  Long id,RedirectAttributes redirectAttributes){
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功") ;
        return "redirect:/admin/types" ;

    }
}
