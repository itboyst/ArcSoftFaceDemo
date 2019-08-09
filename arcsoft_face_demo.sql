
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user_face_info
-- ----------------------------
DROP TABLE IF EXISTS `user_face_info`;
CREATE TABLE `user_face_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_id` int(11) DEFAULT NULL COMMENT '分组id',
  `face_id` varchar(31) DEFAULT NULL COMMENT '人脸唯一Id',
  `name` varchar(63) DEFAULT NULL COMMENT '名字',
  `age` int(3) DEFAULT NULL COMMENT '年纪',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱地址',
  `gender` smallint(1) DEFAULT NULL COMMENT '性别，1=男，2=女',
  `phone_number` varchar(11) DEFAULT NULL COMMENT '电话号码',
  `face_feature` blob COMMENT '人脸特征',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `fpath` varchar(255) NOT NULL COMMENT '照片路径',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `GROUP_ID` (`group_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;
SET FOREIGN_KEY_CHECKS = 1;
