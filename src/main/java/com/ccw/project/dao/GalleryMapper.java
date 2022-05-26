package com.ccw.project.dao;

import com.ccw.project.entities.Gallery;
import org.springframework.stereotype.Repository;

@Repository
public interface GalleryMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Gallery record);

    int insertSelective(Gallery record);

    Gallery selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Gallery record);

    int updateByPrimaryKey(Gallery record);

    int checkUserExist(Integer userId);

    int updateByUserId(Gallery gallery);

    Gallery getUserGallery(Integer userId);
}