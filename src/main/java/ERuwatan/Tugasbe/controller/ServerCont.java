package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.model.ChangePassword;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServerCont {

    @PostMapping("/ubah-password")
    public ResponseEntity<String> ubahPassword(@RequestBody ChangePassword request) {
        Long id = request.getId();
        String passwordLama = request.getPasswordLama();
        String passwordBaru = request.getPasswordBaru();
        String konfirmasiPassword = request.getKonfirmasiPassword();

        if (passwordLama == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password lama tidak sesuai");
        }

        if (!passwordBaru.equals(konfirmasiPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Konfirmasi password tidak sesuai");
        }

        return ResponseEntity.ok("Password berhasil diubah");
    }
}