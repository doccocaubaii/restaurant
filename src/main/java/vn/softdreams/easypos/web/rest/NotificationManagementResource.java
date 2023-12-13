package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.notification.UpdateNotificationStatus;
import vn.softdreams.easypos.service.NotificationManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;

@RestController
@RequestMapping("/api")
public class NotificationManagementResource {

    private final Logger log = LoggerFactory.getLogger(MoneyManagementResource.class);
    private final String ENTITY_NAME = "notification";
    private final NotificationManagementService notificationManagementService;

    public NotificationManagementResource(NotificationManagementService notificationManagementService) {
        this.notificationManagementService = notificationManagementService;
    }

    @GetMapping("/client/page/notification/get-with-paging")
    public ResponseEntity<ResultDTO> getAllNotification(
        Pageable pageable,
        @RequestParam(required = false) Boolean isUnRead,
        @RequestParam(required = false) Integer type
    ) throws Exception {
        log.info("get all notification");
        ResultDTO result = notificationManagementService.getWithPaging(pageable, isUnRead, type);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/client/page/notification/count-unread")
    public ResponseEntity<ResultDTO> countAllUnRead() {
        log.info("count all unread notification");
        ResultDTO result = notificationManagementService.countUnReadNotification();
        return ResponseEntity.ok(result);
    }

    @PutMapping("/client/page/notification/update-status")
    public ResponseEntity<ResultDTO> readNotification(@RequestBody UpdateNotificationStatus req) throws Exception {
        log.info("update notification status: " + req);
        ResultDTO result = notificationManagementService.updateStatus(req);
        return ResponseEntity.ok(result);
    }
}
