package ERuwatan.Tugasbe.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class ChangePassword {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String passwordLama;
    private String passwordBaru;

    public String getPasswordLama() {
        return passwordLama;
    }

    public void setPasswordLama(String passwordLama) {
        this.passwordLama = passwordLama;
    }

    public String getPasswordBaru() {
        return passwordBaru;
    }

    public void setPasswordBaru(String passwordBaru) {
        this.passwordBaru = passwordBaru;
    }

    public String getKonfirmasiPassword() {
        return konfirmasiPassword;
    }

    public void setKonfirmasiPassword(String konfirmasiPassword) {
        this.konfirmasiPassword = konfirmasiPassword;
    }

    private String konfirmasiPassword;

}