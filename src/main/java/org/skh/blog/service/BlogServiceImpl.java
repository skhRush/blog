package org.skh.blog.service;

import jdk.nashorn.internal.ir.IfNode;
import org.skh.blog.dao.BlogRepository;
import org.skh.blog.entity.Blog;
import org.skh.blog.entity.BlogQuery;
import org.skh.blog.entity.Type;
import org.skh.blog.exceptiom.NotFoundException;
import org.skh.blog.utils.MarkDownUtils;
import org.skh.blog.utils.MyBeansUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogRepository blogRepository;


    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findById(id).orElse(null);
    }

    /**
     * 动态组合查询 使用JPA的API完成 没有拼接SQL语句
     * @param pageable
     * @param blog
     * @return
     */

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (!"".equals(blog.getTitle()) && blog.getTitle() != null) {
                    predicateList.add(criteriaBuilder.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if (blog.getTypeId()!=null){
                    predicateList.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blog.getTypeId())) ;
                }
                if (blog.isRecommend()){
                    predicateList.add(criteriaBuilder.equal(root.<Boolean>get("recommend"),blog.isRecommend())) ;

                }
                query.where(predicateList.toArray(new Predicate[predicateList.size()]));

                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlogByTag(Long tagId, Pageable pageable) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        },pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.findById(id).orElse(null);
        if (b == null) {
            throw new NotFoundException("该博客不存在！！！");
        }
        BeanUtils.copyProperties(blog,b,MyBeansUtils.getNullPropertyNames(blog));
        b.setUpdateTime(new Date());
        blogRepository.save(b);
        return blog;
    }

    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort orders = new Sort(Sort.Direction.DESC,"updateTime");
        Pageable pageable = new PageRequest(0,size,orders);

        return blogRepository.findTop(pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlogByQuery(String query, Pageable pageable) {
        return blogRepository.findQuery("%"+query+"%",pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if (blog == null){
            throw new NotFoundException("此博客不存在!!!") ;
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        b.setContent(MarkDownUtils.markdownToHtmlExtensions(blog.getContent()));
        blogRepository.updateViews(id); //更新浏览次数
        return b ;
    }

    @Override
    public Map<String, List<Blog>> achiveBlog() {
        List<String> groupYear = blogRepository.findGroupYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for (String year:groupYear){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long CountBlog() {
        return blogRepository.count();
    }


}
