package com.ccw.project.service;

import com.ccw.project.dao.CommentsMapper;
import com.ccw.project.dao.QuestionsMapper;
import com.ccw.project.dao.UserMapper;
import com.ccw.project.entities.Comments;
import com.ccw.project.entities.Questions;
import com.ccw.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainService {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private UserMapper userMapper;

    /**
     * Return all questions
     * @return
     */
    public List<Questions> getQuestions(){
        return questionsMapper.getQuestions();
    }

    /**
     * Return the question according to specific question ID
     * @param questionId
     * @return
     */
    public Questions getQuestion(int questionId){
        return questionsMapper.selectByPrimaryKey(questionId);
    }

    /**
     * Get the commments
     * @param q_id
     * @return
     */
    public List<Comments> getComments(int q_id){
        List<Comments> comments = commentsMapper.getComments(q_id);

        return comments;
    }

    /**
     * Get the author name by author ID
     * @param authorId
     * @return
     */
    public String getAuthorName(int authorId){
        return userMapper.selectByPrimaryKey(authorId).getUsername();
    }

    /**
     * Insert the new question to database
     * @param questions
     * @return
     */
    public int insertQuestion(Questions questions){
        return questionsMapper.insertSelective(questions);
    }

    /**
     * Insert the new comment
     * @param comments
     * @return
     */
    public int insertComments(Comments comments){
        return commentsMapper.insertSelective(comments);
    }

    /**
     * Add the rating for comments by 1
     * @param comments
     * @return
     */
    public int updateRating(Comments comments){
        return commentsMapper.updateByPrimaryKeySelective(comments);
    }

    /**
     * Update user profile
     * @param user
     * @return
     */
    public int updateUser(User user){
        return userMapper.updateByPrimaryKeySelective(user);
    }

    /**
     * Get the likyly keyword
     * @param questions
     * @return
     */
    public List<Questions> getLikelyQuestions(Questions questions){
        return questionsMapper.getLikelyQuestions(questions);
    }

}
