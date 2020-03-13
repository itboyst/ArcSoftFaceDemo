package com.itboyst.facedemo.util;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author st7251
 * @date 2020/3/13 16:34
 */
public class UserRamCache {

    private static  ConcurrentHashMap<String, UserInfo> userInfoMap = new ConcurrentHashMap<>();

    public static void addUser(UserInfo userInfo) {
        userInfoMap.put(userInfo.getFaceId(), userInfo);
    }

    public static void removeUser(String faceId) {
        userInfoMap.remove(faceId);
    }

    public static List<UserInfo> getUserList() {
        List<UserInfo> userInfoList = Lists.newLinkedList();
        for (UserInfo value : userInfoMap.values()) {
            userInfoList.add(value);
        }
        return userInfoList;
    }


    @Data
    public static class UserInfo {

        private String faceId;
        private String name;
        private byte[] faceFeature;

    }
}
