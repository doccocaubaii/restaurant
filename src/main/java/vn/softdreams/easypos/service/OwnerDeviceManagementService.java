package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface OwnerDeviceManagementService {
    ResultDTO getWithPaging(Pageable pageable, Integer ownerId, String keyword, String fromDate, String toDate);
}
