package com.shiyi.service;


import com.shiyi.exception.ShouChiPicCheckServiceException;

/**
 *	手持身份证图片对比 
 */
public interface ShouChiPicCheckService {

	/**
	 * 照片对比，身份证上的照片与上传的照片对比，返回系统识别结果。默认70分相似算验证通过。
	 * 
	 * @param idCardNo 身份证号码
	 * @param picBase64 照片内容，base64编码
	 * 
	 * @return true 验证通过
	 * 		   false 验证失败
	 * 
	 * @throws ShouChiPicCheckServiceException
	 */
	public boolean checkPicWithCardIdAndName(String idCardNo, String picBase64) throws ShouChiPicCheckServiceException;
	
	/**
	 * 设置相似度
	 * 
	 *  @param score 1-99
	 *  
	 *  @throws ShouChiPicCheckServiceException 不支持相似度设置
	 */
	public void setScore(int score) throws ShouChiPicCheckServiceException;

}
