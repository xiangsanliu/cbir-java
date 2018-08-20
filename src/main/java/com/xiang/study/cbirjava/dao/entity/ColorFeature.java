package com.xiang.study.cbirjava.dao.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * @author 项健健
 * @date 2018/7/11 8:33
 * @description
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ColorFeature {

    private Long id;
    private String path;
    private Double hM1;
    private Double hM2;
    private Double hM3;
    private Double sM1;
    private Double sM2;
    private Double sM3;
    private Double iM1;
    private Double iM2;
    private Double iM3;

    public ColorFeature(String path, double[] data) {
        this.path = path;
        this.hM1 = data[0];
        this.hM2 = data[1];
        this.hM3 = data[2];
        this.sM1 = data[3];
        this.sM2 = data[4];
        this.sM3 = data[5];
        this.iM1 = data[6];
        this.iM2 = data[7];
        this.iM3 = data[8];
    }

}
