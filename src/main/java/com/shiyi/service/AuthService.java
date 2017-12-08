package com.shiyi.service;



import com.shiyi.controller.bean.OrderBean;
import com.shiyi.util.PageData;



/** 
 * 说明：
 * 创建人：
 * 创建时间：
 *
 */
public interface AuthService {


	/**
	 * 获取平台信息
	 * @param platform_code 平特码
	 * @return
	 * @throws Exception
	 */
	PageData getplatform_message(String platform_code)throws Exception;

	/**
	 * 更新信息
	 * @param platform_code
	 * @throws Exception
	 */
	void updateplatform_message(String platform_code)throws Exception;

	 int AuthCreditCardPre(PageData pd)throws Exception;

	 PageData checkBankCard(PageData pd)throws Exception;
	 void savebanktype(PageData pd)throws Exception;

	 void saveplatform_message(PageData pd)throws Exception;

	 void updateplatform_messagecard(String platform_code)throws Exception;

	PageData findShortMessage(String platform_code)throws Exception;;

	void saveShortMessage(PageData pd)throws Exception;

	void updateplatform_messageauth4(String platform_code)throws Exception;;

	String getChannelKeyByCode(String channel_code);

	/**
	 * 保存流水，并且认证
	 * @param orderBean
	 * @return
	 * @throws Exception
	 */
	boolean saveOrder(OrderBean orderBean)throws Exception;
}

