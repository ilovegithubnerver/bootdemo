package com.shiyi.controller;


import com.shiyi.controller.bean.DataBean;
import com.shiyi.controller.bean.ResponseBean;
import com.shiyi.service.impl.XinLianAuthServiceImpl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 新联api
 */
@RestController
public class XinlianAuthController {


    public    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());


    @Autowired
    private XinLianAuthServiceImpl xinLianAuthService;

    /**
     * 识别银行卡
     * @param picUrl
     * @param
     * @return
     */
    @RequestMapping("/bankCardVerify")
    public JSONObject bankCardPicQuery(@RequestParam("picUrl")String picUrl
                                        ){
        ResponseBean headBean=new ResponseBean();
        DataBean baseBean=new DataBean();
        headBean.setBody(baseBean);
       try{

           JSONObject object=xinLianAuthService.bankCardPicQuery(picUrl);
           baseBean.addObject(object);
           baseBean.setResponse("00");
           baseBean.setResponse_desc("请求成功");
           logger.info("info=="+object);
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("请求出错"+e);
            baseBean.setResponse("01");
            baseBean.setResponse_desc("请求失败");
        }

        return  JSONObject.fromObject(headBean);
    }

    /**
     * 身份证查验:识别身份证
     * @param picUrl
     * @param
     * @return
     */
    @RequestMapping("/idCardVerify")
    public JSONObject idCardPicQuery(@RequestParam("picUrl")String picUrl
    ){
        ResponseBean headBean=new ResponseBean();
        DataBean baseBean=new DataBean();
        headBean.setBody(baseBean);
        try{

            JSONObject object=xinLianAuthService.idCardCheckByPic(picUrl);
            baseBean.addObject(object);
            baseBean.setResponse("00");
            baseBean.setResponse_desc("请求成功");
        } catch (Exception e) {
            logger.error("请求出错"+e);
            baseBean.setResponse("01");
            baseBean.setResponse_desc("请求失败");
        }

        return  JSONObject.fromObject(headBean);
    }
}
