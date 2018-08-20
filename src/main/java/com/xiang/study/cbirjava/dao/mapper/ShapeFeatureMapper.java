package com.xiang.study.cbirjava.dao.mapper;

import com.xiang.study.cbirjava.dao.entity.ShapeFeature;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 项健健
 * @date 2018/7/12 15:20
 * @description
 */
public interface ShapeFeatureMapper {

    List<ShapeFeature> listAll();

    ShapeFeature selectOne();

    int batchInsert(@Param("list") List<ShapeFeature> list);

}
