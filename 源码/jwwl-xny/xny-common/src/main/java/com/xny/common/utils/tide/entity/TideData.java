package com.xny.common.utils.tide.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据实体类
 *
 * @author zhuqianwei
 */
@Data
@ToString
public class TideData {

    private List<TimeAndLevel> dayTimeAndLevel = new ArrayList<>(); // 当日潮汐预报-潮位

    private List<TimeAndLevel> timeAndLevel = new ArrayList<>(); // 当日潮汐预报-高低潮

    private List<TimeAndLevel> dayVelocityOfFlow = new ArrayList<>(); // 当日潮流预报-流速

    private List<TimeAndLevel> velocityOfFlow = new ArrayList<>(); // 当日潮流预报-高低流速

}
