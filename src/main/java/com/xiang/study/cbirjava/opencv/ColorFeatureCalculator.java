package com.xiang.study.cbirjava.opencv;

import com.xiang.study.cbirjava.dao.entity.ColorFeature;
import lombok.*;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * @author 项健健
 * @date 2018/7/9 17:23
 * @description
 */
public class ColorFeatureCalculator {

    public static double[] calculateHSIMatrix(String filePath) {
        Mat mat = Imgcodecs.imread(filePath);
        return calculateHSIMatrix(rgb2Hsi(mat));
    }

    /**
     * 计算颜色差异
     * 只用来比较大小，就不需要开方了
     * @param original 数据库中的图片
     * @param uploaded 上传的图片
     * @return 差异
     */
    public static double calculateDistance(ColorFeature original, double[] uploaded) {
        return Math.pow(original.getHM1() - uploaded[0], 2) + Math.pow(original.getHM2() - uploaded[1], 2) + Math.pow(original.getHM3() - uploaded[2], 2)
                + Math.pow(original.getSM1() - uploaded[3], 2) + Math.pow(original.getSM2() - uploaded[4], 2) + Math.pow(original.getSM3() - uploaded[5], 2)
                + Math.pow(original.getIM1() - uploaded[6], 2) + Math.pow(original.getIM2() - uploaded[7], 2) + Math.pow(original.getIM3() - uploaded[8], 2);
    }

    private static HSI rgb2Hsi(Mat rgb) {
        int total = rgb.rows() * rgb.cols();
        double[] hh = new double[total];
        double[] ss = new double[total];
        double[] ii = new double[total];
        int index = 0;
        for (int rowIndex = 0; rowIndex < rgb.rows(); rowIndex++) {
            for (int colIndex = 0; colIndex < rgb.cols(); colIndex++) {
                double h, s, i;

                int b = (int) rgb.get(rowIndex, colIndex)[0];
                int g = (int) rgb.get(rowIndex, colIndex)[1];
                int r = (int) rgb.get(rowIndex, colIndex)[2];
                double piAngle;
                if (g == b) {
                    piAngle = 0;
                } else {
                    double tempAngle = ((double) (2 * r - g - b)) / (Math.sqrt(3) * (g - b));
                    piAngle = Math.PI / 2 - Math.atan(tempAngle);
                }

                if (g >= b) {
                    h = piAngle;
                } else {
                    h = piAngle + Math.PI;
                }
                s = 2 / Math.sqrt(6) * Math.sqrt((Math.pow(r - g, 2) + (r - b) * (g - b)));
                i = (r + g + b) / Math.sqrt(3);
                hh[index] = h;
                ss[index] = s;
                ii[index] = i;
                index++;
            }
        }
        return new HSI(hh, ss, ii);
    }

    private static double[] calculateHSIMatrix(HSI hsi) {
        double data[] = new double[9];

        data[0] = mean(hsi.getH());
        data[1] = std(hsi.getH(), data[0]);
        data[2] = skew(hsi.getH(), data[0]);

        data[3] = mean(hsi.getS());
        data[4] = std(hsi.getS(), data[3]);
        data[5] = skew(hsi.getS(), data[3]);

        data[6] = mean(hsi.getI());
        data[7] = std(hsi.getI(), data[6]);
        data[8] = skew(hsi.getI(), data[6]);

        return data;
    }

    /**
     * M1
     * @param data data
     * @return m1
     */
    private static double mean(double[] data) {        //一阶矩均值
        double sum = 0;
        for (double aData : data) {
            sum += aData;
        }
        return sum / data.length;
    }

    /**
     * M2
     * @param data data
     * @param mean m1
     * @return m2
     */
    private static double std(double[] data, double mean) {    //二阶矩方差
        double sum = 0;
        for (double aData : data) {
            sum += Math.pow((aData - mean), 2);
        }
        return Math.pow((sum / data.length), 0.5);
    }

    private static double skew(double[] data, double mean) {   //三阶矩斜度
        double sum = 0;
        for (double aData : data) {
            sum += Math.pow((aData - mean), 3);
        }
        return Math.cbrt(sum / data.length);
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    private static class HSI {
        private double h[];
        private double s[];
        private double i[];
    }

}
