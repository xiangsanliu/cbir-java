package com.xiang.study.cbirjava.service;

import com.xiang.study.cbirjava.dao.entity.ColorFeature;
import com.xiang.study.cbirjava.dao.mapper.ColorFeatureMapper;
import com.xiang.study.cbirjava.opencv.ColorFeatureCalculator;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.*;

/**
 * @author 项健健
 * @date 2018/7/11 8:50
 * @description
 */
@Service
public class ColorFeatureService implements BaseService {

    private static final Logger logger = LoggerFactory.getLogger(ColorFeatureService.class);

    private static final String ROOT_PATH = "C:\\Users\\xiang\\Pictures\\CBIR\\data";

    @Resource
    private ColorFeatureMapper colorFeatureMapper;

    @Override
    public void preProcess() {
        logger.info("Calculating - ColorFeature...");
        List<ColorFeature> colorFeatureList = new LinkedList<>();
        File rootDir = new File(ROOT_PATH);
        File[] dir = rootDir.listFiles();
        for (File file : Objects.requireNonNull(dir)) {
            String[] picPaths = file.list();
            Arrays.stream(Objects.requireNonNull(picPaths)).forEach(item -> {
                String path = file.getAbsolutePath() + File.separator + item;
                colorFeatureList.add(new ColorFeature(path, ColorFeatureCalculator.calculateHSIMatrix(path)));
            });
        }
        logger.info("ColorFeature Calculation complete!");
        colorFeatureMapper.batchInsert(colorFeatureList);
    }

    @Override
    public List<Pair<Double, String>> compare(String fileName) {
        logger.info("Search for color ...");
        List<Pair<Double, String>> result = new LinkedList<>();
        List<ColorFeature> all = colorFeatureMapper.listAll();
        double[] uploadedFeature = ColorFeatureCalculator.calculateHSIMatrix(fileName);
        all.forEach(item -> {
            result.add(new Pair<>(ColorFeatureCalculator.calculateDistance(item, uploadedFeature), item.getPath()));
        });
        result.sort(Comparator.comparingDouble(Pair::getKey));
        return result.subList(0, 9);
    }

}
