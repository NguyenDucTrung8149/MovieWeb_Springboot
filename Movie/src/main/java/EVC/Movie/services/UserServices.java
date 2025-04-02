package EVC.Movie.services;


import EVC.Movie.entity.Movie;
import EVC.Movie.entity.User;
import EVC.Movie.repository.IRoleRepository;
import EVC.Movie.repository.IUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class UserServices {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    public void update(User user) {
        if (!user.getPassword().startsWith("$2a$")) { // Giả sử bạn đang sử dụng bcrypt
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        user.setId(user.getId());
        userRepository.save(user);
    }
    public void save(User user) {
        if (!user.getPassword().startsWith("$2a$")) { // Giả sử bạn đang sử dụng bcrypt
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        Long userId = userRepository.getUserIdByUserName(user.getUserName());
        Long roleId = roleRepository.getRoleIdByName("USER");
        if(roleId != 0 && userId !=0) {
            userRepository.addRoleToUser(userId, roleId);
        }
    }
    public void saveAdmin(User user) {
        if (!user.getPassword().startsWith("$2a$")) { // Giả sử bạn đang sử dụng bcrypt
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        Long userId = userRepository.getUserIdByUserName(user.getUserName());
        Long roleId = roleRepository.getRoleIdByName("ADMIN");
        if(roleId != 0 && userId != 0) {
            userRepository.addRoleToUser(userId, roleId);
        }
    }
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public long countByRoleName(String roleName) {
        return userRepository.countByRoleName(roleName);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
    public User findByIdWithRoleUser(Long id) {
        return userRepository.findByIdWithRoleName(id);
    }

    public Page<User> findByRoleNameAndUsername(String roleName, String username, Pageable pageable) {
        if (StringUtils.hasText(username)) {
            return userRepository.findByRoleNameAndUsernameContainingIgnoreCase(roleName, username, pageable);
        } else {
            return userRepository.findByRoleName(roleName, pageable);
        }
    }

    public Page<User> findUserByRoleName (String roleName, Pageable pageable) {
        return userRepository.findByRoleName(roleName, pageable);
    }
    public List<User> findUserByRoleName (String roleName) {
        return userRepository.findByRoleName(roleName);
    }

    @Transactional
    public User lockUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setLocked(true);
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    public User unlockUser(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user != null) {
            user.setLocked(false);
            userRepository.save(user);
        }
        return user;
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean checkOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
    public User findbyEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findFirstbyEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }
    public User findbyEmailWithOauth(String email) {
        return userRepository.findFirstByEmailAndIsOauthFalse(email);
    }
    public List<User> findUsersByFavoriteMovie(Movie movie) {
        return userRepository.findUsersByFavoriteMovie(movie.getMovieId());
    }

    @Transactional
    public void createNewOAuthUser(String email, String name, String imageUrl) {
        User existingUser = userRepository.findByUsername(email);

        if (existingUser != null) {
            if (!existingUser.isOauth()) {
                existingUser.setId(existingUser.getId());
                existingUser.setOauth(true);
                userRepository.save(existingUser);
            }
        } else {
            User newUser = new User();
            newUser.setUserName(email);
            newUser.setEmail("");
            newUser.setName(name);
            newUser.setImageUrl("/staticImg/anh-mau-den-1.jpg");
            newUser.setPassword(email);
            newUser.setPhone("0123456789");
            newUser.setTuoi(0);
            newUser.setOauth(true);
            newUser.setLocked(false);

            save(newUser);
        }
    }
}
