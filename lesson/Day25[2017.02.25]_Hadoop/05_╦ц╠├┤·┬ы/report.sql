/*
Navicat MySQL Data Transfer

Source Server         : 192.168.187.128
Source Server Version : 50626
Source Host           : 192.168.187.128:3306
Source Database       : report

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2016-09-04 13:57:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `dimension_browser`
-- ----------------------------
DROP TABLE IF EXISTS `dimension_browser`;
CREATE TABLE `dimension_browser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `browser_name` varchar(45) NOT NULL DEFAULT '' COMMENT '浏览器名称',
  `browser_version` varchar(255) NOT NULL DEFAULT '' COMMENT '浏览器版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='浏览器维度信息表';

-- ----------------------------
-- Records of dimension_browser
-- ----------------------------

-- ----------------------------
-- Table structure for `dimension_date`
-- ----------------------------
DROP TABLE IF EXISTS `dimension_date`;
CREATE TABLE `dimension_date` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `year` int(11) DEFAULT NULL,
  `season` int(11) DEFAULT NULL,
  `month` int(11) DEFAULT NULL,
  `week` int(11) DEFAULT NULL,
  `day` int(11) DEFAULT NULL,
  `calendar` date DEFAULT NULL,
  `type` enum('year','season','month','week','day') DEFAULT NULL COMMENT '日期格式',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='时间维度信息表';

-- ----------------------------
-- Records of dimension_date
-- ----------------------------

-- ----------------------------
-- Table structure for `dimension_platform`
-- ----------------------------
DROP TABLE IF EXISTS `dimension_platform`;
CREATE TABLE `dimension_platform` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `platform_name` varchar(45) DEFAULT NULL COMMENT '平台名称',
  `platform_version` varchar(10) DEFAULT NULL COMMENT '平台版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='平台维度信息表';

-- ----------------------------
-- Records of dimension_platform
-- ----------------------------

-- ----------------------------
-- Table structure for `stats_device_browser`
-- ----------------------------
DROP TABLE IF EXISTS `stats_device_browser`;
CREATE TABLE `stats_device_browser` (
  `date_dimension_id` int(11) NOT NULL,
  `platform_dimension_id` int(11) NOT NULL,
  `browser_dimension_id` int(11) NOT NULL DEFAULT '0',
  `active_users` int(11) DEFAULT '0' COMMENT '活跃用户数',
  `new_install_users` int(11) DEFAULT '0' COMMENT '新增用户数',
  `total_install_users` int(11) DEFAULT '0' COMMENT '总用户数',
  `sessions` int(11) DEFAULT '0' COMMENT '会话个数',
  `sessions_length` int(11) DEFAULT '0' COMMENT '会话长度',
  `total_members` int(11) unsigned DEFAULT '0' COMMENT '总会员数',
  `active_members` int(11) unsigned DEFAULT '0' COMMENT '活跃会员数',
  `new_members` int(11) unsigned DEFAULT '0' COMMENT '新增会员数',
  `pv` int(11) DEFAULT '0' COMMENT 'pv数',
  `created` date DEFAULT NULL,
  PRIMARY KEY (`platform_dimension_id`,`date_dimension_id`,`browser_dimension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='统计浏览器相关分析数据的统计表';

-- ----------------------------
-- Records of stats_device_browser
-- ----------------------------

-- ----------------------------
-- Table structure for `stats_user`
-- ----------------------------
DROP TABLE IF EXISTS `stats_user`;
CREATE TABLE `stats_user` (
  `date_dimension_id` int(11) NOT NULL COMMENT '时间维度id',
  `platform_dimension_id` int(11) NOT NULL COMMENT '平台维度id',
  `active_users` int(11) DEFAULT '0' COMMENT '活跃用户数',
  `new_install_users` int(11) DEFAULT '0' COMMENT '新增用户数',
  `total_install_users` int(11) DEFAULT '0' COMMENT '总用户数',
  `sessions` int(11) DEFAULT '0' COMMENT '会话个数',
  `sessions_length` int(11) DEFAULT '0' COMMENT '会话长度',
  `total_members` int(11) unsigned DEFAULT '0' COMMENT '总会员数',
  `active_members` int(11) unsigned DEFAULT '0' COMMENT '活跃会员数',
  `new_members` int(11) unsigned DEFAULT '0' COMMENT '新增会员数',
  `created` date DEFAULT NULL COMMENT '记录创建时间',
  PRIMARY KEY (`platform_dimension_id`,`date_dimension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='统计用户基本信息的统计表';

-- ----------------------------
-- Records of stats_user
-- ----------------------------

-- ----------------------------
-- Table structure for `dimension_kpi`
-- ----------------------------
DROP TABLE IF EXISTS `dimension_kpi`;
CREATE TABLE `dimension_kpi` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `kpi_name` varchar(45) DEFAULT NULL COMMENT 'kpi名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of dimension_kpi
-- ----------------------------

-- ----------------------------
-- Table structure for `stats_hourly`
-- ----------------------------
DROP TABLE IF EXISTS `stats_hourly`;
CREATE TABLE `stats_hourly` (
  `platform_dimension_id` int(11) NOT NULL COMMENT '平台维度id',
  `date_dimension_id` int(11) NOT NULL COMMENT '时间维度id',
  `kpi_dimension_id` int(11) NOT NULL COMMENT 'kpi维度id',
  `hour_00` int(11) DEFAULT '0' COMMENT '0-1点的计算值',
  `hour_01` int(11) DEFAULT '0' COMMENT '1-2点的计算值',
  `hour_02` int(11) DEFAULT '0' COMMENT '2-3点的计算值',
  `hour_03` int(11) DEFAULT '0' COMMENT '3-4点的计算值',
  `hour_04` int(11) DEFAULT '0' COMMENT '4-5点的计算值',
  `hour_05` int(11) DEFAULT '0' COMMENT '5-6点的计算值',
  `hour_06` int(11) DEFAULT '0' COMMENT '6-7点的计算值',
  `hour_07` int(11) DEFAULT '0' COMMENT '7-8点的计算值',
  `hour_08` int(11) DEFAULT '0' COMMENT '8-9点的计算值',
  `hour_09` int(11) DEFAULT '0' COMMENT '9-10点的计算值',
  `hour_10` int(11) DEFAULT '0' COMMENT '10-11点的计算值',
  `hour_11` int(11) DEFAULT '0' COMMENT '11-12点的计算值',
  `hour_12` int(11) DEFAULT '0' COMMENT '12-13点的计算值',
  `hour_13` int(11) DEFAULT '0' COMMENT '13-14点的计算值',
  `hour_14` int(11) DEFAULT '0' COMMENT '14-15点的计算值',
  `hour_15` int(11) DEFAULT '0' COMMENT '15-16点的计算值',
  `hour_16` int(11) DEFAULT '0' COMMENT '16-17点的计算值',
  `hour_17` int(11) DEFAULT '0' COMMENT '17-18点的计算值',
  `hour_18` int(11) DEFAULT '0' COMMENT '18-19点的计算值',
  `hour_19` int(11) DEFAULT '0' COMMENT '19-20点的计算值',
  `hour_20` int(11) DEFAULT '0' COMMENT '20-21点的计算值',
  `hour_21` int(11) DEFAULT '0' COMMENT '21-22点的计算值',
  `hour_22` int(11) DEFAULT '0' COMMENT '22-23点的计算值',
  `hour_23` int(11) DEFAULT '0' COMMENT '23-00点的计算值',
  PRIMARY KEY (`platform_dimension_id`,`date_dimension_id`,`kpi_dimension_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='按小时计算数值的分析结果保存表';

-- ----------------------------
-- Records of stats_hourly
-- ----------------------------
