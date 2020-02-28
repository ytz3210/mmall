package cn.yang.tmall.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yangtz
 * @ClassName: BaseEntity
 * @Description:
 * @create 2020-02-28 21:54
 */
@Data
public class BaseEntity implements Serializable {
    private Integer id;
    private Date createTime;
    private Date updateTime;
}
