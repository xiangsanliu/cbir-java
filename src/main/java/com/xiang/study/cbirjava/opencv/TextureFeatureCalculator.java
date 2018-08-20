package com.xiang.study.cbirjava.opencv;

import com.xiang.study.cbirjava.dao.entity.TextureFeature;
import lombok.Getter;
import lombok.Setter;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.Arrays;

/**
 * @author 项健健
 * @date 2018/7/12 9:18
 * @description
 */
public class TextureFeatureCalculator {

    private static final double POWER[] = {0.20, 0.20, 0.10, 0.10, 0.10, 0.10, 0.10, 0.10};

    public static double[] calculateFeature(String fileName) {
        Matrix matrix = new Matrix(fileName);
        matrix.setMatrix();
        return matrix.standardize();
    }

    public static double calculateDistance(TextureFeature textureFeature, double[] feature) {
        double distance = 0;
        double original[] = textureFeature.getFeature();
        for (int i = 0; i < feature.length; i++) {
            distance += Math.pow(feature[i] - original[i], 2);
        }
        return distance;
    }

    @Getter
    @Setter
    private static class Matrix {
        private Mat image;
        private double aveCon = 0;
        private double varCon = 0;
        private double aveEnt = 0;
        private double varEnt = 0;
        private double aveAsm = 0;
        private double varAsm = 0;
        private double aveCor = 0;
        private double varCor = 0;
        private int coMatrix0[][] = new int[8][8];
        private int coMatrix45[][] = new int[8][8];
        private int coMatrix90[][] = new int[8][8];
        private int coMatrix135[][] = new int[8][8];

        Matrix(String fileName) {
            this.image = Imgcodecs.imread(fileName);
        }

        double[] standardize() {
            double ave = (aveAsm + aveEnt + aveCon + aveCor + varAsm + varEnt
                    + varCon + varCor) / 8;
            double var = Math.sqrt((Math.pow(Math.abs(aveAsm - ave), 2)
                    + Math.pow(Math.abs(aveEnt - ave), 2)
                    + Math.pow(Math.abs(aveCon - ave), 2)
                    + Math.pow(Math.abs(aveCor - ave), 2)
                    + Math.pow(Math.abs(varAsm - ave), 2)
                    + Math.pow(Math.abs(varEnt - ave), 2)
                    + Math.pow(Math.abs(varCon - ave), 2)
                    + Math.pow(Math.abs(varCor - ave), 2)) / 8);

            double stdAveCon = (aveCon - ave) / (var * 3);
            if (stdAveCon > 1) {
                stdAveCon = 1;
            } else if (stdAveCon < -1) {
                stdAveCon = -1;
            }
            double stdVarCon = (varCon - ave) / (var * 3);
            if (stdVarCon > 1) {
                stdVarCon = 1;
            } else if (stdVarCon < -1) {
                stdVarCon = -1;
            }
            double stdAveEnt = (aveEnt - ave) / (var * 3);
            if (stdAveEnt > 1) {
                stdAveEnt = 1;
            } else if (stdAveEnt < -1) {
                stdAveEnt = -1;
            }
            double stdVarEnt = (varEnt - ave) / (var * 3);
            if (stdVarEnt > 1) {
                stdVarEnt = 1;
            } else if (stdVarEnt < -1) {
                stdVarEnt = -1;
            }
            double stdAveAsm = (aveAsm - ave) / (var * 3);
            if (stdAveAsm > 1) {
                stdAveAsm = 1;
            } else if (stdAveAsm < -1) {
                stdAveAsm = -1;
            }
            double stdVarAsm = (varAsm - ave) / (var * 3);
            if (stdVarAsm > 1) {
                stdVarAsm = 1;
            } else if (stdVarAsm < -1) {
                stdVarAsm = -1;
            }
            double stdAveCor = (aveCor - ave) / (var * 3);
            if (stdAveCor > 1) {
                stdAveCor = 1;
            } else if (stdAveCor < -1) {
                stdAveCor = -1;
            }
            double stdVarCor = (varCor - ave) / (var * 3);
            if (stdVarCor > 1) {
                stdVarCor = 1;
            } else if (stdVarCor < -1) {
                stdVarCor = -1;
            }

            double feature[] = new double[8];
            feature[0] = stdAveAsm;
            feature[1] = stdAveEnt;
            feature[2] = stdAveCon;
            feature[3] = stdAveCor;
            feature[4] = stdVarAsm;
            feature[5] = stdVarEnt;
            feature[6] = stdVarCon;
            feature[7] = stdVarCor;

            return feature;
        }

        private void setMatrix() {
            int row = image.rows();
            int col = image.cols();
            int grayMatrix[][] = new int[row][col];
            double data[];
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    data = image.get(i, j);
                    grayMatrix[i][j] = toGray(data[0], data[1], data[2]);
                }
            }
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    if (j - 1 >= 0) {
                        coMatrix0[grayMatrix[i][j]][grayMatrix[i][j - 1]]++;
                    }

                    if (j + 1 < col) {
                        coMatrix0[grayMatrix[i][j]][grayMatrix[i][j + 1]]++;
                    }

                    if (i - 1 >= 0 && j + 1 < col) {
                        coMatrix45[grayMatrix[i][j]][grayMatrix[i - 1][j + 1]]++;
                    }
                    if (i - 1 >= 0) {
                        coMatrix90[grayMatrix[i][j]][grayMatrix[i - 1][j]]++;
                    }
                    if (i + 1 < row) {
                        coMatrix90[grayMatrix[i][j]][grayMatrix[i + 1][j]]++;
                    }
                    if (i + 1 < row && j + 1 < col) {
                        coMatrix135[grayMatrix[i][j]][grayMatrix[i + 1][j + 1]]++;
                    }
                }

            }

            double asm0 = 0;
            double asm45 = 0;
            double asm90 = 0;
            double asm135 = 0;

            double ent0 = 0;
            double ent45 = 0;
            double ent90 = 0;
            double ent135 = 0;

            double con0 = 0;
            double con45 = 0;
            double con90 = 0;
            double con135 = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    asm0 += Math.pow(coMatrix0[i][j], 2);
                    asm45 += Math.pow(coMatrix45[i][j], 2);
                    asm90 += Math.pow(coMatrix90[i][j], 2);
                    asm135 += Math.pow(coMatrix135[i][j], 2);

                    if (coMatrix0[i][j] == 0) {
                        ent0 -= 0;
                    } else {
                        ent0 -= Math.log(coMatrix0[i][j]) * coMatrix0[i][j];
                    }
                    if (coMatrix45[i][j] == 0) {
                        ent45 -= 0;
                    } else {
                        ent45 -= Math.log(coMatrix45[i][j]) * coMatrix45[i][j];
                    }
                    if (coMatrix90[i][j] == 0) {
                        ent90 -= 0;
                    } else {
                        ent90 -= Math.log(coMatrix90[i][j]) * coMatrix90[i][j];
                    }
                    if (coMatrix135[i][j] == 0) {
                        ent135 -= 0;
                    } else {
                        ent135 -= Math.log(coMatrix135[i][j]) * coMatrix135[i][j];
                    }

                    con0 += Math.pow(i - j, 2) * coMatrix0[i][j];
                    con45 += Math.pow(i - j, 2) * coMatrix45[i][j];
                    con90 += Math.pow(i - j, 2) * coMatrix90[i][j];
                    con135 += Math.pow(i - j, 2) * coMatrix135[i][j];
                }
            }

            double cor0 = 0;
            double cor45 = 0;
            double cor90 = 0;
            double cor135 = 0;

            double aveRow[][] = new double[4][8];
            double aveCol[][] = new double[4][8];

            double aveRow0 = 0;
            double varRow0 = 0;
            double aveRow45 = 0;
            double varRow45 = 0;
            double aveRow90 = 0;
            double varRow90 = 0;
            double aveRow135 = 0;
            double varRow135 = 0;
            double aveCol0 = 0;
            double varCol0 = 0;
            double aveCol45 = 0;
            double varCol45 = 0;
            double aveCol90 = 0;
            double varCol90 = 0;
            double aveCol135 = 0;
            double varCol135 = 0;

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    aveRow[0][i] += coMatrix0[i][j];
                    aveRow[1][i] += coMatrix45[i][j];
                    aveRow[2][i] += coMatrix90[i][j];
                    aveRow[3][i] += coMatrix135[i][j];
                    aveCol[0][i] += coMatrix0[j][i];
                    aveCol[1][i] += coMatrix45[j][i];
                    aveCol[2][i] += coMatrix90[j][i];
                    aveCol[3][i] += coMatrix135[j][i];
                }
            }

            for (int i = 0; i < 8; i++) {
                aveRow0 += aveRow[0][i];
                aveRow45 += aveRow[1][i];
                aveRow90 += aveRow[2][i];
                aveRow135 += aveRow[3][i];
                aveCol0 += aveCol[0][i];
                aveCol45 += aveCol[1][i];
                aveCol90 += aveCol[2][i];
                aveCol135 += aveCol[3][i];
            }
            aveRow0 = aveRow0 / 8;
            aveRow45 = aveRow45 / 8;
            aveRow90 = aveRow90 / 8;
            aveRow135 = aveRow135 / 8;
            aveCol0 = aveCol0 / 8;
            aveCol45 = aveCol45 / 8;
            aveCol90 = aveCol90 / 8;
            aveCol135 = aveCol135 / 8;

            for (int i = 0; i < 8; i++) {
                varRow0 += Math.pow(Math.abs(aveRow[0][i] - aveRow0), 2);
                varRow45 += Math.pow(Math.abs(aveRow[1][i] - aveRow45), 2);
                varRow90 += Math.pow(Math.abs(aveRow[2][i] - aveRow90), 2);
                varRow135 += Math.pow(Math.abs(aveRow[3][i] - aveRow135), 2);
                varCol0 += Math.pow(Math.abs(aveCol[0][i] - aveCol0), 2);
                varCol45 += Math.pow(Math.abs(aveCol[1][i] - aveCol45), 2);
                varCol90 += Math.pow(Math.abs(aveCol[2][i] - aveCol90), 2);
                varCol135 += Math.pow(Math.abs(aveCol[3][i] - aveCol135), 2);
            }
            varRow0 = Math.sqrt(varRow0 / 8);
            varRow45 = Math.sqrt(varRow45 / 8);
            varRow90 = Math.sqrt(varRow90 / 8);
            varRow135 = Math.sqrt(varRow135 / 8);
            varCol0 = Math.sqrt(varCol0 / 8);
            varCol45 = Math.sqrt(varCol45 / 8);
            varCol90 = Math.sqrt(varCol90 / 8);
            varCol135 = Math.sqrt(varCol135 / 8);

            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    cor0 += i * j * coMatrix0[i][j];
                    cor45 += i * j * coMatrix45[i][j];
                    cor90 += i * j * coMatrix90[i][j];
                    cor135 += i * j * coMatrix135[i][j];
                }
            }

            cor0 = (cor0 - aveRow0 * aveCol0) / (varRow0 * varCol0);
            cor45 = (cor45 - aveRow45 * aveCol45) / (varRow45 * varCol45);
            cor90 = (cor90 - aveRow90 * aveCol90) / (varRow90 * varCol90);
            cor135 = (cor135 - aveRow135 * aveCol135)
                    / (varRow135 * varCol135);

            aveAsm = (asm0 + asm45 + asm90 + asm135) / 4;
            aveEnt = (ent0 + ent45 + ent90 + ent135) / 4;
            aveCon = (con0 + con45 + con90 + con135) / 4;
            aveCor = (cor0 + cor45 + cor90 + cor135) / 4;

            varAsm = Math.sqrt(Math.pow(Math.abs(asm0 - aveAsm), 2)
                    + Math.pow(Math.abs(asm45 - aveAsm), 2)
                    + Math.pow(Math.abs(asm90 - aveAsm), 2)
                    + Math.pow(Math.abs(asm135 - aveAsm), 2) / 4);
            varEnt = Math.sqrt(Math.pow(Math.abs(ent0 - aveEnt), 2)
                    + Math.pow(Math.abs(ent45 - aveEnt), 2)
                    + Math.pow(Math.abs(ent90 - aveEnt), 2)
                    + Math.pow(Math.abs(ent135 - aveEnt), 2) / 4);
            varCon = Math.sqrt(Math.pow(Math.abs(con0 - aveCon), 2)
                    + Math.pow(Math.abs(con45 - aveCon), 2)
                    + Math.pow(Math.abs(con90 - aveCon), 2)
                    + Math.pow(Math.abs(con135 - aveCon), 2) / 4);
            varCor = Math.sqrt(Math.pow(Math.abs(cor0 - aveCor), 2)
                    + Math.pow(Math.abs(cor45 - aveCor), 2)
                    + Math.pow(Math.abs(cor90 - aveCor), 2)
                    + Math.pow(Math.abs(cor135 - aveCor), 2) / 4);
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
