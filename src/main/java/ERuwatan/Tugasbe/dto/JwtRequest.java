package ERuwatan.Tugasbe.dto;

import java.io.Serializable;

public class JwtRequest implements Serializable {

    private static final long serialVersionUID = 5926468583005150707L;

    private String usernameOrEmail;
    private String password;
    private String role;

    public JwtRequest() {}

    public JwtRequest(String usernameOrEmail, String password, String role) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
        this.role = role;
    }

    public String getUsernameOrEmail() {
        return this.usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}

