package vn.hust.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.hust.restaurant.domain.Otp;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Integer> {

    Optional<Otp> findFirstByUserIdAndAndTypeAndAndStatus(Integer userId, Integer type, Integer status);

    @Modifying
    @Query(value = "update OTP set status = 0 where user_id = ?1 and type = ?2 and status = 1", nativeQuery = true)
    void resetOtp(Integer userId, Integer type);
}
