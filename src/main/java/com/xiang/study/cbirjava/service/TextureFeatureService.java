package com.xiang.study.cbirjava.service;

import com.xiang.study.cbirjava.dao.entity.TextureFeature;
import com.xiang.study.cbirjava.dao.mapper.ColorFeatureMapper;
import com.xiang.study.cbirjava.dao.mapper.TextureFeatureMapper;
import com.xiang.study.cbirjava.opencv.TextureFeatureCalculator;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 项健健
 * @date 2018/7/12 9:59
 * @description
 */
@Service
public class TextureFeatureService implements BaseService {

    private static final Logger logger = LoggerFactory.getLogger(TextureFeatureService.class);

    @Resource
    private ColorFeatureMapper colorFeatureMapper;

    @Resource
    private TextureFeatureMapper textureFeatureMapper;

    @Override
    public void preProcess() {
        logger.info("Calculating - TextureFeature...");
        List<String> paths = colorFeatureMapper.listPath();
        List<TextureFeature> list = new LinkedList<>();
        paths.forEach(path -> {
            list.add(new TextureFeature(path, TextureFeatureCalculator.calculateFeature(path)));
        });
        logger.info("TextureFeature Calculation Complete!");
        textureFeatureMapper.batchInsert(list);
    }

    @Override
    public List<Pair<Double, String>> compare(String fileName) {
        logger.info("Search for texture ...");
        List<Pair<Double, String>> result = new LinkedList<>();
        List<TextureFeature> all = textureFeatureMapper.listAll();
        double[] uploadedFeature = TextureFeatureCalculator.calculateFeature(fileName);
        all.forEach(item -> {
            result.add(new Pair<>(TextureFeatureCalculator.calculateDistance(item, uploadedFeature), item.getPath()));
        });
        result.sort(Comparator.comparingDouble(Pair::getKey));
        return result.subList(0, 9);
    }

}
