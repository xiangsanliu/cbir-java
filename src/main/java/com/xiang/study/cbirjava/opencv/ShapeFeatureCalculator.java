package com.xiang.study.cbirjava.opencv;

import com.xiang.study.cbirjava.dao.entity.ShapeFeature;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Arrays;

/**
 * @author 项健健
 * @date 2018/7/12 13:26
 * @description
 */
public class ShapeFeatureCalculator {

    public static double[] calculateFeature(String fileName) {
        Matrix matrix = new Matrix(fileName);
        return matrix.getShapeFeature();
    }

    public static double calculateDistance(ShapeFeature shapeFeature, double[] feature) {
        double distance = 0;
        double[] original = shapeFeature.getFeature();
        for (int i = 0; i < feature.length; i++) {
            distance += Math.pow(original[i] - feature[i], 2);
        }
        return distance;
    }

    private static class Matrix {
        private int gray[][];
        private Mat image;
        private int rows;
        private int cols;

        Matrix(String fileName) {
            this.image = Imgcodecs.imread(fileName);
            this.rows = this.image.rows();
            this.cols = this.image.cols();
        }

        double[] getShapeFeature() {
            setGrayMatrix();
            medianFiltering();
            sobel();
            int max = gray[0][0];
            int min = max;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (max > gray[i][j]) {
                        max = gray[i][j];
                    }
                    if (min < gray[i][j]) {
                        min = gray[i][j];
                    }
                }
            }
            float threshold = (float) ((max + min) / 2.0);
            binaryzation(threshold);
            return calculateMomentInvariants();
        }

        private void setGrayMatrix() {
            gray = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    double[] data = image.get(i, j);
                    gray[i][j] = toGray(data[0], data[1], data[2]);
                }
            }
        }

        private void medianFiltering() {
            int median[] = new int[9];
            for (int i = 1; i < rows - 1; i++) {
                for (int j = 1; j < cols - 1; j++) {
                    median[0] = gray[i - 1][j - 1];
                    median[1] = gray[i - 1][j];
                    median[2] = gray[i - 1][j + 1];
                    median[3] = gray[i][j - 1];
                    median[4] = gray[i][j];
                    median[5] = gray[i][j + 1];
                    median[6] = gray[i + 1][j - 1];
                    median[7] = gray[i + 1][j];
                    median[8] = gray[i + 1][j + 1];
                    Arrays.sort(median);
                    gray[i][j] = median[4];
                }
            }
        }

        private void sobel() {
            int ret[][] = new int[rows][cols];
            for (int i = 1; i < rows - 1; i++) {
                for (int j = 1; j < cols - 1; j++) {
                    int gx = gray[i + 1][j - 1] + 2 * gray[i + 1][j] + gray[i + 1][j + 1] - gray[i - 1][j - 1]
                            - 2 * gray[i - 1][j] - gray[i - 1][j + 1];
                    int gy = gray[i - 1][j + 1] + 2 * gray[i][j + 1] + gray[i + 1][j + 1] - gray[i - 1][j - 1]
                            - 2 * gray[i][j - 1] - gray[i + 1][j - 1];
                    ret[i][j] = (int) Math.min(255, (Math.sqrt(gx * gx + gy * gy)));
                }
            }
            this.gray = ret;
        }

        private void binaryzation(float threshold) {
            float threshold2 = 0;
            float g1 = 0, g2 = 0;
            int count1 = 0, count2 = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    if (gray[i][j] <= threshold) {
                        g1 += gray[i][j];
                        count1++;
                    } else {
                        g2 += gray[i][j];
                        count2++;
                    }
                }
            }
            g1 = (float) ((g1 * 1.0) / count1);
            g2 = (float) ((g2 * 1.0) / count2);
            threshold2 = (g1 + g2) / 2;
            float STANDARD = 0.01f;
            if (Math.abs(threshold - threshold2) <= STANDARD) {
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < cols; j++) {
                        if (gray[i][j] <= threshold2) {
                            gray[i][j] = 0;
                        } else {
                            gray[i][j] = 255;
                        }
                    }
                }
            } else {
                binaryzation(threshold2);
            }
        }

        /**
         * ret[0]存储 mjk, ret[1] 存储r
         */
        private double[] getMjk(int j, int k, double aveX, double aveY) {
            double ret[] = new double[2];
            for (int a = 0; a < rows; a++) {
                for (int b = 0; b < cols; b++) {
                    ret[0] = ret[0] + gray[a][b] * Math.pow((a - aveX), j) * Math.pow((b - aveY), k);
                }
            }
            ret[1] = (j + k) * 1.0 / 2 + 1;
            return ret;
        }

        private double[] calculateMomentInvariants() {
            int m00 = 0, m10 = 0, m01 = 0;
            double aveX, aveY;
            double u20, u02, u11, u30, u12, u03, u21;
            double temporary[], ret[] = new double[7];
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    m00 += gray[i][j];
                    m01 += i;
                    m01 += j;
                }
            }
            aveX = m10 * 1.0 / m00;
            aveY = m01 * 1.0 / m00;
            //temporary[0]存储MJk,temporary[1]存储r
            temporary = getMjk(2, 0, aveX, aveY);
            u20 = temporary[0] / (Math.pow(m00, temporary[1]));
            temporary = getMjk(0, 2, aveX, aveY);
            u02 = temporary[0] / (Math.pow(m00, temporary[1]));
            temporary = getMjk(1, 1, aveX, aveY);
            u11 = temporary[0] / (Math.pow(m00, temporary[1]));
            temporary = getMjk(3, 0, aveX, aveY);
            u30 = temporary[0] / (Math.pow(m00, temporary[1]));
            temporary = getMjk(2, 1, aveX, aveY);
            u21 = temporary[0] / (Math.pow(m00, temporary[1]));
            temporary = getMjk(0, 3, aveX, aveY);
            u03 = temporary[0] / (Math.pow(m00, temporary[1]));
            temporary = getMjk(1, 2, aveX, aveY);
            u12 = temporary[0] / (Math.pow(m00, temporary[1]));
            ret[0] = u20 + u02;
            ret[1] = Math.pow((u20 - u02), 2) + 4 * Math.pow(u11, 2);
            ret[2] = Math.pow((u30 - 3 * u12), 2) + Math.pow((u03 - 3 * u21), 2);
            ret[3] = Math.pow((u30 + u12), 2) + Math.pow((u03 + u21), 2);
            ret[4] = (u30 - 3 * u12) * (u03 + u12) * (Math.pow((u30 + u12), 2) - 3 * Math.pow((u21 + u03), 2))
                    + (u03 - 3 * u21) * (u30 + u21) * (Math.pow((u03 + u21), 2) - 3 * Math.pow((u12 + u30), 2));
            ret[5] = (u20 - u02) * (Math.pow((u30 + u12), 2) - Math.pow((u21 + u03), 2))
                    + 4 * u11 * (u30 + u12) * (u03 + u21);
            ret[6] = (Math.pow((u20 - u02), 2) + 4 * u11 * u11) / (Math.pow((u20 + u02), 2));
            return ret;
        }


        private int toGray(double b, double g, double r) {
            double gg = r * 0.3 + g * 0.59 + b * 0.11;
            if (gg >= 0 && gg < 32) {
                return 0;
            } else if (gg >= 32 && gg < 64) {
                return 1;
            } else if (gg >= 64 && gg < 96) {
                return 2;
            } else if (gg >= 96 && gg < 128) {
                return 3;
            } else if (gg >= 128 && gg < 160) {
                return 4;
            } else if (gg >= 160 && gg < 192) {
                return 5;
            } else if (gg >= 192 && gg < 224) {
                return 6;
            } else if (gg >= 224 && gg < 256) {
                return 7;
            }
            return 0;
        }

    }

}
