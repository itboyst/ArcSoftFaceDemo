package com.itboyst.facedemo.mapper;
import com.itboyst.facedemo.dto.FaceUserInfo;
import com.itboyst.facedemo.domain.UserFaceInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MybatisUserFaceInfoMapper {

    List<UserFaceInfo> findUserFaceInfoList();

    void insertUserFaceInfo(UserFaceInfo userFaceInfo);

    List<FaceUserInfo> getUserFaceInfoByGroupId(Integer groupId);

}
