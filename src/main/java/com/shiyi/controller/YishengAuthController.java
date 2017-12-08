package com.shiyi.controller;


import com.shiyi.controller.bean.DataBean;
import com.shiyi.controller.bean.OrderBean;
import com.shiyi.controller.bean.ResponseBean;
import com.shiyi.service.AuthService;
import com.shiyi.service.IDCardAndNameService;
import com.shiyi.service.ShouChiPicCheckService;
import com.shiyi.util.GenerateOidUtil;
import com.shiyi.util.MD5;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @Description: 易生实名API接口
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/7/20
 */
@RestController
public class YishengAuthController {


    public    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());


     @Autowired
     private AuthService authService;

    @Resource(name="tianJinRenHangService")
    private ShouChiPicCheckService shouChiPicCheckService;

    @Resource(name="tianJinRenHangService")
    private IDCardAndNameService iDCardAndNameService;





    /**
     * 查验:生活照和身份证号
     * @param
     * @param
     * @return
     */
    @RequestMapping("/cusPicVerify")
    public JSONObject custInfoPicVerify(@RequestParam("id_card_no")String id_card_no, @RequestParam(value = "pic_data",required = true)String pic_data,
                                        @RequestParam(value = "sign",required = true)String sign,
                                        @RequestParam("channel_code")String channel_code
                                        ){
        ResponseBean headBean=new ResponseBean();
        DataBean baseBean=new DataBean();
        headBean.setBody(baseBean);
        JSONObject map = new JSONObject();
        try{
            TreeMap treeMap=new TreeMap<String,String>();
            treeMap.put("id_card_no",id_card_no);
            treeMap.put("pic_data",pic_data);
            treeMap.put("channel_code",channel_code);

            StringBuilder signStr=new StringBuilder("");
            Set set= treeMap.entrySet();
            Iterator iterator=set.iterator();
            for(;iterator.hasNext();){
                Map.Entry entry= (Map.Entry) iterator.next();
                signStr.append(entry.getKey()).append("=").append(entry.getValue()).append("&");

            }
            String channelKey= authService.getChannelKeyByCode(channel_code);

            signStr.append("key=").append(channelKey);
            logger.error("SERVER signStr------------------------"+signStr);
            String serverSign= MD5.md5(signStr.toString());
            logger.error("serverSign------------------------"+serverSign);
            logger.error("sign------------------------"+sign);
            if(!serverSign.equals(sign)){
                headBean.setResult("01");
                headBean.setResultDesc("签名错误");
                return  JSONObject.fromObject(headBean);
            }
            OrderBean orderBean=new OrderBean();
            orderBean.setId_card_no(id_card_no);
            orderBean.setPic_data(pic_data);
            orderBean.setOrder_id(GenerateOidUtil.get());
            Boolean rs=authService.saveOrder(orderBean);

        } catch (Exception e) {
            logger.error("请求出错"+e);
            baseBean.setResponse("01");
            baseBean.setResponse_desc("请求失败");
        }

        return  JSONObject.fromObject(headBean);
    }

    /** ok
     * 证卡文字识别
     * @param picUrl
     * @return
     */
    @RequestMapping("/idNoAndNameIdentify")
    public JSONObject idNoAndNameIdentify(@RequestParam(value = "picUrl",required = true)String picUrl,
                                           @RequestParam(value = "picType",required = true)String picType){
        ResponseBean headBean=new ResponseBean();
        DataBean baseBean=new DataBean();
        headBean.setBody(baseBean);
        JSONObject map = new JSONObject();
       try {



        } catch (Exception e) {
            e.printStackTrace();
            logger.error("请求出错: "+e.getMessage());
            baseBean.setResponse("01");
            baseBean.setResponse_desc("请求失败");
        }

        return  JSONObject.fromObject(headBean);
    }






}
