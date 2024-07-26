package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelUserSer;
import ERuwatan.Tugasbe.config.JwtTokenUtil;
import ERuwatan.Tugasbe.dto.JwtRequest;
import ERuwatan.Tugasbe.dto.JwtResponse;
import ERuwatan.Tugasbe.dto.UserDTO;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.UserRepository;
import ERuwatan.Tugasbe.service.JwtUserDetailsService;
import com.google.api.client.util.Value;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userDao;
    @Autowired
    private ExcelUserSer excelUserSer;

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

        return ResponseEntity.ok(new JwtResponse(token, user));
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody UserDTO userDTO) {
        try {
            if ((userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) || (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty())) {
                UserModel newUser = userDetailsService.save(userDTO);
                List<UserModel> users = userDao.findAll();
                return ResponseEntity.status(HttpStatus.CREATED).body(users);
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

    @GetMapping("/users")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userDao.findAll();
        users.forEach(user -> user.setPassword(""));
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable("id") long userId, @RequestBody UserDTO userDTO) {
        try {
            UserModel updatedUser = userDetailsService.updateUser(userId, userDTO);
            if (updatedUser != null) {
                updatedUser.setPassword("");
                return ResponseEntity.ok(updatedUser);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to update user: " + e.getMessage()));
        }
    }

    @GetMapping("/users/by-id/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") long userId) {
        try {
            UserModel user = userDao.findById(userId);
            if (user != null) {
                user.setPassword("");
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to retrieve user: " + e.getMessage()));
        }
    }

    @DeleteMapping("/users/hapus/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long userId) {
        try {
            userDetailsService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Failed to delete user: " + e.getMessage()));
        }
    }

    @PostMapping("/upload/image/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable Long id, @RequestPart("image") MultipartFile image) {
        try {
            UserModel updatedUser = userDetailsService.uploadImage(id, image);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/edit/image/{id}")
    public ResponseEntity<?> updateImage(@PathVariable Long id, @RequestPart("image") MultipartFile image) {
        try {
            UserModel updatedUser = userDetailsService.uploadImage(id, image);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/upload/image_admin/{id}")
    public ResponseEntity<?> uploadImageAdmin(@PathVariable Long id, @RequestPart("imageAdmin") MultipartFile image) {
        try {
            UserModel updatedUser = userDetailsService.uploadImageAdmin(id, image);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/edit/image_admin/{id}")
    public ResponseEntity<?> updateImageAdmin(@PathVariable Long id, @RequestPart("imageAdmin") MultipartFile image) {
        try {
            UserModel updatedUser = userDetailsService.uploadImageAdmin(id, image);
            return ResponseEntity.ok(updatedUser);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @GetMapping("/upload/export")
    public void exportGuru(
            HttpServletResponse response) throws IOException, NotFoundException {
        excelUserSer.excelExportGuru(response);
    }

    @GetMapping("/download/template")
    public void excelDownloadGuruTemplate(HttpServletResponse response) throws IOException {
        excelUserSer.excelDownloadGuruTemplate(response);
    }

    @PostMapping("/upload/import")
    public ResponseEntity<String> importSiswaFromExcel(@RequestPart("file") MultipartFile file) {
        try {
            Map<String, String> passwordMap = excelUserSer.importGuruFromExcel(file);
            if (!passwordMap.isEmpty()) {
                // Format the success message to include generated passwords
                StringBuilder successMsg = new StringBuilder("Import successful. Generated passwords for the following users: ");
                passwordMap.forEach((username, password) ->
                        successMsg.append("Username '").append(username).append("' with generated password '").append(password).append("'. ")
                );
                return new ResponseEntity<>(successMsg.toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Import successful, no new users with generated passwords.", HttpStatus.OK);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
