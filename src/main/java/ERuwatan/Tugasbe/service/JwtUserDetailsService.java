package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.UserDTO;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userDao;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    public UserModel save(UserDTO user) {
        try {
            if ((user.getUsername() != null && !user.getUsername().isEmpty()) || (user.getEmail() != null && !user.getEmail().isEmpty())) {
                if (userDao.findByUsername(user.getUsername()) == null && userDao.findByEmail(user.getEmail()) == null) {
                    UserModel newUser = new UserModel();
                    newUser.setUsername(user.getUsername());
                    newUser.setEmail(user.getEmail());
                    newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
                    newUser.setRole(user.getRole());
                    return userDao.save(newUser);
                } else {
                    throw new IllegalArgumentException("Username or email is already in use");
                }
            } else {
                throw new IllegalArgumentException("Username or email is required");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Registration failed: " + e.getMessage());
        }
    }

    public List<UserModel> findAllUsers() {
        return userDao.findAll();
    }

    public void deleteUser(long userId) {
        userDao.deleteById(userId);
    }

    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        UserModel user = userDao.findByUsername(usernameOrEmail);
        if (user == null) {
            user = userDao.findByEmail(usernameOrEmail);
        }
        if (user != null) {
            List<SimpleGrantedAuthority> roles = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase()));
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), roles);
        } else {
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
        }
    }
}