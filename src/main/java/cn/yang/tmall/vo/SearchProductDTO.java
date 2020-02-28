package cn.yang.tmall.vo;

import lombok.Data;

/**
 * @author Yangtz
 * @ClassName: SearchProductDTO
 * @Description:
 * @create 2020-02-27 21:28
 */
@Data
public class SearchProductDTO {
    private String keyword;
    private Integer categoryId;
    private String orderBy;
}
