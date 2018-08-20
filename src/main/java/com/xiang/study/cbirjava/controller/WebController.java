package com.xiang.study.cbirjava.controller;

import com.xiang.study.cbirjava.service.BaseService;
import com.xiang.study.cbirjava.service.ColorFeatureService;
import com.xiang.study.cbirjava.service.ShapeFeatureService;
import com.xiang.study.cbirjava.service.TextureFeatureService;
import javafx.util.Pair;
import org.opencv.imgcodecs.Imgcodecs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/**
 * @author 项健健
 * @date 2018/7/9 15:16
 * @description
 */
@Controller
public class WebController {

    private static final String UPLOAD_PATH = "C:\\Users\\xiang\\Pictures\\CBIR\\test";

    private static final String FILE_NAME_KEY = "fileName";

    private final ColorFeatureService colorFeatureService;

    private final TextureFeatureService textureFeatureService;

    private final ShapeFeatureService shapeFeatureService;

    @Autowired
    public WebController(ColorFeatureService colorFeatureService, TextureFeatureService textureFeatureService,
                         ShapeFeatureService shapeFeatureService) {
        this.colorFeatureService = colorFeatureService;
        this.textureFeatureService = textureFeatureService;
        this.shapeFeatureService = shapeFeatureService;
    }

    @RequestMapping("/")
    public String welcome() {
        return "welcome";
    }

    @RequestMapping("/colorFeature")
    public String colorFeature(HttpSession session) {
        search(colorFeatureService, session);
        return "redirect:/";
    }

    @RequestMapping("/textureFeature")
    public String textureFeature(HttpSession session) {
        search(textureFeatureService, session);
        return "redirect:/";
    }

    @RequestMapping("/shapeFeature")
    public String shapeFeature(HttpSession session) {
        search(shapeFeatureService, session);
        return "redirect:/";
    }

    @RequestMapping("/uploadedImage")
    public void uploadedImage(HttpSession session, HttpServletResponse response) {
        String fileName = (String) session.getAttribute(FILE_NAME_KEY);
        if (fileName == null) {
            return;
        }
        File image = new File(UPLOAD_PATH + File.separator + fileName);
        if (!image.exists()) {
            return;
        }
        try {
            writeToWeb(image, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/resultImage")
    public void resultImage(@RequestParam("path") String path, HttpServletResponse response) {
        File image = new File(path);
        if (!image.exists()) {
            return;
        }
        try {
            writeToWeb(image, response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file") MultipartFile file, HttpSession session, RedirectAttributes redirectAttributes) {
        if (file.isEmpty()) {
            return "error";
        }
        try {
            byte[] bytes = file.getBytes();
            String fileName = System.currentTimeMillis() + ".jpg";
            Path path = Paths.get(UPLOAD_PATH + File.separator + fileName);
            Files.write(path, bytes);
            session.setAttribute(FILE_NAME_KEY, fileName);
            redirectAttributes.addFlashAttribute("message", "upload successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return e.getMessage();
        }
        return "redirect:/";
    }

    private void writeToWeb(File image, HttpServletResponse response) throws IOException {
        FileInputStream fis = new FileInputStream(image);
        OutputStream oos = null;
        int c;
        while ((c = fis.read()) != -1) {
            oos = response.getOutputStream();
            oos.write(c);
        }
        Objects.requireNonNull(oos).flush();
    }

    private void search(BaseService service, HttpSession session) {
        String fileName = (String) session.getAttribute(FILE_NAME_KEY);
        if (fileName == null) {
            return;
        }
        List<Pair<Double, String>> result = service.compare(UPLOAD_PATH + File.separator + fileName);
        session.setAttribute("compare_result1", result.subList(0, 3));
        session.setAttribute("compare_result2", result.subList(3, 6));
        session.setAttribute("compare_result3", result.subList(6, 9));
    }

}
