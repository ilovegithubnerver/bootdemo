package com.shiyi.controller.bean;

import java.io.Serializable;

/**
 * @Description:实名制认证主体
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/8/1
 */
public class CustomerVerifyBean implements Serializable{
    /**
     * @Fields serialVersionUID : TODO
     */
    private static final long serialVersionUID = 1L;
    private String custCertNo;
    private String custName;
    private String gender;
    private String nation;
    private String address;
    private String picz;
    private String picf;
    private int staff_check;
    private  String check_result;
    private int combination;
    private  int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCombination() {
        return combination;
    }

    public void setCombination(int combination) {
        this.combination = combination;
    }

    public String getCheck_result() {
        return check_result;
    }

    public void setCheck_result(String check_result) {
        this.check_result = check_result;
    }

    public int getStaff_check() {
        return staff_check;
    }

    public void setStaff_check(int staff_check) {
        this.staff_check = staff_check;
    }

    public String getCustCertNo() {
        return custCertNo;
    }

    public void setCustCertNo(String custCertNo) {
        this.custCertNo = custCertNo;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPicf() {
        return picf;
    }

    public void setPicf(String picf) {
        this.picf = picf;
    }

    public String getPicz() {
        return picz;
    }

    public void setPicz(String picz) {
        this.picz = picz;
    }
}
