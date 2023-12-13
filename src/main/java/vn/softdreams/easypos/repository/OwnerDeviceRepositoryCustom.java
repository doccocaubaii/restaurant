package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.ownerDevice.OwnerDeviceResult;

public interface OwnerDeviceRepositoryCustom {
    Page<OwnerDeviceResult> getWithPaging(Pageable pageable, Integer ownerId, String keyword, String fromDate, String toDate);
}
