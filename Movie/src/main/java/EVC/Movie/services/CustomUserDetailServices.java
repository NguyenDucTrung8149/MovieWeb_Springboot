package EVC.Movie.services;



import EVC.Movie.entity.CustomUserDetail;
import EVC.Movie.entity.User;
import EVC.Movie.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailServices implements UserDetailsService {
    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        if(user.isLocked()){
            throw new LockedException("Tài khoản của bạn đã bị khóa.");
        }
        return new CustomUserDetail(user,userRepository);
    }
}
