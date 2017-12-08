package com.shiyi.util.jiean;


/**
 * 验证元素封装类，暂时定义四个要素，商户可根据需要自行添加
 * @author fajor.fan
 */
public class VerifyParam {
	
	private String CARD_ID;
	
	private String CERT_ID;
	
	private String CERT_NAME;
	
	private String PROD_ID;

	private String MP;
	
	public String getMP() {
		return MP;
	}

	public void setMP(String mP) {
		MP = mP;
	}

	public String getCARD_ID() {
		return CARD_ID;
	}

	public void setCARD_ID(String cARD_ID) {
		CARD_ID = cARD_ID;
	}

	public String getCERT_ID() {
		return CERT_ID;
	}

	public void setCERT_ID(String cERT_ID) {
		CERT_ID = cERT_ID;
	}

	public String getCERT_NAME() {
		return CERT_NAME;
	}

	public void setCERT_NAME(String cERT_NAME) {
		CERT_NAME = cERT_NAME;
	}

	public String getPROD_ID() {
		return PROD_ID;
	}

	public void setPROD_ID(String pROD_ID) {
		PROD_ID = pROD_ID;
	}
	
}
