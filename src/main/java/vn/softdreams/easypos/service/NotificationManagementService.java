package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.notification.UpdateNotificationStatus;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface NotificationManagementService {
    ResultDTO getWithPaging(Pageable pageable, Boolean isUnRead, Integer type);
    ResultDTO countUnReadNotification();
    ResultDTO updateStatus(UpdateNotificationStatus request);
}
