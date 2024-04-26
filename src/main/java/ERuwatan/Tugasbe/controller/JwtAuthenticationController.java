package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.config.JwtTokenUtil;
import ERuwatan.Tugasbe.dto.JwtRequest;
import ERuwatan.Tugasbe.dto.JwtResponse;
import ERuwatan.Tugasbe.dto.UserDTO;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.UserRepository;
import ERuwatan.Tugasbe.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userDao;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        String usernameOrEmail = authenticationRequest.getUsernameOrEmail();
        String password = authenticationRequest.getPassword();

        UserModel user = userDao.findByUsername(usernameOrEmail);
        if (user == null) {
            user = userDao.findByEmail(usernameOrEmail);
        }

        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        authenticate(user.getUsername(), password);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        String role = user.getRole().toLowerCase();
        String redirectUrl = role.equals("murid") ? "/dashboard_murid" : (role.equals("guru") ? "/dashboard_guru" : "/dashboard_default");

        return ResponseEntity.ok(new JwtResponse(token, user, redirectUrl));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        try {
            if ((userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) || (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty())) {
                UserModel newUser = userDetailsService.save(userDTO);
                return ResponseEntity.ok("User registered successfully");
            } else {
                throw new IllegalArgumentException("Username or email is required");
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Registration failed: " + e.getMessage()));
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}