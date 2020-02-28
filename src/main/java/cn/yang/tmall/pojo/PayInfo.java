package cn.yang.tmall.pojo;

import lombok.Data;

@Data
public class PayInfo extends BaseEntity{

    private Integer userId;

    private Long orderNo;

    private Integer payPlatform;

    private String platformNumber;

    private String platformStatus;

}