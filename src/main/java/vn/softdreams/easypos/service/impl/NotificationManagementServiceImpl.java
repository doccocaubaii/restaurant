package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Notification;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.notification.NotificationItemResponse;
import vn.softdreams.easypos.dto.notification.UpdateNotificationStatus;
import vn.softdreams.easypos.repository.NotificationRepository;
import vn.softdreams.easypos.service.NotificationManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.MoneyManagementResource;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class NotificationManagementServiceImpl implements NotificationManagementService {

    private final Logger log = LoggerFactory.getLogger(MoneyManagementResource.class);
    private final String ENTITY_NAME = "notification";
    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationManagementServiceImpl(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    @Override
    public ResultDTO getWithPaging(Pageable pageable, Boolean getAll, Integer type) {
        User user = userService.getUserWithAuthorities();
        Page<NotificationItemResponse> page = notificationRepository.getWithPaging(
            pageable,
            getAll,
            user.getCompanyId(),
            user.getId(),
            type
        );
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_GET_LIST,
            true,
            page.getContent(),
            (int) page.getTotalElements()
        );
    }

    @Override
    public ResultDTO countUnReadNotification() {
        User user = userService.getUserWithAuthorities();
        Integer count = notificationRepository.countAllByComIdAndIsRead(user.getCompanyId(), Boolean.FALSE, user.getId());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, count, count);
    }

    @Override
    public ResultDTO updateStatus(UpdateNotificationStatus request) {
        User user = userService.getUserWithAuthorities();
        if (request.getId() != null) {
            Optional<Notification> optionalNotification = notificationRepository.findByIdAndComId(request.getId(), user.getCompanyId());
            if (optionalNotification.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.NOTIFICATION_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.NOTIFICATION_NOT_FOUND
                );
            }
            Notification notification = optionalNotification.get();
            notification.setIsRead(Boolean.TRUE);
        } else if (request.getReadAll()) {
            List<Notification> notifications = notificationRepository.getAllUnReadNotification(user.getCompanyId(), user.getId());
            for (Notification notification : notifications) {
                notification.setIsRead(Boolean.TRUE);
            }
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_UPDATE, true);
    }
}
