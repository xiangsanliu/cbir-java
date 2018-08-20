package com.xiang.study.cbirjava.dao.mapper;

import com.xiang.study.cbirjava.dao.entity.ColorFeature;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 项健健
 * @date 2018/7/11 9:06
 * @description
 */
public interface ColorFeatureMapper {

    ColorFeature selectOne(@Param("id") long id);

    List<ColorFeature> listAll();

    List<String> listPath();

    int batchInsert(@Param("list") List<ColorFeature> list);

}
