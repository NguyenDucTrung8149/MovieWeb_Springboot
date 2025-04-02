package EVC.Movie.repository;

import EVC.Movie.entity.ForgotPassword;
import EVC.Movie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface IForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    @Query("SELECT fp from ForgotPassword  fp where fp.otp = ?1 and fp.user = ?2")
    ForgotPassword findByOtpAndUser(Integer otp, User user);

    ForgotPassword findByUser(User user);

    @Modifying
    @Transactional
    @Query("DELETE FROM ForgotPassword fp WHERE fp.id = :id")
    void deletePass(@Param("id") Long id);
}
