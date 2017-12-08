package com.shiyi.controller;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shiyi.util.PageData;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author FH Q313596790
 * 修改时间：2015、12、11
 */
public class BaseController {
	
	public org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());
	
	/** new PageData对象
	 * @return
	 */
	public PageData getPageData(){
		return new PageData(this.getRequest());
	}
	
	/**得到ModelAndView
	 * @return
	 */
	public ModelAndView getModelAndView(){
		return new ModelAndView();
	}
	
	/**得到request对象
	 * @return
	 */
	public HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		return request;
	}

	protected void doResponse(HttpServletResponse response, String jsonStr) {
		// CLogger.debugLog(JsonResponseUtil.class.getName(), "start write",
		// jsonStr);
		response.setContentType("text/plain;charset=UTF-8");
		try {
			logger.info("返回报文-----------------------"+jsonStr);
			response.getWriter().print(jsonStr);
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public void outResult(HttpServletResponse response, JSONObject jsonObj) {
		// response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsonObj.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error(e);
		} finally{
			if(out != null)
				out.close();
		}
		
	}

	public void outResult(HttpServletResponse response, JSONArray jsonObj) {
		// response.setCharacterEncoding("UTF-8");
		response.setContentType("text/plain;charset=UTF-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(jsonObj.toString());
			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error(e);
		} finally{
			if(out != null)
				out.close();
		}
		
	}

	/**
	 * 
	 * 方法描述:获取请求中的参数转为MAP对象
	 * 
	 * @param
	 * @return date:2014-3-6 add by: raymond
	 */
	protected Map<String, Object> getMapParameter(HttpServletRequest request) {
		Enumeration<String> it = request.getParameterNames();
		Map<String, Object> re = new HashMap<String, Object>();
		while (it.hasMoreElements()) {
			String key = it.nextElement();
			Object value = request.getParameter(key);
			re.put(key, value);
		}
		return re;
	}
}