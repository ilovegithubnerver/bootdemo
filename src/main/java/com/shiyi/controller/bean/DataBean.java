package com.shiyi.controller.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataBean implements Serializable{

	/** 
	* @Fields serialVersionUID : TODO
	*/ 
	private static final long serialVersionUID = 1L;
	

	public String response;			//报文应答码，00表示成功，其它表示失败
	public String response_desc;	//报文应答码中文描述
	
	public List<Object> data = new ArrayList<Object>();
	
	public void addObject(Object o) {
		data.add(o);
	}
	

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	public String getResponse() {
		return response;
	}
	public void setResponse(String response) {
		this.response = response;
	}
	public String getResponse_desc() {
		return response_desc;
	}
	public void setResponse_desc(String response_desc) {
		this.response_desc = response_desc;
	}



}
