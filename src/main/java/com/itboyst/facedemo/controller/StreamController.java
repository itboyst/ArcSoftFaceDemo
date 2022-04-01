package com.itboyst.facedemo.controller;

import com.itboyst.facedemo.service.VideoPlayerService;
import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.FrameGrabber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @author shentao
 * @desc
 * @date 2022/3/25
 */
@Controller
@Slf4j
public class StreamController {

    @Autowired
    private VideoPlayerService videoPlayerService;

    @RequestMapping(value = "/stream")
    public void test(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String address) throws Exception {

        response.addHeader("Content-Disposition", "attachment;filename=\"" + "127.0.0.1" + "\"");
        response.setContentType("video/x-flv");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("accept_ranges", "bytes");
        response.setHeader("pragma", "no-cache");
        response.setHeader("cache_control", "no-cache");
        response.setHeader("transfer_encoding", "CHUNKED");


        response.setStatus(200);
        FrameGrabber grabber;
        if (address == null) {
            ClassPathResource resource = new ClassPathResource("static/images/videodemo.mp4");
            InputStream inputStream = resource.getInputStream();
            grabber = new FFmpegFrameGrabber(inputStream);
        } else {
            grabber = new FFmpegFrameGrabber(address);
        }

        videoPlayerService.servletStreamPlayer(grabber, response.getOutputStream());


    }

}
