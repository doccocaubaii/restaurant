package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.employee.EmployeeCreateRequest;
import vn.softdreams.easypos.dto.employee.EmployeeUpdateRequest;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface EmployerManagementService {
    ResultDTO getAllEmployeeWithPaging(String keyword, Pageable pageable, Boolean isCountAll);

    ResultDTO getEmployeeById(Integer id);

    ResultDTO update(EmployeeUpdateRequest request);

    ResultDTO delete(Integer id);

    ResultDTO create(EmployeeCreateRequest request);

    ResultDTO getAllRoles(Integer comId);

    ResultDTO deleteList(DeleteProductList req);
}
