package com.shiyi.util.jiean;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 验证请求参数，具体参数含义请详见接口文档
 * @author fajor.fan
 */
public class VerifyRequest {
	
	private String versionId;
	
	private String chrSet;
	
	private String custId;
	
	private String ordId;
	
	private String transType;
	
	private String busiType;
	
	private String merPriv;
	
	private String retUrl;
	
	private String jsonStr;
	
	private String macStr;

	public String getVersionId() {
		return versionId;
	}

	public void setVersionId(String versionId) {
		this.versionId = versionId;
	}

	public String getChrSet() {
		return chrSet;
	}

	public void setChrSet(String chrSet) {
		this.chrSet = chrSet;
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

	public String getBusiType() {
		return busiType;
	}

	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	public String getMerPriv() {
		return merPriv;
	}

	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}

	public String getRetUrl() {
		return retUrl;
	}

	public void setRetUrl(String retUrl) {
		this.retUrl = retUrl;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getMacStr() {
		return macStr;
	}

	public void setMacStr(String macStr) {
		this.macStr = macStr;
	}
	
	@Override
    public String toString() {
    	return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
