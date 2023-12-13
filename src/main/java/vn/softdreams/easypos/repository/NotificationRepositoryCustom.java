package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.notification.NotificationItemResponse;

public interface NotificationRepositoryCustom {
    Page<NotificationItemResponse> getWithPaging(Pageable pageable, Boolean getAll, Integer comId, Integer userId, Integer type);
}
