package EVC.Movie.repository;



import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.userName = ?1")
    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.email = :email AND u.email IS NOT NULL AND u.email <> ''")
    User findFirstByEmail(@Param("email") String email);


    User findByEmail(String email);
    User findFirstByEmailAndIsOauthFalse(String email);

    @Query("SELECT COUNT(u) FROM User u JOIN u.roles r WHERE r.name = ?1")
    long countByRoleName(String roleName);
    // them quyen cho nguoi dung
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role(user_id,role_id) VALUE (?1,?2)",nativeQuery = true)
    void addRoleToUser(long userId, long roleId);

    //Lay ID cua User bang Username
    @Query("SELECT u.id FROM User u WHERE u.userName=?1")
    Long getUserIdByUserName(String userName);

    //Lay danh sach quyen tu User Id
    @Query(value = "SELECT r.name FROM role r INNER JOIN user_role ur ON r.id = ur.role_id WHERE ur.user_id = ?1",nativeQuery = true)
    String[] getRoleOfUser (long userId);

    // Find users by role name
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    Page<User> findByRoleName(String roleName, Pageable pageable);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    List<User> findByRoleName(String roleName);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'USER' AND u.id = :id")
    User findByIdWithRoleName(@Param("id") Long id);
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1 AND u.userName LIKE %?2%")
    Page<User> findByRoleNameAndUsernameContainingIgnoreCase(String roleName, String username, Pageable pageable);

    @Query("SELECT u FROM User u JOIN u.loveLists l WHERE l.movie.movieId = :movieId")
    List<User> findUsersByFavoriteMovie(@Param("movieId") Long movieId);


}
