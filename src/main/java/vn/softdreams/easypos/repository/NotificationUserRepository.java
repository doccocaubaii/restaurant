package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.NotificationUser;

@SuppressWarnings("unused")
@Repository
public interface NotificationUserRepository extends JpaRepository<NotificationUser, Integer> {}
