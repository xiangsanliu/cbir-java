package com.xiang.study.cbirjava.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

/**
 * @author 项健健
 * @date 2018/7/12 15:16
 * @description
 */
@Getter
@Setter
@NoArgsConstructor
public class ShapeFeature {

    private Long id;
    private String path;
    private Double feature0;
    private Double feature1;
    private Double feature2;
    private Double feature3;
    private Double feature4;
    private Double feature5;
    private Double feature6;

    public ShapeFeature(String path, double[] feature) {
        this.path = path;
        this.feature0 = feature[0];
        this.feature1 = feature[1];
        this.feature2 = feature[2];
        this.feature3 = feature[3];
        this.feature4 = feature[4];
        this.feature5 = feature[5];
        this.feature6 = feature[6];
    }

    public double[] getFeature() {
        double[] feature = new double[7];
        feature[0] = this.feature0;
        feature[1] = this.feature1;
        feature[2] = this.feature2;
        feature[3] = this.feature3;
        feature[4] = this.feature4;
        feature[5] = this.feature5;
        feature[6] = this.feature6;
        return feature;
    }
}
