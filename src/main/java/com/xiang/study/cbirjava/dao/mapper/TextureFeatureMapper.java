package com.xiang.study.cbirjava.dao.mapper;

import com.xiang.study.cbirjava.dao.entity.TextureFeature;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 项健健
 * @date 2018/7/12 10:11
 * @description
 */
public interface TextureFeatureMapper {

    List<TextureFeature> listAll();
    
    TextureFeature selectOne();
    
    int batchInsert(@Param("list") List<TextureFeature> list);

}
