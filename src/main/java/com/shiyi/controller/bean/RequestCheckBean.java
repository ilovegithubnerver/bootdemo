package com.shiyi.controller.bean;

import javax.xml.bind.annotation.*;
import java.util.Map;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "requestcheck")  
@XmlType(propOrder = {})  
public class RequestCheckBean {
	
	@XmlElementWrapper(name = "channels")
	private Map<String, ChannelBean> map;

	public Map<String, ChannelBean> getMap() {
		return map;
	}

	public void setMap(Map<String, ChannelBean> map) {
		this.map = map;
	}
	
	

}
