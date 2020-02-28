package cn.yang.tmall.pojo;

import lombok.Data;

import java.util.Date;
@Data
public class Shipping extends BaseEntity{

    private Integer userId;

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;
}