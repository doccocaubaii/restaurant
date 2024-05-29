package vn.hust.easypos.service.impl;

import com.google.common.base.Strings;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.hust.easypos.config.Constants;
import vn.hust.easypos.domain.Product;
import vn.hust.easypos.domain.User;
import vn.hust.easypos.repository.ProductRepository;
import vn.hust.easypos.service.dto.ResultDTO;
import vn.hust.easypos.service.dto.product.ProductDetailResponse;
import vn.hust.easypos.service.dto.product.SaveProductRequest;
import vn.hust.easypos.service.util.Common;
import vn.hust.easypos.service.util.ImagePathConstants;
import vn.hust.easypos.web.rest.errors.InternalServerException;

import java.util.Arrays;
import java.util.Optional;

import static vn.hust.easypos.constants.ResultConstants.*;
import static vn.hust.easypos.web.rest.errors.ExceptionConstants.PRODUCT_NOT_FOUND;
import static vn.hust.easypos.web.rest.errors.ExceptionConstants.PRODUCT_NOT_FOUND_VI;

@Service
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final UserService userService;

    private final ProductRepository productRepository;

    public ProductService(UserService userService, ProductRepository productRepository) {
        this.userService = userService;
        this.productRepository = productRepository;
    }

    public ResultDTO getWithPagingForProduct(Pageable pageable, String keyword, Integer company) {
        if (company == null) {
            User user = userService.getUserWithAuthorities();
            company = user.getCompanyId();
        }
        Page<ProductDetailResponse> productResponses = productRepository.getWithPagingForProduct(pageable, company, keyword);
        return new ResultDTO(
            SUCCESS,
            GET_PRODUCTS_SUCCESS_VI,
            true,
            productResponses.getContent(),
            Long.valueOf(productResponses.getTotalElements()).intValue()
        );
    }

    public ResultDTO save(MultipartFile images, SaveProductRequest product, HttpServletRequest httpRequest) {
        // Kiểm tra người dùng đăng nhập
        User user = userService.getUserWithAuthorities();
        Integer comId = user.getCompanyId();

        //Tạo sản phẩm mới
        Product productSave = new Product();
        String url = "";
        if (images != null && !Strings.isNullOrEmpty(images.getOriginalFilename())) {
            url = Common.saveFile(images, Constants.IMAGE_FORMAT, ImagePathConstants.PRODUCT + comId.toString(), httpRequest);
        }
        productSave.setImage(url);
        BeanUtils.copyProperties(product, productSave);
        productSave.setPurchasePrice(product.getPurchasePrice());
        productSave.setSalePrice(product.getSalePrice());
        productSave.setCode(userService.genCode(comId, Constants.PRODUCT_CODE));
        productSave.setActive(true);
        productSave.setNormalizedName(Common.normalizedName(Arrays.asList(product.getName())));
        productRepository.save(productSave);

        return new ResultDTO(SUCCESS, CREATE_PRODUCT_SUCCESS_VI, true);
    }

    @Transactional(readOnly = true)
    public ResultDTO getProductDetail(Integer id) {
        User user = userService.getUserWithAuthorities();
        ProductDetailResponse productItem = productRepository.findByProductId(user.getCompanyId(), id);
        if (productItem == null) {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND);
        }
        log.debug("_getProductDetail: " + GET_PRODUCT_DETAIL_SUCCESS_VI);
        return new ResultDTO(SUCCESS, GET_PRODUCT_DETAIL_SUCCESS_VI, true, productItem);
    }

    public ResultDTO update(MultipartFile images, SaveProductRequest productRequest, HttpServletRequest httpRequest) {
        User user = userService.getUserWithAuthorities();
        Integer productId = productRequest.getId();
        Integer comId = user.getCompanyId();

        Optional<Product> productOptional = productRepository.findByIdAndComIdAndActive(productId, userService.getCompanyId(), true);
        if (productOptional.isEmpty()) {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND);
        }
        Product product = productOptional.get();
        if (images != null && !Strings.isNullOrEmpty(images.getOriginalFilename())) {
            String url = Common.saveFile(images, Constants.IMAGE_FORMAT, ImagePathConstants.PRODUCT + comId.toString(), httpRequest);
            product.setImage(url);
        }
        product.setName(productRequest.getName());
        product.setPurchasePrice(productRequest.getPurchasePrice());
        product.setSalePrice(productRequest.getSalePrice());
        product.setNormalizedName(Common.normalizedName(Arrays.asList(productRequest.getName())));
        product.setDescription(productRequest.getDescription());
        product.setUnit(productRequest.getUnit());
        productRepository.save(product);
        return new ResultDTO(SUCCESS, UPDATE_PRODUCT_SUCCESS_VI, true);
    }

    public ResultDTO delete(Integer id) {
        //                Kiem tra dang nhap
        log.debug("Request to delete Product : {}", id);
        User user = userService.getUserWithAuthorities();
        // Lay san pham
        Optional<Product> productOptional = productRepository.findByIdAndComId(id, user.getCompanyId());
        if (productOptional.isEmpty()) {
            throw new InternalServerException(PRODUCT_NOT_FOUND_VI, PRODUCT_NOT_FOUND);
        }
        Product product = productOptional.get();
        // Thay doi trang thai
        product.setActive(false);
        productRepository.save(product);

        return new ResultDTO(SUCCESS, DELETE_PRODUCT_SUCCESS_VI, true);
    }
}
