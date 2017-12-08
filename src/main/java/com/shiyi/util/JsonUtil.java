package com.shiyi.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 类说明:JSON转换工具类
 */
public class JsonUtil {
	
	/**
	 * Object转json字符串
	 * @param obj
	 * @return String
	 * @throws Exception
	 */
	public static String toJson(Object obj) {
		return JSON.toJSONString(obj);
	}
	
	/**
	 * json字符串转对象,键值对的方式获取信息
	 * @param json
	 * @return Object
	 * @throws Exception
	 */
	public static JSONObject toObjectMap(String json) throws Exception{
		return JSON.parseObject(json);
	}
	
	/**
	 *  json字符串转对象,JavaBean方式获取信息
	 * @param json
	 * @param clazz
	 * @return Object
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Object toObject(String json,Class clazz) throws Exception{
		try {
			return JSON.parseObject(json, clazz);
		} catch (Exception e) {
			throw new Exception("json转"+clazz.getName()+"异常:"+e.getMessage());
		}
	}
}
