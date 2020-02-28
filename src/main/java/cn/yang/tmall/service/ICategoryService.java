package cn.yang.tmall.service;

import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.pojo.Category;

import java.util.List;

/**
 * @author Yangtz
 * @ClassName: ICategoryService
 * @Description:
 * @create 2020-02-24 21:05
 */
public interface ICategoryService {
    RestTO addCategory(String categoryName, Integer parentId);

    RestTO updateCategoryName(String categoryName, Integer categoryId);

    RestTO<List<Category>> getChildrenParallelCategory(Integer categoryId);

    RestTO<List<Integer>> getCategoryAndChildrenById(Integer categoryId);
}
