package com.shiyi.controller.bean;

import org.apache.http.entity.mime.content.FileBody;

/**
 * @Description:
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/9/6
 */
public class XinLianAuthBean {
    private String insId="INS161215000002";
    private String operId;
    private String version="1.0";
    private String requestDate;
    private String requestTime;
    private String serialNo;
    private FileBody recognitionPhoto;

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }


    public FileBody getRecognitionPhoto() {
        return recognitionPhoto;
    }

    public void setRecognitionPhoto(FileBody recognitionPhoto) {
        this.recognitionPhoto = recognitionPhoto;
    }
}
