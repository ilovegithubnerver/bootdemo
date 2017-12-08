package com.shiyi.util.jiean;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * 验证结果响应类
 * @author fajor.fan
 */
@XmlRootElement(name = "verify")
public class VerifyResponse {
	
	private String versionId;
	
	private String custId;
	
	private String ordId;
	
	private String transType;
	
	private String merPriv;
	
	private String jsonStr;
	
	private String respCode;
	
	private String respDesc;
	
	private String resTxnId;
	
	private String macStr;

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getOrdId() {
		return ordId;
	}

	public void setOrdId(String ordId) {
		this.ordId = ordId;
	}

	public String getTransType() {
		return transType;
	}

	public void setTransType(String transType) {
		this.transType = transType;
	}

	public String getMerPriv() {
		return merPriv;
	}

	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getRespCode() {
		return respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	public String getRespDesc() {
		return respDesc;
	}

	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}

	public String getResTxnId() {
		return resTxnId;
	}

	public void setResTxnId(String resTxnId) {
		this.resTxnId = resTxnId;
	}

	public String getMacStr() {
		return macStr;
	}

	public void setMacStr(String macStr) {
		this.macStr = macStr;
	}
	
}
