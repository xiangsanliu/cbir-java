package com.xiang.study.cbirjava.controller;

import com.xiang.study.cbirjava.service.ColorFeatureService;
import com.xiang.study.cbirjava.service.ShapeFeatureService;
import com.xiang.study.cbirjava.service.TextureFeatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 项健健
 * @date 2018/7/11 8:46
 * @description
 */
@Controller
@RequestMapping("/preProcess")
public class PreProcessController {

    private final ColorFeatureService colorFeatureService;
    private final TextureFeatureService textureFeatureService;
    private final ShapeFeatureService shapeFeatureService;

    @Autowired
    public PreProcessController(ColorFeatureService colorFeatureService, TextureFeatureService textureFeatureService,
                                ShapeFeatureService shapeFeatureService) {
        this.colorFeatureService = colorFeatureService;
        this.textureFeatureService = textureFeatureService;
        this.shapeFeatureService = shapeFeatureService;
    }

    @RequestMapping("calculateColor")
    @ResponseBody
    public String calculateHsi() {
        colorFeatureService.preProcess();
        return "done";
    }

    @RequestMapping("calculateTexture")
    @ResponseBody
    public String calculateTexture() {
        textureFeatureService.preProcess();
        return "done";
    }

    @RequestMapping("/calculateShape")
    @ResponseBody
    public String calculateShape() {
        shapeFeatureService.preProcess();
        return "done";
    }
}
