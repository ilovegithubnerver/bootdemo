package com.shiyi.util;

/**
 * @ClassName ResponseConstants
 * @Description 接口响应常量类
 * @author zhangguangying
 * @date 2015-7-16 上午11:59:51
 * 
 */
public class ResponseConstants {

	/** 
	 * @Fields SUCCESS : 请求接口网关成功
	 */ 
	public static final boolean SUCCESS=true;
	
	/** 
	 * @Fields FAIL : 请求接口网关失败
	 */ 
	public static final boolean FAIL=false;
	
	
	/**
	 * 接口网关响应枚举
	 * @ClassName Resp
	 * @Description 后续有需要可继续扩展
	 * @author zhangguangying
	 * @date 2015-7-16 上午11:57:58
	 * 
	 */
	public static enum Resp {
		
		OK("200", "请求成功"),
		
		EXCEPTION("00", "未填入返回信息");
		
		/** 
		 * @Fields code : 响应代码
		 */ 
		private String code;
		
		/** 
		 * @Fields message : 响应消息
		 */ 
		private String message;
		
		Resp(String code, String message) {
			this.code = code;
			this.message = message;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}
	
	
}
