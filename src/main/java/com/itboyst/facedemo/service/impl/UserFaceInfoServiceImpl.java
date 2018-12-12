package com.itboyst.facedemo.service.impl;

import com.itboyst.facedemo.dao.mapper.UserFaceInfoMapper;
import com.itboyst.facedemo.domain.UserFaceInfo;
import com.itboyst.facedemo.service.UserFaceInfoService;
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
