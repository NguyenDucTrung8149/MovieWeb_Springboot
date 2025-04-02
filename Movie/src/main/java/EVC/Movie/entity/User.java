package EVC.Movie.entity;


import EVC.Movie.validator.OnCreate;
import EVC.Movie.validator.ValidEmail;
import EVC.Movie.validator.ValidUsername;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username",length = 50, nullable = false,unique = true)
    @NotBlank(message = "Ten dang nhap khong duoc de trong")
    @Size(max = 50,message = "Ten dang nhap phai it hon 50 ki tu")
    @Pattern(regexp = "^\\p{ASCII}+$", message = "Tên đăng nhập không được chứa dấu hoặc ký tự đặc biệt")
    @ValidUsername
    private String userName;

    @Column(name = "password",length = 250, nullable = false)
    @NotBlank(message = "Mat khau khong duoc de trong")
    private String password;

    @Column(name = "image")
    private String imageUrl;

    @Transient
    private String oldPassword;

    @Transient
    private String confirmPassword;

    @Column(name="email",length = 50)
    @Size(max = 50,message = "Email phai it hon 50 ky tu")
    @ValidEmail
    private String email;

    @Column(name = "name",length = 50, nullable = false)
    @Size(max = 50 ,message = "Ten cua ban phai it hon 50 ky tu")
    @NotBlank(message = "Ten khong duoc de trong")
    private String name;

    @Column(name = "phone",length = 50, nullable = true)
    @Size(max = 10,min = 10,message = "SDT cua ban phai du 10 ky tu")
    private String phone;

    @Column(name = "tuoi")
    @NotNull(message = "Tuoi khong duoc de trong")
    private int tuoi;

    @Column(name = "PremiumDuration")
    @NotNull
    private LocalDateTime PremiumDuration = LocalDateTime.of(0, 1, 1, 0, 0);

    @Column(name = "is_locked")
    private boolean isLocked = false;

    @Column(name = "is_oauth")
    private boolean isOauth = false;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Comment> comments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LoveList> loveLists;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Recomment> recomments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<History> histories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonBackReference
    private List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @EqualsAndHashCode.Exclude
    private ForgotPassword forgotPassword;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PaymentHistory> paymentHistories;
}

