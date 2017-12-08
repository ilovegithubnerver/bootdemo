package com.shiyi.controller.bean;

/**
 * @Description:
 * @author: chenjiangpeng
 * @Param:
 * @Return:
 * @Date: 2017/12/7
 */
public class OrderBean {

    private String order_id;

    private String id_card_no;

    private String name;

    private String pic_data;






    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getId_card_no() {
        return id_card_no;
    }

    public void setId_card_no(String id_card_no) {
        this.id_card_no = id_card_no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_data() {
        return pic_data;
    }

    public void setPic_data(String pic_data) {
        this.pic_data = pic_data;
    }
}
