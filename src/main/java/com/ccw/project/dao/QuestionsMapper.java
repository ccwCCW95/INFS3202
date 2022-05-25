package com.ccw.project.dao;

import com.ccw.project.entities.Questions;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Questions record);

    int insertSelective(Questions record);

    Questions selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Questions record);

    int updateByPrimaryKey(Questions record);

    List<Questions> getQuestions();

    List<Questions> getLikelyQuestions(Questions questions);

    int updateViewsByQuestionId(Questions questions);

    int updatePinids(Questions questions);
}