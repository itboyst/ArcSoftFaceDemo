package com.itboyst.face.service;

import com.itboyst.face.face.FacePreview;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.ffmpeg.global.avutil;
import org.bytedeco.javacv.*;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.servlet.ServletOutputStream;
import java.io.ByteArrayOutputStream;

/**
 * @author shentao
 * @desc
 * @date 2022/3/25
 */
@Service
public class VideoPlayerService {

    @Value("${config.arcface-sdk.app-id}")
    public String appId;

    @Value("${config.arcface-sdk.sdk-key}")
    public String sdkKey;

    @Value("${config.arcface-sdk.active-key}")
    public String activeKey;

    public void servletStreamPlayer(FrameGrabber grabber, ServletOutputStream servletOutputStream) throws Exception {

        //启动人脸处理引擎
        FacePreview faceProcessEngine = new FacePreview(appId, sdkKey,activeKey);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OpenCVFrameConverter.ToIplImage converter = new OpenCVFrameConverter.ToIplImage();

        // 超时时间(15秒)
        grabber.setOption("stimeout", "15000000");
        grabber.setOption("threads", "1");
        // 设置缓存大小，提高画质、减少卡顿花屏
        grabber.setOption("buffer_size", "1020000");

        // 读写超时，适用于所有协议的通用读写超时
        grabber.setOption("rw_timeout", "15000000");
        // 探测视频流信息，为空默认5000000微秒
        grabber.setOption("probesize", "15000000");
        //设置超时时间
        // 解析视频流信息，为空默认5000000微秒
        grabber.setOption("analyzeduration", "15000000");
        grabber.start();


        // 流媒体输出地址，分辨率（长，高），是否录制音频（0:不录制/1:录制） ?overrun_nonfatal=1&fifo_size=50000000
        //这里udp地址增加参数扩大udp缓存
        FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(outputStream, grabber.getImageWidth(), grabber.getImageHeight(), 0);
        // 直播流格式
        // 转码
        recorder.setFormat("flv");
        recorder.setInterleaved(false);
        recorder.setVideoOption("tune", "zerolatency");
        recorder.setVideoOption("preset", "ultrafast");
        recorder.setVideoOption("crf", "26");
        recorder.setVideoOption("threads", "1");
        double frameRate = grabber.getFrameRate();
        recorder.setFrameRate(frameRate);// 设置帧率
        recorder.setGopSize(25);// 设置gop,关键帧
        int videoBitrate = grabber.getVideoBitrate();
        recorder.setVideoBitrate(videoBitrate);// 设置码率500kb/s，画质
        recorder.setVideoCodec(avcodec.AV_CODEC_ID_H264);
        recorder.setPixelFormat(avutil.AV_PIX_FMT_YUV420P);
        recorder.setAudioCodec(avcodec.AV_CODEC_ID_AAC);
        recorder.setTrellis(1);
        recorder.setMaxDelay(0);// 设置延迟
        recorder.setAudioChannels(grabber.getAudioChannels());
        recorder.start();

        long startTime = 0;
        long videoTS = 0;
        for (; ; ) {
            Frame frame = grabber.grab();
            if (frame == null) {
                continue;
            }
            IplImage iplImage = converter.convert(frame);//抓取一帧视频并将其转换为图像，至于用这个图像用来做什么？加水印，人脸识别等等自行添加
            if (iplImage != null) {
                faceProcessEngine.preview(iplImage);
                frame = converter.convert(iplImage);
            }


            if (startTime == 0) {
                startTime = System.currentTimeMillis();
            }
            videoTS = 1000 * (System.currentTimeMillis() - startTime);
            // 判断时间偏移
            if (videoTS > recorder.getTimestamp()) {
                recorder.setTimestamp((videoTS));
            }
            recorder.record(frame);

            if (outputStream.size() > 0) {
                byte[] bytes = outputStream.toByteArray();
                servletOutputStream.write(bytes);

                outputStream.reset();

            }

        }
    }

}
