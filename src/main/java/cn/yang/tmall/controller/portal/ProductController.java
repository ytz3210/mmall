package cn.yang.tmall.controller.portal;

import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.pojo.Product;
import cn.yang.tmall.service.IProductService;
import cn.yang.tmall.vo.SearchProductDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Yangtz
 * @ClassName: ProductController
 * @Description:
 * @create 2020-02-27 20:59
 */
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private IProductService iProductService;

    /**
     * @Description: 查看商品详情
     * @Param productId
     * @Author: Yangtz
     * @Date: 2020-02-27 21:01
     */
    @GetMapping("/detail")
    public RestTO detail(Integer productId){
        return iProductService.getProductDetail(productId);
    }

    public RestTO list(@RequestBody(required = false)SearchProductDTO searchProductDTO,
                       @RequestParam(value = "pageNum",required = false)int pageNum,
                       @RequestParam(value = "pageSize",required = false)int pageSize){
        return iProductService.getProductByKeywordCategory(searchProductDTO,pageNum,pageSize);
    }
}
