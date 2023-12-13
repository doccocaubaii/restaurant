package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.employee.EmployeeResponse;
import vn.softdreams.easypos.dto.user.UserResponse;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

public interface UserRepositoryCustom {
    String getSeqByCompanyAndCode(String seqName);

    ResultDTO autoGenSeqName(String seqName);

    UserResponse getUserResponse(Integer userId);

    Page<EmployeeResponse> findAllEmployeeByCompanyId(
        Integer companyId,
        Integer employeeId,
        String keyword,
        Pageable pageable,
        Boolean isCountAll,
        boolean paramCheckAll,
        List<Integer> ids
    );

    EmployeeResponse findOneByIdAndCompanyId(Integer id, Integer companyId);

    String genCodeByTableName(Integer comId, String tableName);
}
