package cn.yang.tmall.pojo;

import lombok.Data;

/**
 * @author Yangtz
 * @ClassName: Dict
 * @Description:
 * @create 2020-02-28 21:52
 */
@Data
public class Dict extends BaseEntity {
    private String dict_name;
    private String dict_code;
    private String description;
    private String createBy;
    private Integer type;
}
