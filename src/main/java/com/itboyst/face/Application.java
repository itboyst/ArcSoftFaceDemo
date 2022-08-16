package com.itboyst.face;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.CvPoint;
import org.bytedeco.opencv.opencv_imgproc.CvFont;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info("springboot run success ");

        Loader.load(opencv_imgproc.class);
        Loader.load(CvPoint.class);
        Loader.load(CvFont.class);

        log.info("默认访问地址：http://127.0.0.1:8089/");
    }
}

