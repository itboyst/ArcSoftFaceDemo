package com.itboyst.face.dto;


import com.arcsoft.face.Rect;
import lombok.Data;

@Data
public class FaceRecognitionResDTO {

    private Rect rect;
    private String name;
    private float similar;

}
