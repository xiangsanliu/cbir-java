package com.xiang.study.cbirjava.service;

import com.xiang.study.cbirjava.dao.entity.ShapeFeature;
import com.xiang.study.cbirjava.dao.mapper.ColorFeatureMapper;
import com.xiang.study.cbirjava.dao.mapper.ShapeFeatureMapper;
import com.xiang.study.cbirjava.opencv.ShapeFeatureCalculator;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 项健健
 * @date 2018/7/12 11:37
 * @description
 */
@Service
public class ShapeFeatureService implements BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ShapeFeatureService.class);

    @Resource
    private ColorFeatureMapper colorFeatureMapper;

    @Resource
    private ShapeFeatureMapper shapeFeatureMapper;

    @Override
    public void preProcess() {
        logger.info("Calculating - TextureFeature...");
        List<String> paths = colorFeatureMapper.listPath();
        int index = 0;
        int len = paths.size();
        while (index + 50 < len) {
            List<ShapeFeature> list = new LinkedList<>();
            paths.subList(index, index + 50).forEach(path -> {
                logger.info("Calculating - {}", path);
                list.add(new ShapeFeature(path, ShapeFeatureCalculator.calculateFeature(path)));
            });
            shapeFeatureMapper.batchInsert(list);
            index += 50;
        }
        List<ShapeFeature> list = new LinkedList<>();
        paths.subList(7600, paths.size()).forEach(path -> {
            logger.info("Calculating - {}", path);
            list.add(new ShapeFeature(path, ShapeFeatureCalculator.calculateFeature(path)));
        });
        shapeFeatureMapper.batchInsert(list);
        logger.info("TextureFeature Calculation Complete!");
    }

    @Override
    public List<Pair<Double, String>> compare(String fileName) {
        logger.info("Searching for shap ...");
        List<Pair<Double, String>> result = new LinkedList<>();
        List<ShapeFeature> all = shapeFeatureMapper.listAll();
        double[] uploadedFeature = ShapeFeatureCalculator.calculateFeature(fileName);
        all.forEach(item -> {
            result.add(new Pair<>(ShapeFeatureCalculator.calculateDistance(item, uploadedFeature), item.getPath()));
        });
        result.sort(Comparator.comparingDouble(Pair::getKey));
        return result.subList(0, 9);
    }
}
