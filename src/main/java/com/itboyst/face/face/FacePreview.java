package com.itboyst.face.face;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.arcsoft.face.enums.ImageFormat;
import com.arcsoft.face.toolkit.ImageInfo;
import com.itboyst.face.util.UserRamCache;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.CvPoint;
import org.bytedeco.opencv.opencv_core.CvScalar;
import org.bytedeco.opencv.opencv_core.IplImage;
import org.bytedeco.opencv.opencv_imgproc.CvFont;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.bytedeco.opencv.global.opencv_core.cvPoint;
import static org.bytedeco.opencv.global.opencv_core.cvScalar;
import static org.bytedeco.opencv.global.opencv_imgproc.cvFont;

/**
 * @author shentao
 * @desc
 * @date 2022/3/30
 */
public class FacePreview {

    private static CvScalar color = cvScalar(0, 0, 255, 0);       // blue [green] [red]
    private static CvFont cvFont = cvFont(opencv_imgproc.FONT_HERSHEY_DUPLEX);

    private FaceRecognize faceRecognize;

    public FacePreview(String libPath, String appId, String sdkKey) {
        this.faceRecognize = new FaceRecognize();
        faceRecognize.initEngine(libPath, appId, sdkKey);
        List<UserRamCache.UserInfo> userList = UserRamCache.getUserList();
        Map<String, byte[]> face = userList.stream().collect(Collectors.toMap(UserRamCache.UserInfo::getName, UserRamCache.UserInfo::getFaceFeature));
        faceRecognize.registerFace(face);

        UserRamCache.addListener(new UserRamCache.Listener() {
            @Override
            public void onAdd(UserRamCache.UserInfo userInfo) {
                Map<String, byte[]> map=new HashMap<>();
                map.put(userInfo.getName(),userInfo.getFaceFeature());
                faceRecognize.registerFace(map);
            }
            @Override
            public void onRemove(UserRamCache.UserInfo userInfo) {
                faceRecognize.removeFace(userInfo.getName());
            }
        });
    }

    public void preview(IplImage iplImage) {
        ImageInfo imageInfo = new ImageInfo();
        imageInfo.setWidth(iplImage.width());
        imageInfo.setHeight(iplImage.height());
        imageInfo.setImageFormat(ImageFormat.CP_PAF_BGR24);
        byte[] imageData = new byte[iplImage.imageSize()];
        iplImage.imageData().get(imageData);
        imageInfo.setImageData(imageData);
        List<FaceRecognize.FacePreviewInfo> previewInfoList = faceRecognize.detectFaces(imageInfo);

        for (FaceRecognize.FacePreviewInfo facePreviewInfo : previewInfoList) {
            int x = facePreviewInfo.getFaceInfo().getRect().getLeft();
            int y = facePreviewInfo.getFaceInfo().getRect().getTop();
            int xMax = facePreviewInfo.getFaceInfo().getRect().getRight();
            int yMax = facePreviewInfo.getFaceInfo().getRect().getBottom();

            CvPoint pt1 = cvPoint(x, y);
            CvPoint pt2 = cvPoint(xMax, yMax);
            opencv_imgproc.cvRectangle(iplImage, pt1, pt2, color, 1, 4, 0);

            FaceRecognize.FaceResult faceResult = faceRecognize.getFaceResult(facePreviewInfo.getFaceInfo(), imageInfo);
            if (faceResult != null) {
                try {
                    CvPoint pt3 = cvPoint(x, y - 2);
                    opencv_imgproc.cvPutText(iplImage,  PinyinUtil.getPinyin(faceResult.getName(), " "), pt3, cvFont, color);
//                    opencv_imgproc.cvPutText(iplImage, faceResult.getName(), pt3, cvFont, color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
