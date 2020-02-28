package cn.yang.tmall.pojo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem extends BaseEntity{

    private Integer userId;

    private Long orderNo;

    private Integer productId;

    private String productName;

    private String productImage;

    private BigDecimal currentUnitPrice;

    private Integer quantity;

    private BigDecimal totalPrice;
}