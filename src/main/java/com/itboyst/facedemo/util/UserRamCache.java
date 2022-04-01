package com.itboyst.facedemo.util;

import com.google.common.collect.Lists;
import lombok.Data;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author st7251
 * @date 2020/3/13 16:34
 */
public class UserRamCache {

    private static final ConcurrentHashMap<String, UserInfo> USER_INFO_MAP = new ConcurrentHashMap<>();

    private static final Set<Listener> REGISTER = new CopyOnWriteArraySet<>();

    public static void addUser(UserInfo userInfo) {
        USER_INFO_MAP.put(userInfo.getFaceId(), userInfo);
        for (Listener listener : REGISTER) {
            listener.onAdd(userInfo);
        }
    }

    public static void removeUser(String faceId) {
        UserInfo userInfo = USER_INFO_MAP.remove(faceId);
        for (Listener listener : REGISTER) {
            listener.onRemove(userInfo);
        }
    }

    public static List<UserInfo> getUserList() {
        List<UserInfo> userInfoList = Lists.newLinkedList();
        userInfoList.addAll(USER_INFO_MAP.values());
        return userInfoList;
    }

    public static void addListener(Listener listener) {
        REGISTER.add(listener);
    }

    public static void removeListener(Listener listener) {
        REGISTER.remove(listener);
    }

    @Data
    public static class UserInfo {

        private String faceId;
        private String name;
        private byte[] faceFeature;

    }

    public interface Listener {
        default void onAdd(UserInfo userInfo) {
        }

        default void onRemove(UserInfo userInfo) {

        }
    }
}
