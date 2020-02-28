package cn.yang.tmall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @author Yangtz
 * @ClassName: Dict
 * @Description:
 * @create 2020-02-28 21:52
 */
@Data
@AllArgsConstructor
public class Dict{
    private Integer id;
    private String dict_name;
    private String dict_code;
    private String description;
    private String createBy;
    private Integer dictType;
    private Date createTime;
    private Date updateTime;
}
