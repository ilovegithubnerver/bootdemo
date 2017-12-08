package com.shiyi.controller;

import com.shiyi.controller.bean.CustomerVerifyBean;
import com.shiyi.controller.bean.DataBean;
import com.shiyi.controller.bean.ResponseBean;
import com.shiyi.util.DownLoadUtil;
import com.shiyi.util.Httpclient;
import com.shiyi.util.MD5;
import com.shiyi.util.RSAUtils;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 实名制系统数据API接口
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/7/20
 */
@RestController
public class AuthController {

    public    final String cusPicCompare="http://apir.oujistore.com/regist_api/api/cusPicCompare";
    public    final String authByCardAndWorkUrl="http://apir.oujistore.com/regist_api/api/custPicIdentify";
    public    final String custInfouUrl="http://apir.oujistore.com/regist_api/api/custInfoPicVerify";
    public    final String authAccount="shsyxy2";
    public    org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(this.getClass());


   // @Resource(name="auhtService")
    //private AuthService authService;

    /**
     * 身份证查验:验证姓名和身份证号
     * @param custCertNo
     * @param custName
     * @return
     */
    @RequestMapping("/custInfoPicVerify")
    public JSONObject custInfoPicVerify(@RequestParam("custCertNo")String custCertNo, @RequestParam(value = "nation",required = false)String nation, @RequestParam(value = "needStaffCheck",required = true)Integer needStaffCheck,
                                        @RequestParam("custName")String custName, @RequestParam(value = "gender",required = false)String gender, @RequestParam(value = "isCheckValiddate",required = false)String isCheckValiddate,
                                        @RequestParam(value = "address",required = false)String address, @RequestParam(value = "certExpdate",required = false)String certExpdate,
                                        @RequestParam(value = "picZ",required = false)String picZ, @RequestParam(value = "picF",required = false)String picF, @RequestParam(value = "notifyUrl",required = false)String notifyUrl
                                        ){
        ResponseBean headBean=new ResponseBean();
        DataBean baseBean=new DataBean();
        headBean.setBody(baseBean);
        JSONObject map = new JSONObject();
        map.put("account", authAccount);
        map.put("password", MD5.md5(authAccount));
        String str_sign=map.toString();
        String cryptograph="";
        try {
            cryptograph = RSAUtils.getsec((str_sign));/// 生成的密文
        } catch (Exception e) {
            logger.error("加密错误"+e);
        }
        map.put("sign", cryptograph);
        map.put("custCertNo", custCertNo);
        map.put("custName", custName);
        map.put("nation", nation);
        map.put("gender", gender);
        map.put("isCheckValiddate", isCheckValiddate);
        map.put("address", address);
        map.put("certExpdate", certExpdate);
        map.put("needStaffCheck", needStaffCheck);
        if (1==needStaffCheck){
            if (StringUtils.isBlank(picZ)||StringUtils.isBlank(picF)||StringUtils.isBlank(notifyUrl)){
                baseBean.setResponse("05");
                baseBean.setResponse_desc("身份证正反面不能为空");
                return  JSONObject.fromObject(headBean);

            }
        }
        try {
            if(!StringUtils.isBlank(picF)){
                map.put("picF", DownLoadUtil.downloadPictureBase64(picF));
            }
            if(!StringUtils.isBlank(picZ)){
                map.put("picZ", DownLoadUtil.downloadPictureBase64(picZ));
            }

        }catch (Exception e){
            logger.error("图片转换失败"+e);
            baseBean.setResponse("05");
            baseBean.setResponse_desc("图片地址错误");
            return  JSONObject.fromObject(headBean);
        }
        map.put("isInterfaceCombination", "1");// TODO 联合使用接口
        Map reqMap=new HashMap();
        reqMap.put("reqJson",map.toString());
        logger.info("param----"+map.get("isCheckValiddate"));
        String rs="";//name, CertNo, nation, gender,picf ,picz ,address , staff_check,paltform,combination
        CustomerVerifyBean verifyBean=new CustomerVerifyBean();
        verifyBean.setAddress(address);
        verifyBean.setCombination(1);
        verifyBean.setCustCertNo(custCertNo);
        verifyBean.setCustName(custName);
        verifyBean.setNation(nation);
        verifyBean.setGender(gender);
        verifyBean.setPicf(picF);
        verifyBean.setPicz(picZ);
        verifyBean.setStaff_check(needStaffCheck);
        try {
           // int result=authService.saveAuth(verifyBean);
            logger.info("verifyBean id----------"+verifyBean.getId());
            rs= Httpclient.sendRequestMethod(reqMap,custInfouUrl,"POST",3000);
            logger.info("认证结果----"+rs);
            if (null!=rs){
                JSONObject object=JSONObject.fromObject(rs);
                int retCode = (Integer) object.get("retCode");
                if (retCode==0) {
                    String data=(String) object.get("data");
                    JSONObject resultJsonObject=JSONObject.fromObject(data);
                    String checkResult= (String) resultJsonObject.get("checkResult");
                    if("1".equals(checkResult)){
                        verifyBean.setCheck_result("1");
                      //  int updateResult=authService.updateAuth(verifyBean);
                        //logger.info("updateResult----"+updateResult);
                        baseBean.addObject(resultJsonObject);
                        baseBean.setResponse("00");
                        baseBean.setResponse_desc("信息一致");
                    }else if("1".equals(String.valueOf(resultJsonObject.get("sendStaffCheck")))){
                        baseBean.setResponse("00");
                        baseBean.setResponse_desc("等待人工审核");
                    } else{
                        baseBean.setResponse("04");
                        baseBean.setResponse_desc("信息不一致");
                    }

                }else{
                    baseBean.setResponse("03");
                    baseBean.setResponse_desc("请求失败，请重试");
                }
            }else{
                logger.warn("接口返回解析错误");
                baseBean.setResponse("02");
                baseBean.setResponse_desc("接口返回解析错误");
            }

        } catch (Exception e) {
            //e.printStackTrace();
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
    @RequestMapping("/custPicIdentify")
    public JSONObject authByCardAndWorkUrl(@RequestParam(value = "picUrl",required = true)String picUrl,
                                           @RequestParam(value = "picType",required = true)String picType){
        ResponseBean headBean=new ResponseBean();
        DataBean baseBean=new DataBean();
        headBean.setBody(baseBean);
        JSONObject map = new JSONObject();
        map.put("account", authAccount);
        map.put("password", MD5.md5(authAccount));
        String str_sign=map.toString();
        try {
            String cryptograph= RSAUtils.getsec((str_sign));/// 生成的密文
            map.put("sign", cryptograph);
            logger.info("picUrl----------"+picUrl);
            String picBase64=DownLoadUtil.downloadPictureBase64(java.net.URLDecoder.decode(picUrl,   "utf-8")  );//.downloadPicture(picUrl);
            map.put("pic", picBase64);
            if (!StringUtils.isBlank(picType))
            map.put("picType", picType);
            Map reqMap=new HashMap();
            reqMap.put("reqJson",map.toString());
           // String rs="";
            //File file=new File("d:/param.txt");
            //FileOutputStream outputStream=new FileOutputStream(file);
            //outputStream.write(reqMap.toString().getBytes());
            //outputStream.flush();
            //outputStream.close();
            String rs=Httpclient.sendRequestMethod(reqMap,authByCardAndWorkUrl,"POST",3000);
            if (null!=rs){
                JSONObject object=JSONObject.fromObject(rs);
                logger.info("obj--------"+object);
                int retCode = (Integer) object.get("retCode");
                if (retCode==0) {// TODO
                    String data=(String) object.get("data");
                    JSONObject resultRs=JSONObject.fromObject(data);
                    baseBean.addObject(resultRs);
                    baseBean.setResponse("00");
                }else{
                    baseBean.setResponse(String.valueOf(object.getString("retCode")));
                    baseBean.setResponse_desc(object.getString("retMsg"));
                }
            }else{
                logger.warn("接口返回解析错误");
                baseBean.setResponse("02");
                baseBean.setResponse_desc("接口返回解析错误");
            }

        } catch (Exception e) {
            logger.error("请求出错: "+e);
            baseBean.setResponse("01");
            baseBean.setResponse_desc("请求失败");
        }

        return  JSONObject.fromObject(headBean);
    }


    /**人像对比
     * @param isHandleCard 1手持 必选
     * @param custCertNo
     * @param custName
     * @param picUrl
     * @return
     */
    @RequestMapping("/cusPicCompare")
    public JSONObject auth(@RequestParam("custCertNo")String custCertNo, @RequestParam("custName")String custName,
                           @RequestParam(value = "picUrl",required = true)String picUrl, @RequestParam("isHandleCard")String isHandleCard,
                           @RequestParam(value = "token" ,required = false)String token, @RequestParam(value = "transId",required = false)String transId
                           ){
        ResponseBean headBean=new ResponseBean();
        DataBean baseBean=new DataBean();
        headBean.setBody(baseBean);
        JSONObject map = new JSONObject();
        map.put("account", authAccount);
        map.put("password", MD5.md5(authAccount));
        String str_sign=JSONObject.fromObject(map).toString();
        String cryptograph="";
        try {
            cryptograph = RSAUtils.getsec((str_sign));/// 生成的密文
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("sign", cryptograph);
        map.put("custCertNo", custCertNo);
        map.put("custName", custName);
        map.put("isInterfaceCombination", "0");// 联合使用 NOT USE
        if ("1".equals(map.getString("isInterfaceCombination"))){
            map.put("token", token);
            map.put("transId", transId);
        }

        try {
            String picR=DownLoadUtil.downloadPictureBase64(java.net.URLDecoder.decode(picUrl,   "utf-8"));
            map.put("picR", picR);
            map.put("isHandleCard", String.valueOf(isHandleCard));
            map.put("needStaffCheck", "0");
            Map reqMap=new HashMap();
            reqMap.put("reqJson",map.toString());
            String rs=Httpclient.sendRequestMethod(reqMap,cusPicCompare,"POST",3000);
            if (null!=rs){
                JSONObject object=JSONObject.fromObject(rs);
                logger.info("obj--------"+object);
                int retCode = (Integer) object.get("retCode");
                if (retCode==0) {// TODO
                    String data=(String) object.get("data");
                    JSONObject resultRs=JSONObject.fromObject(data);
                    baseBean.addObject(resultRs);
                    baseBean.setResponse("00");
                }else{
                    baseBean.setResponse(String.valueOf(object.getString("retCode")));
                    baseBean.setResponse_desc(object.getString("retMsg"));
                }
            }else{
                logger.warn("接口返回解析错误");
                baseBean.setResponse("02");
                baseBean.setResponse_desc("接口返回解析错误");
            }
        } catch (Exception e) {
            logger.error("请求出错"+e);
            baseBean.setResponse("01");
            baseBean.setResponse_desc("请求失败");
        }

        return  JSONObject.fromObject(headBean);
    }




}
