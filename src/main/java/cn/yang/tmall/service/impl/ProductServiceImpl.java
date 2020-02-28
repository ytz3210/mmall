package cn.yang.tmall.service.impl;

import cn.yang.tmall.common.Const;
import cn.yang.tmall.common.ResponseCode;
import cn.yang.tmall.common.RestTO;
import cn.yang.tmall.dao.CategoryMapper;
import cn.yang.tmall.dao.ProductMapper;
import cn.yang.tmall.pojo.Category;
import cn.yang.tmall.pojo.Product;
import cn.yang.tmall.properties.FTPProperties;
import cn.yang.tmall.service.ICategoryService;
import cn.yang.tmall.service.IProductService;
import cn.yang.tmall.vo.ProductDetailVO;
import cn.yang.tmall.vo.ProductListVO;
import cn.yang.tmall.vo.SearchProductDTO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yangtz
 * @ClassName: ProductServiceImpl
 * @Description:
 * @create 2020-02-25 16:00
 */
@Service
public class ProductServiceImpl implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Autowired
    private FTPProperties ftpProperties;

    @Resource
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;

    @Override
    public RestTO saveOrUpdateProduct(Product product) {
        if (product == null) {
            return RestTO.error("新增或更新产品参数不正确");
        }
        if (StringUtils.isNotBlank(product.getSubImages())) {
            String[] subImageArray = product.getSubImages().split(",");
            if (subImageArray.length > 0) {
                product.setMainImage(subImageArray[0]);
            }
        }
        if (product.getId() != null) {
            int rowCount = productMapper.updateByPrimaryKeySelective(product);
            if (rowCount > 0) {
                return RestTO.success("更新产品成功");
            } else {
                return RestTO.error("更新产品失败");
            }
        }
        int rowCount = productMapper.insert(product);
        if (rowCount > 0) {
            return RestTO.success("新增产品成功");
        } else {
            return RestTO.error("新增产品失败");
        }
    }

    @Override
    public RestTO<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return RestTO.error(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int rowCount = productMapper.updateByPrimaryKeySelective(product);
        if (rowCount > 0) {
            return RestTO.success("修改产品销售状态成功");
        }
        return RestTO.error("修改产品销售状态失败");
    }

    @Override
    public RestTO<ProductDetailVO> manageProductDetail(Integer productId) {
        if (productId == null) {
            return RestTO.error(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return RestTO.error("产品已下架或删除");
        }
        //VO对象
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product, productDetailVO);
        productDetailVO.setImageHost(ftpProperties.getServerHttpPrefix());
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            //默认为根节点
            productDetailVO.setParentCategoryId(0);
        } else {
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        return RestTO.success(productDetailVO);
    }

    @Override
    public RestTO<PageInfo<ProductListVO>> getProductList(int pageNum, int pageSize) {
        //startPage -- start 记录一个开始
        PageHelper.startPage(pageNum, pageSize);
        //填充自己的SQL查询逻辑
        List<Product> productList = productMapper.selectListProduct();
        //pageHelper-收尾
        List<ProductListVO> productListVOList = Lists.newArrayList();
        pageProductList(productList, productListVOList);
        PageInfo<ProductListVO> pageInfo = new PageInfo<>(productListVOList);
        return RestTO.success(pageInfo);
    }

    @Override
    public RestTO<PageInfo<ProductListVO>> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByNameAndProductId(productName, productId);
        List<ProductListVO> productListVOList = Lists.newArrayList();
        pageProductList(productList, productListVOList);
        PageInfo<ProductListVO> pageInfo = new PageInfo<>(productListVOList);
        return RestTO.success(pageInfo);
    }

    @Override
    public RestTO<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return RestTO.error(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return RestTO.error("产品已下架或删除");
        }
        if (product.getStatus() != 1) {
            return RestTO.error("产品已下架或删除");
        }
        //VO对象
        ProductDetailVO productDetailVO = new ProductDetailVO();
        BeanUtils.copyProperties(product, productDetailVO);
        productDetailVO.setImageHost(ftpProperties.getServerHttpPrefix());
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null) {
            //默认为根节点
            productDetailVO.setParentCategoryId(0);
        } else {
            productDetailVO.setParentCategoryId(category.getParentId());
        }
        return RestTO.success(productDetailVO);
    }

    @Override
    public RestTO<PageInfo> getProductByKeywordCategory(SearchProductDTO dto, int pageNum, int pageSize) {
        String keyword = dto.getKeyword();
        Integer categoryId = dto.getCategoryId();
        String orderBy = dto.getOrderBy();
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return RestTO.error(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getMsg());
        }
        List<Integer> categoryIdList = new ArrayList<Integer>();

        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVoList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVoList);
                return RestTO.success(pageInfo);
            }
            categoryIdList = iCategoryService.getCategoryAndChildrenById(category.getId()).getData();
        }
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum, pageSize);
        //排序处理
        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(
                StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVO> productListVoList = Lists.newArrayList();

        pageProductList(productList, productListVoList);

        PageInfo<ProductListVO> pageInfo = new PageInfo<>(productListVoList);
        return RestTO.success(pageInfo);
    }

    private void pageProductList(List<Product> productList, List<ProductListVO> productListVOList) {
        for (Product product : productList) {
            ProductListVO productListVO = new ProductListVO();
            BeanUtils.copyProperties(product, productListVO);
            productListVO.setImageHost(ftpProperties.getServerHttpPrefix());
            productListVOList.add(productListVO);
        }
    }
}
