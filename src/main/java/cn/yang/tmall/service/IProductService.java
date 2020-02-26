package cn.yang.tmall.service;

import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.pojo.Product;
import cn.yang.tmall.vo.ProductDetailVO;
import cn.yang.tmall.vo.ProductListVO;
import com.github.pagehelper.PageInfo;

/**
 * @author Yangtz
 * @ClassName: IProductService
 * @Description:
 * @create 2020-02-25 16:00
 */
public interface IProductService {
    RestTO saveOrUpdateProduct(Product product);

    RestTO<String> setSaleStatus(Integer productId, Integer status);

    RestTO<ProductDetailVO> manageProductDetail(Integer productId);

    RestTO<PageInfo<ProductListVO>> getProductList(int pageNum, int pageSize);

    RestTO<PageInfo<ProductListVO>> searchProduct(String productName, Integer productId, int pageNum, int pageSize);
}
