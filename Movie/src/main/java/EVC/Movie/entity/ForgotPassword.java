package EVC.Movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@Entity
@Data
@Table(name = "ForgotPassword")
public class ForgotPassword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "OTP cannot be null")
    @Column(name = "otp")
    private int otp;

    @NotNull(message = "Expired time cannot be null")
    @Column(name = "expiredTime")
    private Date expiredTime;


    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private User user;
}
