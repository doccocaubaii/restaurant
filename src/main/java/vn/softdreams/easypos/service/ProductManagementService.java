package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.dto.backup.ImportUrlRequest;
import vn.softdreams.easypos.dto.product.*;
import vn.softdreams.easypos.dto.productProductUnit.GetByIdsRequest;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.service.dto.ResultDTO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProductManagementService {
    /**
     * Delete the "id" product.
     *
     * @param id the id of the entity.
     */
    ResultDTO delete(Integer id);
    ResultDTO deleteList(DeleteProductList req);

    ResultDTO save(MultipartFile images, SaveProductRequest product, HttpServletRequest httpRequest, Boolean isActionAdmin);

    ResultDTO getProductDetail(Integer id);

    ResultDTO getAllForOffline();
    ResultDTO getAllForOfflineForProduct();

    ResultDTO getWithPaging(Pageable pageable, String keyword, Integer productGroupId, Boolean isTopping, Boolean isCountAll);
    ResultDTO getWithPaging2(Pageable pageable, String keyword, List<Integer> groupIds);
    ResultDTO getWithPagingForProduct(
        Pageable pageable,
        String keyword,
        Integer productGroupId,
        Boolean isTopping,
        Boolean isCountAll,
        List<Integer> ids
    );

    ResultDTO update(MultipartFile images, UpdateProdRequest productRequest, HttpServletRequest httpRequest, Boolean isActionAdmin);

    ResultDTO getAllProductUnit(String keyword);

    ResultDTO findByBarcode(String barcode);
    ResultDTO findByBarcodeForBill(String barcode);

    ResultDTO importUrl(List<ImportUrlRequest> importUrlRequests);

    ResultDTO importUrlByTaxCode(List<ImportUrlRequest> importUrlRequests);

    ResultDTO createNewProductUnit(CreateProductUnitRequest request);
    ResultDTO deleteProductUnit(DeleteConversionUnitRequest request, Boolean isActionAdmin);

    ResultDTO updateProductAsync(List<Integer> companyIds, EB88ApiClient eb88ApiClient);

    ResultDTO importProductByExcel(MultipartFile file, Integer comId);

    ResultDTO importProductByExcel1(MultipartFile file, Integer comId);

    ResultDTO reSendQueue(List<Integer> ids);
    ResultDTO enableInventoryTracking(ProductStockUpdateRequest request);

    ResultDTO validateImportExcel(MultipartFile file, Integer comId, Integer indexSheet);

    ResultDTO saveDataImportExcel(ProductExcelRequest request);

    void syncDataAfterImport(ImportProductAsyncRequest request);

    ResultDTO exportErrorData(List<ProductExcelResponse> request);

    ResultDTO searchProductUnit(Pageable pageable, String keyword);
    ResultDTO getDetailProductUnit(Integer id);
    ResultDTO deleteProductUnit(Integer id);
    ResultDTO updateProductUnit(UpdateProductUnitRequest request);
    ResultDTO deleteListProductUnit(DeleteProductList request);
    ResultDTO insertDefaultProduct();
    ResultDTO checkBarCode(String barcode);
    ResultDTO alterImage(List<String> images);
    ResultDTO getByProductProductUnitIds(GetByIdsRequest request);
}
