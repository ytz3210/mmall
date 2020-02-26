package cn.yang.tmall.controller.backend;

import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.pojo.Product;
import cn.yang.tmall.properties.FTPProperties;
import cn.yang.tmall.service.IFileService;
import cn.yang.tmall.service.IProductService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @author Yangtz
 * @ClassName: ProductManageController
 * @Description:
 * @create 2020-02-25 15:57
 */
@RestController
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IProductService iProductService;

    @Autowired
    private IFileService iFileService;

    @Autowired
    FTPProperties ftpProperties;

    /**
     * @Description: 新增或更新商品
     * @Param product
     * @Author: Yangtz
     * @Date: 2020-02-25 17:08
     */
    @PostMapping("/save")
    public RestTO productSave(Product product) {
        return iProductService.saveOrUpdateProduct(product);
    }

    /**
     * @Description: 更新商品销售状态
     * @Param productId
     * @Param status
     * @Author: Yangtz
     * @Date: 2020-02-25 17:08
     */
    @GetMapping("/set_sale_status")
    public RestTO setSaleStatus(@RequestParam(value = "productId") Integer productId,
                                @RequestParam(value = "status") Integer status) {
        return iProductService.setSaleStatus(productId, status);
    }

    /**
     * @Description: 获取商品详情
     * @Param productId
     * @Author: Yangtz
     * @Date: 2020-02-25 18:04
     */
    @GetMapping("/detail")
    public RestTO getDetail(@RequestParam(value = "productId") Integer productId) {
        return iProductService.manageProductDetail(productId);
    }

    /**
     * @Description: 查询商品列表
     * @Param pageNum
     * @Param pageSize
     * @Author: Yangtz
     * @Date: 2020-02-25 18:38
     */
    @GetMapping("/list")
    public RestTO getList(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return iProductService.getProductList(pageNum, pageSize);
    }

    /**
     * @Description: 商品搜索
     * @Param productName
     * @Param productId
     * @Param pageNum
     * @Param pageSize
     * @Author: Yangtz
     * @Date: 2020-02-25 21:18
     */
    @GetMapping("/search")
    public RestTO searchProduct(@RequestParam(required = false) String productName,
                                @RequestParam(required = false) Integer productId,
                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return iProductService.searchProduct(productName, productId, pageNum, pageSize);
    }

    @PostMapping("/upload")
    public RestTO upload(@RequestParam("file") MultipartFile file) {
        String targetFileName = iFileService.upload(file, "file/");
        String url = ftpProperties.getServerHttpPrefix() + targetFileName;
        Map filMap = Maps.newHashMap();
        filMap.put("uri", targetFileName);
        filMap.put("url", url);
        return RestTO.success(filMap);
    }
}
