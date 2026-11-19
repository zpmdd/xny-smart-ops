/*
 Navicat Premium Data Transfer

 Source Server         : 经纬(内网)
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Schema         : xny_system_dev

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 04/09/2024 11:45:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for data_device_day
-- ----------------------------
DROP TABLE IF EXISTS `data_device_day`;
CREATE TABLE `data_device_day`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `device_id` bigint(64) NOT NULL COMMENT '设备id',
  `param_id` bigint(64) NOT NULL COMMENT '参数id',
  `param_value` decimal(64, 2) NULL DEFAULT NULL COMMENT '参数值',
  `param_time` datetime(0) NULL DEFAULT NULL COMMENT '参数值记录时间',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `combin_index`(`device_id`, `param_id`, `param_time`) USING BTREE,
  INDEX `index_device_id`(`device_id`) USING BTREE,
  INDEX `index_param_id`(`param_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备数据信息表-日' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_device_hour
-- ----------------------------
DROP TABLE IF EXISTS `data_device_hour`;
CREATE TABLE `data_device_hour`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `device_id` bigint(64) NOT NULL COMMENT '设备id',
  `param_id` bigint(64) NOT NULL COMMENT '参数id',
  `param_value` decimal(64, 2) NULL DEFAULT NULL COMMENT '参数值',
  `param_time` datetime(0) NULL DEFAULT NULL COMMENT '参数值记录时间',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `combin_index`(`device_id`, `param_id`, `param_time`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备数据信息表-小时' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_device_month
-- ----------------------------
DROP TABLE IF EXISTS `data_device_month`;
CREATE TABLE `data_device_month`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `device_id` bigint(64) NOT NULL COMMENT '设备id',
  `param_id` bigint(64) NOT NULL COMMENT '参数id',
  `param_value` decimal(64, 2) NULL DEFAULT NULL COMMENT '参数值',
  `param_time` datetime(0) NULL DEFAULT NULL COMMENT '参数值记录时间',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `combin_index`(`device_id`, `param_id`, `param_time`) USING BTREE,
  INDEX `index_device_id`(`device_id`) USING BTREE,
  INDEX `index_param_id`(`param_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备数据信息表-月' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_device_real_time
-- ----------------------------
DROP TABLE IF EXISTS `data_device_real_time`;
CREATE TABLE `data_device_real_time`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `device_id` bigint(64) NOT NULL COMMENT '设备id',
  `param_id` bigint(64) NOT NULL COMMENT '参数id',
  `param_value` decimal(64, 14) NULL DEFAULT NULL COMMENT '参数值',
  `param_time` datetime(0) NULL DEFAULT NULL COMMENT '参数值记录时间',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `combin_index`(`device_id`, `param_id`) USING BTREE,
  INDEX `index_param_id`(`param_id`) USING BTREE,
  INDEX `index_device_id`(`device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备数据信息表-实时数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_hfqx_equipment
-- ----------------------------
DROP TABLE IF EXISTS `data_hfqx_equipment`;
CREATE TABLE `data_hfqx_equipment`  (
  `device_number` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备编号',
  `device_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `work_mode` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工作模式',
  `work_status` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '工作状态',
  `offline_status` tinyint(1) NULL DEFAULT 0 COMMENT '离线状态(0:离线,1:在线)',
  `offline_days` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '长时间离线天数',
  `start_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '起点',
  `end_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '终点',
  `real_time_address` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实时位置',
  `is_start` tinyint(1) NULL DEFAULT 0 COMMENT '是否在起点（0:否,1:是）',
  `is_end` tinyint(1) NULL DEFAULT 0 COMMENT '是否在终点（0:否,1:是）',
  `start_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '启动时间',
  `end_time` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结束时间',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`device_number`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '红枫清洗设备数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_param_config
-- ----------------------------
DROP TABLE IF EXISTS `data_param_config`;
CREATE TABLE `data_param_config`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `project_id` bigint(64) NULL DEFAULT NULL COMMENT '项目id',
  `chart_id` bigint(64) NULL DEFAULT NULL COMMENT '图表id',
  `param_id` bigint(64) NULL DEFAULT NULL COMMENT '参数id',
  `param_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名',
  `param_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数key',
  `param_value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数value',
  `param_unit` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数单位',
  `if_hide` tinyint(255) NULL DEFAULT 0 COMMENT '是否隐藏参数（0:否,1:是）',
  `if_enable` tinyint(1) NULL DEFAULT 0 COMMENT '是否启用(0:停用,1:启用)',
  `data_type` tinyint(1) NULL DEFAULT NULL COMMENT '数据类型（1:数据统计,2:图表）',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_project_id`(`project_id`) USING BTREE,
  INDEX `index_chart_id`(`chart_id`) USING BTREE,
  INDEX `index_param_id`(`param_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备参数配置' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_qd_warn
-- ----------------------------
DROP TABLE IF EXISTS `data_qd_warn`;
CREATE TABLE `data_qd_warn`  (
  `id` int(10) NOT NULL COMMENT '主键id',
  `uuid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '电站下告警设备uuid',
  `type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '告警设备类型，如：station、elink、eblock、meter',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '告警具体设备如：eblock、PCS名称',
  `station_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '告警属于那个电站',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '告警触发的时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '告警关闭的时间',
  `warn_type` tinyint(1) NULL DEFAULT NULL COMMENT '告警类型（1:离线告警,2:非计划运行告警）',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '奇点告警数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_ykc_equipment
-- ----------------------------
DROP TABLE IF EXISTS `data_ykc_equipment`;
CREATE TABLE `data_ykc_equipment`  (
  `connector_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '设备id',
  `station_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '站点id',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '设备状态(0:空闲,1:使用中,2:故障)',
  `current_a` decimal(64, 2) NULL DEFAULT NULL COMMENT '电流(A)',
  `voltage_a` decimal(64, 2) NULL DEFAULT NULL COMMENT '电压(V)',
  `start_time` datetime(0) NULL DEFAULT NULL COMMENT '充电开始时间',
  `end_time` datetime(0) NULL DEFAULT NULL COMMENT '充电结束时间',
  `total_money` decimal(64, 2) NULL DEFAULT NULL COMMENT '累计总金额(元)',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`connector_id`) USING BTREE,
  INDEX `index_StationID`(`station_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '云快充设备数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_ykc_station
-- ----------------------------
DROP TABLE IF EXISTS `data_ykc_station`;
CREATE TABLE `data_ykc_station`  (
  `station_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '站点id',
  `station_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '充电站名称',
  `operator_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '运营商 ID',
  `equipment_owner_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备所属运营平台组织机构代码',
  `country_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '充电站国家代码',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`station_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '云快充充电站数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_device
-- ----------------------------
DROP TABLE IF EXISTS `sys_device`;
CREATE TABLE `sys_device`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `project_id` bigint(64) NOT NULL COMMENT '项目id',
  `yzn_device_id` bigint(64) NULL DEFAULT NULL COMMENT '云综能设备id',
  `yzn_channel_id` bigint(64) NULL DEFAULT NULL COMMENT '云综能通道id',
  `yzn_station_id` bigint(64) NULL DEFAULT NULL COMMENT '云综能场站id',
  `device_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '设备名称',
  `place` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '放置位置',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remarks` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_project_id`(`project_id`) USING BTREE,
  INDEX `index_remarks`(`remarks`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `dict_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典Label',
  `dict_value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典value',
  `dict_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remarks` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `ext` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_dict_type`(`dict_type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据字典' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `parent_id` bigint(64) NULL DEFAULT NULL COMMENT '父id',
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `perms_flag` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
  `sort_num` int(10) NULL DEFAULT 0 COMMENT '排序序号',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remarks` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_nanrui
-- ----------------------------
DROP TABLE IF EXISTS `sys_nanrui`;
CREATE TABLE `sys_nanrui`  (
  `id` bigint(20) NOT NULL COMMENT '主键id（与sys_param_simulate主键对应）',
  `data_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据编号',
  `signal_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '信号类型',
  `inside_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '南睿内部编号',
  `data_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据名称',
  `area` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `submatrix` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '子阵',
  `submatrix_section` int(10) NULL DEFAULT NULL COMMENT '子阵区间（1: 0-9，2: 10-18，3: 19-27，4: 28-36，5:升压站）',
  `remark1` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注1',
  `inverter_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '逆变器名称',
  `remark2` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注2',
  `remark21` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注21',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_data_code`(`data_code`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '南瑞参数信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_param
-- ----------------------------
DROP TABLE IF EXISTS `sys_param`;
CREATE TABLE `sys_param`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `yzn_param_id` bigint(64) NULL DEFAULT NULL COMMENT '云综能参数id',
  `param_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名称',
  `param_unit` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数单位',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remarks` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_yzn_param_id`(`yzn_param_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_param_simulate
-- ----------------------------
DROP TABLE IF EXISTS `sys_param_simulate`;
CREATE TABLE `sys_param_simulate`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `device_id` bigint(64) NULL DEFAULT NULL COMMENT '设备id',
  `param_id` bigint(64) NULL DEFAULT NULL COMMENT '参数id',
  `param_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名',
  `yzn_device_id` bigint(64) NULL DEFAULT NULL COMMENT '云综能设备id',
  `yzn_param_id` bigint(64) NULL DEFAULT NULL COMMENT '云综能参数id',
  `yzn_measure_id` bigint(64) NULL DEFAULT NULL COMMENT '云综能测点id',
  `param_base_value` decimal(64, 2) NULL DEFAULT NULL COMMENT '参数基础值',
  `param_upper_limit` decimal(64, 2) NULL DEFAULT NULL COMMENT '参数值浮动上限',
  `param_lower_limit` decimal(64, 2) NULL DEFAULT NULL COMMENT '参数值浮动下限',
  `param_normal_value` decimal(64, 2) NULL DEFAULT NULL COMMENT '参数值正常值（状态量）',
  `ext` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '扩展字段',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  `data_source` tinyint(1) NULL DEFAULT NULL COMMENT '数据来源（1:云综能,2:云快充,3:南瑞,4.模拟数据,5:奇点数据）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_device_id`(`device_id`) USING BTREE,
  INDEX `index_param_id`(`param_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '设备参数模拟量' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_project
-- ----------------------------
DROP TABLE IF EXISTS `sys_project`;
CREATE TABLE `sys_project`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `project_type_id` bigint(64) NOT NULL COMMENT '项目类型id(字典id)',
  `project_type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目类型名称',
  `project_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名称',
  `head_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '负责人姓名',
  `contact_info` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `province` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址-省',
  `city` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址-市',
  `area` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址-区',
  `address` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址-详细地址',
  `province_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址-省行政编码',
  `city_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址-市行政编码',
  `longitude` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址-经度',
  `latitude` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目地址-纬度',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remarks` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_project_type_id`(`project_type_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_quartz_job
-- ----------------------------
DROP TABLE IF EXISTS `sys_quartz_job`;
CREATE TABLE `sys_quartz_job`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `job_class_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务类名',
  `cron_expression` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'cron表达式',
  `parameter` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数',
  `description` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态(0正常,-1停止)',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` tinyint(4) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `user_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户姓名',
  `account_number` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账号',
  `password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0:女,1:男)',
  `position` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '职位',
  `whether_block_up` tinyint(1) NULL DEFAULT 0 COMMENT '是否停用(0:否,1:是)',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `remarks` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for sys_user_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_menu`;
CREATE TABLE `sys_user_menu`  (
  `id` bigint(64) NOT NULL COMMENT 'id',
  `user_id` bigint(64) NOT NULL COMMENT '用户id',
  `menu_id` bigint(64) NOT NULL COMMENT '菜单id',
  `create_by` bigint(64) NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint(64) NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0:正常,1:已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '人员菜单关系表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
