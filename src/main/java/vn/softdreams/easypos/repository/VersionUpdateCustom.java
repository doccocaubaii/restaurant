package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.VersionUpdate;

public interface VersionUpdateCustom {
    Page<VersionUpdate> getWithPaging(Pageable pageable, Integer comId, String fromDate, String toDate);
}
