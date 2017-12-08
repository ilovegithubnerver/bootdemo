package com.shiyi.service;


import com.shiyi.util.PageData;

public interface InformationService {
	
	 boolean send(String phone_no, String message_content, boolean isCode)throws Exception;
	
	 PageData findShortMessage(String platform_code)throws Exception;
	
	 void saveShortMessage(PageData pd)throws Exception;


}
