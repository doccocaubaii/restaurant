package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Notification;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Notification entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer>, NotificationRepositoryCustom {
    Optional<Notification> findByIdAndComId(Integer id, Integer comId);

    @Query(
        value = "select n.* from notification n join notification_user nu on n.id = nu.notification_id where user_id = ?2 and com_id = ?1 and is_read = 0",
        nativeQuery = true
    )
    List<Notification> getAllUnReadNotification(Integer comId, Integer userId);

    @Query(
        value = "select count(*) from notification n join notification_user nu on n.id = nu.notification_id where com_id = ?1 and is_read = ?2 and user_id = ?3 and CAST(n.create_time as date) = CAST(GETDATE() as date) ",
        nativeQuery = true
    )
    Integer countAllByComIdAndIsRead(Integer comId, Boolean isRead, Integer userId);
}
