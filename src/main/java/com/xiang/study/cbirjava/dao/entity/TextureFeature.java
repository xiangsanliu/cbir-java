package com.xiang.study.cbirjava.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 项健健
 * @date 2018/7/12 10:04
 * @description
 */
@Getter
@Setter
@NoArgsConstructor
public class TextureFeature {

    private Long id;
    private String path;
    private Double feature0;
    private Double feature1;
    private Double feature2;
    private Double feature3;
    private Double feature4;
    private Double feature5;
    private Double feature6;
    private Double feature7;

    public TextureFeature(String path, double[] feature) {
        this.path = path;
        this.feature0 = feature[0];
        this.feature1 = feature[1];
        this.feature2 = feature[2];
        this.feature3 = feature[3];
        this.feature4 = feature[4];
        this.feature5 = feature[5];
        this.feature6 = feature[6];
        this.feature7 = feature[7];
    }

    public double[] getFeature() {
        double[] feature = new double[8];
        feature[0] = this.feature0;
        feature[1] = this.feature1;
        feature[2] = this.feature2;
        feature[3] = this.feature3;
        feature[4] = this.feature4;
        feature[5] = this.feature5;
        feature[6] = this.feature6;
        feature[7] = this.feature7;
        return feature;
    }

}
