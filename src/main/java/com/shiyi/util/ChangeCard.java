package com.shiyi.util;

/**
 * 获取
 */
public class ChangeCard {
     
   
    
    /**
    * @Title :  银行卡转换
    * @Description: 
    * @param @param 
    * @param @param cardno
    * @param @return    
    * @return String 
    * @throws
     */
    public static String getChangeCard(String cardno) {
    		cardno = cardno.substring(0, 6)+"****"+cardno.substring(cardno.length()-4,cardno.length());
    		return cardno;
       
    }
    
     
     
}