package com.arcsoft.facedemo.service.impl;

import com.arcsoft.facedemo.dao.mapper.UserFaceInfoMapper;
import com.arcsoft.facedemo.domain.UserFaceInfo;
import com.arcsoft.facedemo.service.UserFaceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserFaceInfoServiceImpl implements UserFaceInfoService {


    @Autowired
    private UserFaceInfoMapper userFaceInfoMapper;

    @Override
    public int insertSelective(UserFaceInfo userFaceInfo) {
        return userFaceInfoMapper.insertSelective(userFaceInfo);
    }
}
