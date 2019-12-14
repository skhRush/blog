package org.skh.blog.service;

import org.skh.blog.dao.TypeRepository;
import org.skh.blog.entity.Type;
import org.skh.blog.exceptiom.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository ;

    @Transactional
    @Override
    public Type addType(Type type) {
        return typeRepository.save(type) ;
    }

    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findById(id).orElse(null) ;
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        //分页查询
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.findById(id).orElse(null) ;
        if (t==null){
            throw new NotFoundException("不存在该类型！！！");
        }
        BeanUtils.copyProperties(type,t);

        return typeRepository.save(t);
    }

    @Transactional
    @Override
    public void deleteType(Long id) {
       typeRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeRepository.getTypeByName(name);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = new PageRequest(0, size, sort);
        return typeRepository.findTop(pageable) ;
    }
}
