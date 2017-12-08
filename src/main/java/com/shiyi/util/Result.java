package com.shiyi.util;

/**
 * 接口返回的基类
 * @author sxhan
 * @2012-08-21 PM
 */
public class Result {

	private boolean success = ResponseConstants.SUCCESS;
	private String response_code = ResponseConstants.Resp.EXCEPTION.getCode();
	private String message = ResponseConstants.Resp.EXCEPTION.getMessage();
	private Object data = null;
   
	public Result() {

	}

	public Result(boolean success, String respCode, String respDesc, Object data) {
		super();
		this.success = success;
		this.response_code = respCode;
		this.message = respDesc;
		this.data = data;
	}


	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getmessage()
	{
		return message;
	}

	public void setmessage(String respDesc)
	{
		message = respDesc;
	}

	public Object getData()
	{
		return data;
	}

	public void setData(Object data)
	{
		this.data = data;
	}

	public String getrespone_code() {
		return response_code;
	}

	public void setrespone_code(String resp_code) {
		this.response_code = resp_code;
	}

}