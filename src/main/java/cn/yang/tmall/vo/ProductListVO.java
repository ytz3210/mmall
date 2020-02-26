package cn.yang.tmall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Yangtz
 * @ClassName: ProductListVO
 * @Description:
 * @create 2020-02-25 18:23
 */
@Data
public class ProductListVO {
    private Integer id;

    private Integer categoryId;

    private String name;

    private String subtitle;

    private String mainImage;

    private BigDecimal price;

    private Integer status;

    private String imageHost;
}
