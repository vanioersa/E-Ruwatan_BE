package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.dto.UserDTO;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.repository.UserRepository;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.*;
import javassist.NotFoundException;
import org.apache.xpath.operations.Mult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    static final String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/e-ruwatan-c9706.appspot.com/o/%s?alt=media";

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
                    newUser.setAlamat(user.getAlamat());
                    newUser.setGender(user.getGender());
                    newUser.setTelepon(user.getTelepon());
                    newUser.setStatus_nikah(user.getStatus_nikah());
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

    public UserModel updateUser(long userId, UserDTO userDTO) {
        try {
            UserModel user = userDao.findById(userId);
            if (user != null) {
                user.setUsername(userDTO.getUsername());
                user.setEmail(userDTO.getEmail());
                if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                    user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
                }
                user.setRole(userDTO.getRole());
                user.setAlamat(userDTO.getAlamat());
                user.setGender(userDTO.getGender());
                user.setTelepon(userDTO.getTelepon());
                user.setStatus_nikah(userDTO.getStatus_nikah());
                return userDao.save(user);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update user: " + e.getMessage());
        }
    }
    public UserModel ubahUser(long id, UserDTO userDTO , MultipartFile image) {
        try {
            UserModel user = userDao.findById(id);
            if (user != null) {
                user.setUsername(userDTO.getUsername());
                user.setEmail(userDTO.getEmail());
                if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                    user.setPassword(bcryptEncoder.encode(userDTO.getPassword()));
                }
                user.setRole(userDTO.getRole());
                user.setAlamat(userDTO.getAlamat());
                user.setGender(userDTO.getGender());
                user.setTelepon(userDTO.getTelepon());
                user.setStatus_nikah(userDTO.getStatus_nikah());
                String fileImage = uploadFoto(image , "E-ruwatan-user" + id);
                user.setImage(fileImage);
                return userDao.save(user);
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to update user: " + e.getMessage());
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

    public UserModel uploadImage(Long id , MultipartFile image ) throws NotFoundException, IOException {
        UserModel userModelOptional = userDao.findById(id)
                .orElseThrow(()  -> new ERuwatan.Tugasbe.exception.NotFoundException("Id tidak ditemukan"));

        String fileUrl = uploadFoto(image , "fotoRoleGuru_" + id);
        userModelOptional.setImage(fileUrl);

        return userDao.save(userModelOptional);
    }
    public UserModel uploadImageAdmin(Long id , MultipartFile image ) throws NotFoundException, IOException {
        UserModel userModelOptional = userDao.findById(id)
                .orElseThrow(()  -> new ERuwatan.Tugasbe.exception.NotFoundException("Id tidak ditemukan"));

        String fileUrl = uploadFoto(image , "fotoRoleAdmin_" + id);
        userModelOptional.setImage(fileUrl);

        return userDao.save(userModelOptional);
    }

    private String uploadFoto(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "user/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("e-ruwatan-c9706.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

    private String uploadFotoAdmin(MultipartFile multipartFile, String fileName) throws IOException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String folderPath = "admin/";
        String fullPath = folderPath + timestamp + "_" + fileName;
        BlobId blobId = BlobId.of("e-ruwatan-c9706.appspot.com", fullPath);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        Credentials credentials = GoogleCredentials.fromStream(new FileInputStream("./src/main/resources/FirebaseConfig.json"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, multipartFile.getBytes());
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fullPath, StandardCharsets.UTF_8));
    }

    public boolean checkPassword(UserModel user, String rawPassword) {
        return bcryptEncoder.matches(rawPassword, user.getPassword());
    }

    public void changePassword(UserModel user, String newPassword) {
        user.setPassword(bcryptEncoder.encode(newPassword));
        userDao.save(user);
    }

    public UserModel findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}