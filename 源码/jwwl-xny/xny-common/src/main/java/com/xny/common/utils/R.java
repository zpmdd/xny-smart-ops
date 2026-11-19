package com.xny.common.utils;


import com.xny.common.constant.CommonConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * 响应信息主体
 *
 * @param <T>
 * @author xjc
 */
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "响应信息主体")
public class R<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回标记：成功标记=0，失败标记=1")
	private Integer code;

	@Getter
	@Setter
	@ApiModelProperty(value = "返回信息")
	private String message;

	@Getter
	@Setter
	@ApiModelProperty(value = "数据")
	private T data;

	public boolean succeed() {
		return CommonConstants.SUCCESS.equals(this.code);
	}

	public static <T> R<T> ok() {
		return restResult(null, CommonConstants.SUCCESS, null);
	}

	public static <T> R<T> ok(String msg) {
		return restResult(null, CommonConstants.SUCCESS, msg);
	}

	public static <T> R<T> ok(T data) {
		return restResult(data, CommonConstants.SUCCESS, null);
	}

	public static <T> R<T> ok(T data, String msg) {
		return restResult(data, CommonConstants.SUCCESS, msg);
	}

	public static <T> R<T> failed() {
		return restResult(null, CommonConstants.FAIL, null);
	}

	public static <T> R<T> failed(String msg) {
		return restResult(null, CommonConstants.FAIL, msg);
	}

	public static <T> R<T> failed(T data) {
		return restResult(data, CommonConstants.FAIL, null);
	}

	public static <T> R<T> failed(T data, String msg) {
		return restResult(data, CommonConstants.FAIL, msg);
	}

	public static <T> R<T> failed(int code) {
		return restResult(null, code, null);
	}

	public static <T> R<T> failed(int code, String msg) {
		return restResult(null, code, msg);
	}

	public static <T> R<T> restResult(T data, int code, String msg) {
		R<T> apiResult = new R<T>();
		apiResult.setCode(code);
		apiResult.setData(data);
		apiResult.setMessage(msg);
		return apiResult;
	}

}
