package com.shiyi.service;


import com.shiyi.exception.IDCardAndNameServiceException;

public interface IDCardAndNameService {

	/**
	 * 身份证号码与姓名是否一致校验
	 * 
	 * @param idCardNo 身份证号码
	 * @param name 姓名
	 * 
	 * @return true 验证通过
	 * 		   false 验证失败
	 * 
	 *
	 */
	public boolean isCardMatchName(String idCardNo, String name) throws IDCardAndNameServiceException;
}
