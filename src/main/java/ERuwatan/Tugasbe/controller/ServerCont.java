package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.model.ChangePassword;
import ERuwatan.Tugasbe.model.UserModel;
import ERuwatan.Tugasbe.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerCont {

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @PostMapping("/ubah-password")
    public ResponseEntity<String> ubahPassword(@RequestBody ChangePassword request) {
        String passwordLama = request.getPasswordLama();
        String passwordBaru = request.getPasswordBaru();
        String konfirmasiPassword = request.getKonfirmasiPassword();

        // Mendapatkan username dari user yang sedang login
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal instanceof UserDetails ? ((UserDetails) principal).getUsername() : principal.toString();

        UserModel user = userDetailsService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pengguna tidak ditemukan");
        }

        if (!userDetailsService.checkPassword(user, passwordLama)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password lama tidak sesuai");
        }

        if (!passwordBaru.equals(konfirmasiPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Konfirmasi password tidak sesuai");
        }

        userDetailsService.changePassword(user, passwordBaru);

        return ResponseEntity.ok("Password berhasil diubah");
    }
}
