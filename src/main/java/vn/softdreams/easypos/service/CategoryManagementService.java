package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.dto.productGroup.*;
import vn.softdreams.easypos.dto.productProductUnit.GetByIdsRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

public interface CategoryManagementService {
    /**
     * Save a Category.
     *
     * @param Category the entity to save.
     * @return the persisted entity.
     */
    ResultDTO save(SaveProductGroupRequest Category);

    /**
     * Updates a productGroup.
     *
     * @param Category the entity to update.
     * @return the persisted entity.
     */
    ResultDTO update(UpdateProductGroupRequest saveProductGroupDTO);

    /**
     * Delete the "id" productGroup.
     *
     * @param id the id of the entity.
     */
    ResultDTO delete(ProductGroupDeleteRequest request);
    ResultDTO deleteList(DeleteProductList request);

    /**
     * Search productGroup by keyword
     *
     * @param keyword  the keyword that need to be searched for.
     * @param pageable the pagination information.
     */
    ResultDTO searchProductGroup(String name, Pageable pageable, Boolean isCountAll);

    ResultDTO getProductGroupDetail(Integer id);

    ResultDTO getAllByProductId(Integer id);

    ResultDTO getAllProductGroupsForOffline();

    ResultDTO importExel(MultipartFile file, Integer comId);

    ResultDTO validateImportExcel(MultipartFile file, Integer comId, Integer indexSheet);

    ResultDTO saveDataImportExcel(ProductGroupExcelRequest request);

    ResultDTO exportErrorData(List<ProductGroupValidateResponse> request);

    ResultDTO getByIds(GetByIdsRequest request);
}
