package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Otp;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Otp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer> {
    Optional<Otp> findByUsernameAndOtpAndType(String username, String otp, Integer type);
    List<Otp> findByUsernameAndTypeAndStatus(String username, Integer type, Integer status);

    @Query(value = "SELECT COUNT(*) FROM otp WHERE create_time >= DATEADD(MINUTE, -30, GETDATE()) and username = ?1", nativeQuery = true)
    Integer countNumberRequest(String username);

    @Query(value = "select top(1) * from otp where username = ?1 and type = ?2 order by expired_time desc", nativeQuery = true)
    Optional<Otp> getLastUserOtp(String username, Integer type);
}
