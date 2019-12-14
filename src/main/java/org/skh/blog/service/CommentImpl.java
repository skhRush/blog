package org.skh.blog.service;

import org.skh.blog.dao.CommentRepository;
import org.skh.blog.entity.Comment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.Id;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Created IntelliJ IDEA.
 * @Author L_SKH
 * @Date 2019/11/10 9:56
 */
@Service
public class CommentImpl implements CommentService{


    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();



    @Autowired
    private CommentRepository commentRepository ;

    @Override
    public List<Comment> listCommentByBlogId(Long id) {
        Sort sort = new Sort(Sort.Direction.ASC, "createtime");
        List<Comment> byBlogIdAndParentCommentNot = commentRepository.findByBlogIdAndParentCommentNull(id, sort);
        return eachComment(byBlogIdAndParentCommentNot) ;
    }


    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        Long parentCommentId = comment.getParentComment().getId();
        if (parentCommentId!=-1){
            comment.setParentComment(commentRepository.findById(parentCommentId).orElse(null));
        }else{
            //如果为-1 要为pc初始化一下 因为此时的comment并没有被实例化
            comment.setParentComment(null);
        }
        comment.setCreatetime(new Date());
        return commentRepository.save(comment);
    }

    @Override
    public int getBlogId(Long id) {
        return commentRepository.countByBlogId(id);
    }


    /**
     * 循环每个顶级的评论节点
     * @param comments
     * @return
     */
    private List<Comment> eachComment(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment,c);
            commentsView.add(c);
        }
        //合并评论的各层子代到第一级子代集合中
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     *
     * @param comments root根节点，blog不为空的对象集合
     * @return
     */
    private void combineChildren(List<Comment> comments) {

        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplayComments()  ;
            for(Comment reply1 : replys1) {
                //循环迭代，找出子代，存放在tempReplys中
                recursively(reply1);
            }
            //修改顶级节点的reply集合为迭代处理后的集合
            comment.setReplayComments(tempReplys);
            //清除临时存放区
            tempReplys = new ArrayList<>();
        }
    }

    /**
     * 递归迭代 将所有非顶级评论放入一个集合当中
     * @param comment 被迭代的对象
     * @return
     */
    private void recursively(Comment comment) {
        tempReplys.add(comment);//顶节点添加到临时存放集合
        if (comment.getReplayComments().size()>0) {
            List<Comment> replys = comment.getReplayComments() ;
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplayComments().size()>0) {
                    recursively(reply);
                }
            }
        }
    }

}
