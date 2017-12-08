package com.shiyi.mapper;

import com.shiyi.controller.bean.OrderBean;
import com.shiyi.util.PageData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;


/**
 * @Description:
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/9/28
 */

public interface AuthMapper {


    PageData getplatform_message(@Param("platform_code")String platform_code)throws Exception;

    void updateplatform_message(@Param("platform_code") String platform_code)throws Exception;

    int AuthCreditCardPre(PageData pd)throws Exception;

    PageData checkBankCard(PageData pd)throws Exception;

    void savebanktype(PageData pd)throws Exception;

    void saveplatform_message(PageData pd)throws Exception;

    void updateplatform_messagecard(@Param("platform_code") String platform_code)throws Exception;

    PageData findShortMessage(@Param("platform_code") String platform_code)throws Exception;

    void saveShortMessage(PageData pd)throws Exception;

    void updateplatform_messageauth4(@Param("platform_code")String platform_code)throws Exception;

    @Select("select platform_key from auth where platform_code=#{channel_code}")
    @ResultType(String.class)
    String getChannelKeyByCode(@Param("channel_code") String channel_code);

    @Insert("insert into transruning (id_card_no,order_id)values(#{id_card_no},#{order_id})")
    int saveOrder(OrderBean orderBean);
}
