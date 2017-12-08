package com.shiyi.service.impl;

import com.shiyi.controller.bean.OrderBean;
import com.shiyi.mapper.AuthMapper;
import com.shiyi.service.AuthService;
import com.shiyi.service.IDCardAndNameService;
import com.shiyi.service.ShouChiPicCheckService;
import com.shiyi.util.PageData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.ShortBufferException;


@Transactional
@Service
public class AuthServiceImpl implements AuthService {



	@Autowired
	private AuthMapper authMapper;
	

	@Autowired
	private ShouChiPicCheckService shouChiPicCheckService;

	//@Autowired
	//private IDCardAndNameService idCardAndNameService;



	@Override
	public PageData getplatform_message(String platform_code) throws Exception {

		return authMapper.getplatform_message(platform_code);
	}

	@Override
	public void updateplatform_message(String platform_code) throws Exception {

		authMapper.updateplatform_message(platform_code);
		
	}

	@Override
	public int AuthCreditCardPre(PageData pd) throws Exception {
		return authMapper.AuthCreditCardPre(pd);

		  //throw  new RuntimeException();
		   //return 1;
	}



	@Override
	public PageData checkBankCard(PageData pd) throws Exception {
		return authMapper.checkBankCard(pd);
	}

	@Override
	public void savebanktype(PageData pd) throws Exception {
		authMapper.savebanktype(pd);
		
	}

	@Override
	public void saveplatform_message(PageData pd) throws Exception {
		authMapper.saveplatform_message(pd);
		
	}





	@Override
	public void updateplatform_messagecard(String platform_code)
			throws Exception {
		authMapper.updateplatform_messagecard(platform_code);
		
	}

	@Override
	public PageData findShortMessage(String platform_code) throws Exception {
		return authMapper.findShortMessage(platform_code);
	}

	@Override
	public void saveShortMessage(PageData pd) throws Exception {
		authMapper.saveShortMessage(pd);
	}

	@Override
	public void updateplatform_messageauth4(String platform_code) throws Exception {
		authMapper.updateplatform_messageauth4(platform_code);
	}

	@Override
	public String getChannelKeyByCode(String channel_code) {
		return authMapper.getChannelKeyByCode(channel_code);
	}

	@Override
	public boolean saveOrder(OrderBean orderBean)throws Exception {

		    authMapper.saveOrder(orderBean);
		    shouChiPicCheckService.setScore(70);
		    boolean success=shouChiPicCheckService.checkPicWithCardIdAndName(orderBean.getId_card_no(),orderBean.getPic_data());

            return success;
		///	throw new ShortBufferException("1");

		// 1;
	}
}

