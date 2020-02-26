package cn.yang.tmall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author Yangtz
 * @ClassName: ProductDetailVO
 * @Description:
 * @create 2020-02-25 17:29
 */
@Data
public class ProductDetailVO {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String subtitle;
    private String mainImage;
    private String subImages;
    private String detail;
    private BigDecimal price;
    private Integer stock;
    private Integer status;
    private String createTime;
    private String updateTime;
    private String imageHost;
    private Integer parentCategoryId;
}
