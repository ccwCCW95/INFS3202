package com.ccw.project.service;

import com.ccw.project.dao.CommentsMapper;
import com.ccw.project.dao.GalleryMapper;
import com.ccw.project.dao.QuestionsMapper;
import com.ccw.project.dao.UserMapper;
import com.ccw.project.entities.Comments;
import com.ccw.project.entities.Gallery;
import com.ccw.project.entities.Questions;
import com.ccw.project.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

    @Autowired
    private QuestionsMapper questionsMapper;

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GalleryMapper galleryMapper;

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

    /**
     * Update the view number of specific question
     * @param questionId
     * @param newViews
     * @return
     */
    public boolean updateQusViews(int questionId, int newViews){
        Questions questions = new Questions();
        questions.setId(questionId);
        questions.setViews(newViews);

        int num = questionsMapper.updateViewsByQuestionId(questions);

        if (num == 1){
            return true;
        }

        return false;
    }

    /**
     * Update the pinIds
     * @param questionId
     * @param pinids
     * @return
     */
    public boolean updatePinids(int questionId, String pinids){
        Questions questions = new Questions();
        questions.setId(questionId);
        questions.setPinId(pinids);

        int num = questionsMapper.updatePinids(questions);

        if (num == 1){
            return true;
        }

        return false;
    }

    /**
     * Get the questionList after pin
     * @param returnQuestions
     * @param userId
     * @return
     */
    public List<Questions> getAfPinQuestions(List<Questions> returnQuestions, int userId){
        // Get the new question list
        List<Questions> newList = new ArrayList<Questions>();

        for (Questions tempQuestion : returnQuestions) {
            String tempPin = tempQuestion.getPinId();

            if(tempPin == null){
                continue;
            }

            String[] temp = tempPin.split(",");

            for(int i = 0; i <= temp.length - 1; i++){
                if (temp[i].equals(String.valueOf(userId))){
                    newList.add(tempQuestion);
                }
            }
        }

        for (Questions tempQuestion : returnQuestions) {
            if (!newList.contains(tempQuestion)){
                newList.add(tempQuestion);
            }
        }

        return newList;
    }

    /**
     * Check if this question is pinned by this user
     * @param questions
     * @param userId
     * @return
     */
    public boolean checkIfPin(Questions questions, int userId){
        String pinIds = questions.getPinId();

        if(pinIds == null){
            pinIds = "";
        }

        String[] temp = pinIds.split(",");

        for(int i = 0; i <= temp.length - 1; i++){
            if (temp[i].equals(String.valueOf(userId))){
                return true;
            }
        }

        return false;
    }

    /**
     * Update the img info for user
     * @param user
     * @return
     */
    public boolean updateImg(User user){
        int num = userMapper.updateImgById(user);

        if (num == 1){
            return true;
        }

        return false;
    }

    /**
     * Check if the user is exist in the gallery
     * @param userId
     * @return
     */
    public boolean checkUserinGallery(int userId){
        int num = galleryMapper.checkUserExist(userId);

        if (num == 1){
            return true;
        }

        return false;
    }

    /**
     * Update the gallery by userId
     * @param gallery
     * @return
     */
    public boolean updateGallery(Gallery gallery){
        int num = galleryMapper.updateByUserId(gallery);

        if (num == 1){
            return true;
        }

        return false;
    }

    /**
     * Insert the images into gallery
     * @param gallery
     * @return
     */
    public boolean insertGallery(Gallery gallery){
        int num = galleryMapper.insertSelective(gallery);

        if (num == 1){
            return true;
        }

        return false;
    }

    /**
     * Return the user's gallery
     * @param userId
     * @return
     */
    public Gallery getUserGallery(int userId){
        Gallery gallery = galleryMapper.getUserGallery(userId);

        return gallery;
    }

    /**
     * Return all users except manager
     * @return
     */
    public List<User> getAllUsers(){
        return userMapper.selectAllUsers();
    }

    /**
     * Delete the user
     * @param userId
     * @return
     */
    public boolean deleteUser(int userId){
        int num = userMapper.deleteUser(userId);

        if (num == 1){
            return true;
        }

        return false;
    }
}
