package cn.yang.tmall.pojo;

import lombok.Data;
@Data
public class Cart extends BaseEntity {

    private Integer productId;

    private Integer quantity;

    private Integer checked;
}