package com.xny.common.utils;

import cn.hutool.core.lang.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

/**
 * @author 王骞
 * @date 2020-12-17
 */
@Configuration
public class SnowIdUtil {
	private static Snowflake snowflake;

	@Autowired
	public void setSnowflake(Snowflake snowflake) {
		SnowIdUtil.snowflake = snowflake;
	}

	/**
	 * 雪花算法获取下个ID
	 *
	 * @return long
	 */
	public static long nextId() {
		return snowflake.nextId();
	}

}
