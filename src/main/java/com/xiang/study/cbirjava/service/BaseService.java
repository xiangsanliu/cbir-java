package com.xiang.study.cbirjava.service;

import javafx.util.Pair;

import java.util.List;

/**
 * @author 项健健
 * @date 2018/7/12 11:29
 * @description
 */
public interface BaseService {
    void preProcess() ;
    List<Pair<Double, String>> compare(String fileName);
}
