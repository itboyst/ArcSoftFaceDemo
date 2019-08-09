package com.itboyst.facedemo.domain;


import java.util.Date;


public class UserFaceInfo {

        private Integer id;

        private Integer groupId;

        private String faceId;

        private String name;

        private Integer age;

        private String email;

        private Short gender;

        private String phoneNumber;

        private Date createTime;

        private Date updateTime;

        private byte[] faceFeature;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        
        public Integer getGroupId() {
            return groupId;
        }

        public void setGroupId(Integer groupId) {
            this.groupId = groupId;
        }

        public String getFaceId() {
            return faceId;
        }

        public void setFaceId(String faceId) {
            this.faceId = faceId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Short getGender() {
            return gender;
        }

        public void setGender(Short gender) {
            this.gender = gender;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Date getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Date createTime) {
            this.createTime = createTime;
        }

        public Date getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Date updateTime) {
            this.updateTime = updateTime;
        }

        public byte[] getFaceFeature() {
            return faceFeature;
        }

        public void setFaceFeature(byte[] faceFeature) {
            this.faceFeature = faceFeature;
        }
    }

