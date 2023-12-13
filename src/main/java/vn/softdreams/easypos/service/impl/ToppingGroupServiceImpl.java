package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.product.ProductItemResponse;
import vn.softdreams.easypos.dto.product.ProductToppingGroupItem;
import vn.softdreams.easypos.dto.product.ProductToppingItem;
import vn.softdreams.easypos.dto.toppingGroup.*;
import vn.softdreams.easypos.repository.ProductRepository;
import vn.softdreams.easypos.repository.ProductToppingRepository;
import vn.softdreams.easypos.repository.ToppingGroupRepository;
import vn.softdreams.easypos.repository.ToppingToppingGroupRepository;
import vn.softdreams.easypos.service.ToppingGroupService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ToppingGroup}.
 */
@Service
@Transactional
public class ToppingGroupServiceImpl implements ToppingGroupService {

    private final Logger log = LoggerFactory.getLogger(ToppingGroupServiceImpl.class);
    private final String ENTITY_NAME = "topping_group";

    private final UserService userService;

    private final ToppingGroupRepository toppingGroupRepository;
    private final ToppingToppingGroupRepository toppingToppingGroupRepository;
    private final ProductToppingRepository productToppingRepository;
    private final ProductRepository productRepository;

    public ToppingGroupServiceImpl(
        UserService userService,
        ToppingGroupRepository toppingGroupRepository,
        ToppingToppingGroupRepository toppingToppingGroupRepository,
        ProductToppingRepository productToppingRepository,
        ProductRepository productRepository
    ) {
        this.userService = userService;
        this.toppingGroupRepository = toppingGroupRepository;
        this.toppingToppingGroupRepository = toppingToppingGroupRepository;
        this.productToppingRepository = productToppingRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ResultDTO save(ToppingGroupUpdateRequest request) {
        boolean isNew = request.getId() == null;
        User user = userService.getUserWithAuthorities(request.getComId());
        ToppingGroup toppingGroup = new ToppingGroup();
        if (!isNew) {
            Optional<ToppingGroup> optional = toppingGroupRepository.findByIdAndComId(request.getId(), user.getCompanyId());
            if (optional.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.TOPPING_GROUP_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.TOPPING_GROUP_NOT_FOUND
                );
            }
            toppingGroup = optional.get();
        }
        if (request.getProducts().isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.TOPPING_PRODUCT_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.TOPPING_PRODUCT_NOT_NULL_CODE
            );
        }
        Optional<ToppingGroup> optional = toppingGroupRepository.findOneByComIdAndName(user.getCompanyId(), request.getName());
        if (
            (isNew && optional.isPresent()) || (!isNew && optional.isPresent() && !Objects.equals(optional.get().getId(), request.getId()))
        ) {
            throw new BadRequestAlertException(
                ExceptionConstants.DUPLICATE_TOPPING_PRODUCT_NAME_VI,
                ENTITY_NAME,
                ExceptionConstants.DUPLICATE_TOPPING_PRODUCT_NAME_CODE
            );
        }
        if (!isNew && !request.getName().equals(toppingGroup.getName())) {
            if (toppingGroupRepository.checkDeleteGroup(toppingGroup.getId()) > 0) {
                throw new BadRequestAlertException(
                    ExceptionConstants.TOPPING_GROUP_CANNOT_UPDATE_VI.replace("@@", toppingGroup.getName()),
                    ENTITY_NAME,
                    ExceptionConstants.TOPPING_GROUP_CANNOT_UPDATE
                );
            }
        }
        Set<Integer> ids = new HashSet<>();
        for (ToppingGroupUpdateRequest.ProductToppingRequest product : request.getProducts()) {
            if (ids.contains(product.getId())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DUPLICATE_TOPPING_PRODUCT_ID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DUPLICATE_TOPPING_PRODUCT_ID_CODE
                );
            }
            ids.add(product.getId());
        }
        List<Product> products = productRepository.findAllByComIdAndIdInAndIsToppingTrueAndActiveTrue(
            user.getCompanyId(),
            request.getProducts().stream().map(ToppingGroupUpdateRequest.ProductToppingRequest::getId).collect(Collectors.toSet())
        );
        if (products.size() != request.getProducts().size()) {
            throw new BadRequestAlertException(
                ExceptionConstants.PRODUCT_LIST_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.PRODUCT_LIST_INVALID_CODE
            );
        }
        Map<Integer, String> mapProducts = products.stream().collect(Collectors.toMap(Product::getId, Product::getName));
        for (ToppingGroupUpdateRequest.ProductToppingRequest req : request.getProducts()) {
            if (!mapProducts.containsKey(req.getId())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_LIST_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_LIST_INVALID_CODE
                );
            }
            if (!mapProducts.get(req.getId()).equals(req.getName())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PRODUCT_LIST_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PRODUCT_LIST_INVALID_CODE
                );
            }
        }
        BeanUtils.copyProperties(request, toppingGroup);
        if (toppingGroup.getRequiredOptional() == null) {
            toppingGroup.setRequiredOptional(Boolean.FALSE);
        }
        toppingGroup.setNormalizedName(Common.normalizedName(List.of(request.getName())));
        toppingGroupRepository.save(toppingGroup);
        List<ToppingToppingGroup> groups = new ArrayList<>();
        for (ToppingGroupUpdateRequest.ProductToppingRequest product : request.getProducts()) {
            ToppingToppingGroup group = new ToppingToppingGroup();
            group.setToppingGroupId(toppingGroup.getId());
            group.setProductId(product.getId());
            group.setProductName(product.getName());
            groups.add(group);
        }
        if (!isNew) {
            List<ToppingToppingGroup> toppingToppingGroups = toppingToppingGroupRepository.findAllByToppingGroupId(toppingGroup.getId());
            Map<Integer, ToppingToppingGroup> map = new HashMap<>();
            for (ToppingToppingGroup group : toppingToppingGroups) {
                map.put(group.getProductId(), group);
            }
            for (ToppingGroupUpdateRequest.ProductToppingRequest product : request.getProducts()) {
                map.remove(product.getId());
            }
            for (Map.Entry<Integer, ToppingToppingGroup> entry : map.entrySet()) {
                Integer deleteProductId = entry.getKey();
                if (toppingToppingGroupRepository.checkDeleteProductInGroup(deleteProductId, toppingGroup.getId()) > 0) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.PRODUCT_TOPPING_CANNOT_DELETE_VI.replace("@@", entry.getValue().getProductName()),
                        ENTITY_NAME,
                        ExceptionConstants.PRODUCT_TOPPING_CANNOT_DELETE
                    );
                }
            }

            toppingToppingGroupRepository.deleteAll(toppingToppingGroups);

            // update product_toppping
            Set<Integer> idSet = new HashSet<>();
            List<ProductTopping> productToppings = productToppingRepository.findAllByToppingGroupId(toppingGroup.getId());
            for (ProductTopping topping : productToppings) {
                idSet.add(topping.getProductId());
            }
            productToppingRepository.deleteAll(productToppings);
            addProductTopping(idSet, request.getProducts(), toppingGroup.getId());
        }
        toppingToppingGroupRepository.saveAll(groups);
        ToppingGroupResponse response = new ToppingGroupResponse();
        BeanUtils.copyProperties(toppingGroup, response);
        response.setNumberOption(0);
        response.setNumberProductLink(0);
        if (!isNew) {
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.TOPPING_GROUP_UPDATE_SUCCESS_VI, true, response);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.TOPPING_GROUP_CREATE_SUCCESS_VI, true, response);
    }

    @Override
    public ToppingGroup update(ToppingGroup toppingGroup) {
        log.debug("Request to save ToppingGroup : {}", toppingGroup);
        return toppingGroupRepository.save(toppingGroup);
    }

    @Override
    public Optional<ToppingGroup> partialUpdate(ToppingGroup toppingGroup) {
        log.debug("Request to partially update ToppingGroup : {}", toppingGroup);

        return toppingGroupRepository
            .findById(toppingGroup.getId())
            .map(existingToppingGroup -> {
                if (toppingGroup.getComId() != null) {
                    existingToppingGroup.setComId(toppingGroup.getComId());
                }
                if (toppingGroup.getName() != null) {
                    existingToppingGroup.setName(toppingGroup.getName());
                }
                if (toppingGroup.getNormalizedName() != null) {
                    existingToppingGroup.setNormalizedName(toppingGroup.getNormalizedName());
                }
                if (toppingGroup.getRequiredOptional() != null) {
                    existingToppingGroup.setRequiredOptional(toppingGroup.getRequiredOptional());
                }

                return existingToppingGroup;
            })
            .map(toppingGroupRepository::save);
    }

    @Override
    public ResultDTO getAllWithPaging(Pageable pageable, Integer comId, String keyword, Boolean isCountAll) {
        User user = userService.getUserWithAuthorities(comId);
        ResultDTO resultDTO = new ResultDTO();
        Page<ToppingGroupResponse> page = toppingGroupRepository.getALlWithPaging(pageable, comId, keyword, isCountAll);
        Map<Integer, Integer> optionMap = productToppingRepository
            .getProductInside()
            .stream()
            .collect(Collectors.toMap(IToppingCount::getToppingGroupId, IToppingCount::getNumber));
        Map<Integer, Integer> productLinkMap = productToppingRepository
            .getProductLink()
            .stream()
            .collect(Collectors.toMap(IToppingCount::getToppingGroupId, IToppingCount::getNumber));
        List<ToppingGroupResponse> responses = page.getContent();
        for (ToppingGroupResponse response : responses) {
            response.setNumberOption(optionMap.getOrDefault(response.getId(), 0));
            response.setNumberProductLink(productLinkMap.getOrDefault(response.getId(), 0));
        }
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setReason(ResultConstants.SUCCESS_GET_LIST);
        resultDTO.setStatus(true);
        resultDTO.setData(responses);
        if (Boolean.TRUE.equals(isCountAll)) {
            resultDTO.setCount((int) page.getTotalElements());
        }
        return resultDTO;
    }

    @Override
    public ResultDTO getToppingGroupDetail(Integer id) {
        User user = userService.getUserWithAuthorities();
        ToppingGroupItemResult result = new ToppingGroupItemResult();
        Optional<ToppingGroup> optional = toppingGroupRepository.findByIdAndComId(id, user.getCompanyId());
        if (optional.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.TOPPING_GROUP_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.TOPPING_GROUP_NOT_FOUND
            );
        }
        ToppingGroup toppingGroup = optional.get();
        List<ToppingRequiredItem> items = toppingToppingGroupRepository.findProductIdByToppingGroupId(List.of(toppingGroup.getId()));
        List<Integer> productIds = items.stream().map(ToppingRequiredItem::getProductId).collect(Collectors.toList());
        List<ProductToppingItem> productResponses = productRepository.findForToppingGroupDetail(
            user.getCompanyId(),
            productIds,
            Boolean.TRUE
        );
        BeanUtils.copyProperties(toppingGroup, result);
        List<Integer> ids = productToppingRepository.findProductIdByToppingGroupId(id);
        List<ProductToppingItem> productLinkResponses = productRepository.findForToppingGroupDetail(
            user.getCompanyId(),
            ids,
            Boolean.FALSE
        );
        result.setProducts(productResponses);
        result.setProductLinks(productLinkResponses);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_DETAIL, true, result);
    }

    @Override
    public ResultDTO delete(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<ToppingGroup> optional = toppingGroupRepository.findByIdAndComId(id, user.getCompanyId());
        if (optional.isEmpty()) {
            throw new BadRequestAlertException(
                ExceptionConstants.TOPPING_GROUP_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.TOPPING_GROUP_NOT_FOUND
            );
        }
        if (toppingGroupRepository.checkDeleteGroup(id) > 0) {
            throw new BadRequestAlertException(
                ExceptionConstants.TOPPING_GROUP_CANNOT_DELETE_VI.replace("@@", optional.get().getName()),
                ENTITY_NAME,
                ExceptionConstants.TOPPING_GROUP_CANNOT_DELETE
            );
        }
        List<ProductTopping> productToppings = productToppingRepository.findAllByToppingGroupId(id);
        productToppingRepository.deleteAll(productToppings);

        List<ToppingToppingGroup> groups = toppingToppingGroupRepository.findAllByToppingGroupId(id);
        toppingToppingGroupRepository.deleteAll(groups);

        toppingGroupRepository.delete(optional.get());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.TOPPING_GROUP_DELETE_SUCCESS_VI, true);
    }

    @Override
    public ResultDTO getListToppingForProduct(Integer page, Integer size, Integer id, Boolean isSingleList, String keyword) {
        User user = userService.getUserWithAuthorities();
        Pageable pageable;
        if (page == null || size == null) {
            pageable = PageRequest.of(0, 2000);
        } else {
            pageable = PageRequest.of(page, size);
        }
        List<ToppingItem> toppingItems = new ArrayList<>();
        List<ToppingItem> productToppings = new ArrayList<>();
        List<ToppingItem> groupToppings = new ArrayList<>();
        List<Product> products = productRepository.findAllByComIdAndIsToppingTrueAndActiveTrue(user.getCompanyId(), pageable);
        List<ToppingGroup> groups = toppingGroupRepository.findAllByComId(user.getCompanyId(), pageable);
        if (keyword != null && !Strings.isNullOrEmpty(keyword.trim())) {
            products =
                products
                    .stream()
                    .filter(product ->
                        product.getNormalizedName() != null && product.getNormalizedName().contains(Common.normalizedName(List.of(keyword)))
                    )
                    .collect(Collectors.toList());
            groups =
                groups
                    .stream()
                    .filter(toppingGroup ->
                        toppingGroup.getNormalizedName() != null &&
                        toppingGroup.getNormalizedName().contains(Common.normalizedName(List.of(keyword)))
                    )
                    .collect(Collectors.toList());
        }
        for (Product product : products) {
            if (id != null && Objects.equals(product.getId(), id)) {
                continue;
            }
            ToppingItem item = new ToppingItem();
            item.setId(product.getId());
            item.setName(product.getName());
            item.setIsTopping(Boolean.TRUE);
            item.setImageUrl(product.getImage());
            item.setSalePrice(product.getSalePrice());
            toppingItems.add(item);
            productToppings.add(item);
        }
        for (ToppingGroup group : groups) {
            ToppingItem item = new ToppingItem();
            item.setId(group.getId());
            item.setName(group.getName());
            item.setIsTopping(Boolean.FALSE);
            toppingItems.add(item);
            groupToppings.add(item);
        }
        if (isSingleList != null && isSingleList) {
            ProductToppingGroupItem result = new ProductToppingGroupItem();
            result.setGroupToppings(groupToppings);
            result.setProductToppings(productToppings);
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, result);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, toppingItems);
    }

    @Override
    public ResultDTO getListToppingByProductId(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<Product> productOptional = productRepository.findByIdAndComId(id, user.getCompanyId());
        if (productOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.PRODUCT_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.PRODUCT_NOT_FOUND);
        }
        List<ToppingGroupItem> groupItems = productToppingRepository.findToppingGroupByProductId(id);
        List<ToppingGroupItemResponse> results = new ArrayList<>();
        List<ProductToppingItemResponse> itemResponses = productToppingRepository.findToppingGroupForBill(id);
        Map<Integer, List<ProductItemResponse>> map = new HashMap<>();
        for (ProductToppingItemResponse item : itemResponses) {
            List<ProductItemResponse> list = new ArrayList<>();
            if (map.containsKey(item.getToppingGroupId())) {
                list = map.get(item.getToppingGroupId());
            }
            ProductItemResponse response = new ProductItemResponse();
            BeanUtils.copyProperties(item, response);
            response.setProductCode(item.getCode());
            response.setProductName(item.getName());
            list.add(response);
            map.put(item.getToppingGroupId(), list);
        }
        if (map.containsKey(null)) {
            ToppingGroupItemResponse result = new ToppingGroupItemResponse();
            result.setIsGroupTopping(Boolean.FALSE);
            result.setId(0);
            result.setRequiredOptional(Boolean.FALSE);
            result.setProducts(map.get(null));
            results.add(result);
        }
        for (ToppingGroupItem item : groupItems) {
            ToppingGroupItemResponse result = new ToppingGroupItemResponse();
            BeanUtils.copyProperties(item, result);
            if (map.containsKey(item.getId())) {
                result.setProducts(map.get(item.getId()));
            }
            result.setIsGroupTopping(Boolean.TRUE);
            results.add(result);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, results);
    }

    private void addProductTopping(
        Set<Integer> ids,
        List<ToppingGroupUpdateRequest.ProductToppingRequest> products,
        Integer toppingGroupId
    ) {
        List<ProductTopping> productToppings = new ArrayList<>();
        for (Integer id : ids) {
            for (ToppingGroupUpdateRequest.ProductToppingRequest product : products) {
                ProductTopping topping = new ProductTopping();
                topping.setProductId(id);
                topping.setToppingId(product.getId());
                topping.setToppingGroupId(toppingGroupId);
                productToppings.add(topping);
            }
        }
        productToppingRepository.saveAll(productToppings);
    }
}
