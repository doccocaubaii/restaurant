package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.printSetting.PrintSettingItemResponse;

public interface PrintSettingRepositoryCustom {
    Page<PrintSettingItemResponse> getWithPaging(Pageable pageable, Integer comId, Integer userId, String keyword);
}
