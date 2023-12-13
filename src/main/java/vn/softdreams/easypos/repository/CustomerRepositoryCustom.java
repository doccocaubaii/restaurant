package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.customer.CustomerItemResult;
import vn.softdreams.easypos.dto.customer.CustomerResponse;

import java.util.List;

public interface CustomerRepositoryCustom {
    List<CustomerResponse> getAllForOffline(Integer companyId, Integer active, Integer type);

    Page<CustomerResponse> getAllWithPaging(
        Pageable pageable,
        String keyword,
        Integer companyId,
        Integer active,
        Integer type,
        boolean isCountAll,
        Boolean isHiddenDefault,
        boolean paramCheckAll,
        List<Integer> ids
    );

    Page<CustomerItemResult> getAllCustomerItem(Integer comId, Pageable pageable, String keyword);
}
