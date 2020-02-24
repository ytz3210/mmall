package cn.yang.tmall.controller.backend;

import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yangtz
 * @ClassName: CategoryManageController
 * @Description: 商品分类
 * @create 2020-02-24 20:55
 */
@RestController
@RequestMapping("/manage/category")
public class CategoryManageController {
    @Autowired
    ICategoryService iCategoryService;

    /**
     * @Description: 增加商品接口
     * @Param categoryName 商品名称
     * @Param parentId 商品父类id，默认0
     * @Author: Yangtz
     * @Date: 2020-02-24 21:13
     */
    @PostMapping("/add_category")
    public RestTO addCategory(String categoryName, @RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return iCategoryService.addCategory(categoryName, parentId);
    }

    /**
     * @Description: 更新商品名称接口
     * @Param categoryName
     * @Param categoryId
     * @Author: Yangtz
     * @Date: 2020-02-24 21:23
     */
    @PostMapping("/update_category_name")
    public RestTO updateCategoryName(String categoryName, Integer categoryId) {
        return iCategoryService.updateCategoryName(categoryName, categoryId);
    }

    /**
     * @Description: 获取和当前物品同级的物品
     * @Param categoryId
     * @Author: Yangtz
     * @Date: 2020-02-24 21:35
     */
    @GetMapping("/get_category")
    public RestTO getChildrenParallelCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return iCategoryService.getChildrenParallelCategory(parentId);
    }

    /**
     * @Description: 获取当前商品所有的子商品
     * @Param id
     * @Author: Yangtz
     * @Date: 2020-02-24 21:37
     */
    @GetMapping("/get_deep_category")
    public RestTO getCategoryAndDeepChildrenCategory(@RequestParam(value = "categoryId") Integer categoryId) {
        return iCategoryService.getCategoryAndChildrenById(categoryId);
    }
}
