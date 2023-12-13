package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.domain.Customer;
import vn.softdreams.easypos.dto.customer.CustomerCreateRequest;
import vn.softdreams.easypos.dto.customer.CustomerUpdateCardRequest;
import vn.softdreams.easypos.dto.customer.CustomerUpdateRequest;
import vn.softdreams.easypos.dto.customer.CustomerValidateResponse;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

public interface CustomerManagementService {
    ResultDTO getAllWithPaging(
        Pageable pageable,
        String keyword,
        Integer type,
        boolean isCountAll,
        Boolean isHiddenDefault,
        boolean paramCheckAll,
        List<Integer> ids
    );

    ResultDTO getAllForOffline(Integer type);

    ResultDTO create(CustomerCreateRequest customerRequest);

    ResultDTO update(CustomerUpdateRequest customerRequest);

    ResultDTO deleteCustomer(Integer id);
    ResultDTO deleteListCustomer(DeleteProductList req);

    ResultDTO findById(Integer id);

    ResultDTO importExel(MultipartFile file, Integer comId);

    ResultDTO validateImportExcel(MultipartFile file, Integer comId, Integer indexSheet);

    ResultDTO saveDataImportExcel(List<CustomerCreateRequest> request);
    void syncDataAfterImport(List<Customer> request);

    ResultDTO exportErrorData(List<CustomerValidateResponse> request);

    ResultDTO getDataByTaxCode(String taxCode);

    ResultDTO updateCard(CustomerUpdateCardRequest request);
}
