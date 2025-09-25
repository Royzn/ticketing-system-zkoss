package latihan.service;

import latihan.entity.User;
import latihan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPasswordHash(),
                    Collections.singleton(authority)  // Wrap in singleton set
            );

        } catch (RuntimeException e) {
            e.printStackTrace();
            throw new RuntimeException("Invalid login");
        }
    }
}
