package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.inventory.InventoryCommonStats;
import vn.softdreams.easypos.dto.product.*;
import vn.softdreams.easypos.dto.productGroup.ProductOfflineResponse;

import java.util.List;
import java.util.Set;

public interface ProductRepositoryCustom {
    boolean countByIdAndComId(Integer id, Integer companyId, String code2);
    boolean checkDuplicateBarcode(Integer id, Integer companyId, Set<String> barcode);
    List<ProductOfflineResponse> findAllByProductGroupIds(List<Integer> ids, Integer companyId);

    List<ProductCheckBill> productCheckBill(Integer comId, Set<Integer> ids);

    List<ProductItemResponse> getAllForOffline(Integer companyId);
    List<ProductDetailResponse> getAllForOfflineForProduct(Integer companyId);
    List<ProductItemResponse> getWithPaging2(Pageable pageable, Integer companyId, List<Integer> groupIds, String keyword);

    ProductDetailResponse findByBarcode(Integer companyId, String barcode);
    ProductItemResponse findByBarcodeForBill(Integer companyId, String barcode);
    ProductItem findByProductId(Integer companyId, Integer productId);
    Page<ProductItemResponse> getWithPaging(
        Pageable pageable,
        Integer companyId,
        Integer groupId,
        String keyword,
        Boolean isTopping,
        Boolean isCountAll
    );
    Page<ProductDetailResponse> getWithPagingForProduct(
        Pageable pageable,
        Integer companyId,
        Integer groupId,
        String keyword,
        Boolean isTopping,
        Boolean isCountAll,
        List<Integer> productIds,
        boolean paramCheckAll,
        List<Integer> ids
    );
    ProductItem findByProductIdAdmin(Integer productId);
    boolean checkProductExistInInvoice(Integer productId, Integer comId);
    List<InventoryCommonStats.InventoryCommonStatsDetail> inventoryCommonStatsV2(Integer comId, Integer groupId, String keyword);
    List<ProductProfitResult> getProductProfit(Integer comId, String fromDate, String toDate, Pageable pageable, Integer type);
    List<ProductProfitResult> getProductRevenueStats(Integer comId, String fromDate, String toDate, Integer type, List<Integer> productIds);
    List<ProductProfitResult> getProductCostStats(
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        Integer businessTypeId,
        List<Integer> productIds
    );
    List<ProductToppingItem> findForToppingGroupDetail(Integer comId, List<Integer> ids, Boolean isGetTopping);
    List<ProductItemResponse> getDetailsByIdsOrGroupIds(Integer comId, List<Integer> ids, List<Integer> groupIds);
}
