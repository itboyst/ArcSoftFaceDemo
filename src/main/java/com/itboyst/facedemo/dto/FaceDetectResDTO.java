package com.itboyst.facedemo.dto;

import com.arcsoft.face.Rect;
import lombok.Data;

/**
 * @author st7251
 * @date 2020/3/13 14:50
 */
@Data
public class FaceDetectResDTO {
    private Rect rect;
    private int orient;
    private int faceId = -1;
    private int age = -1;
    private int gender = -1;
    private int liveness = -1;
}
