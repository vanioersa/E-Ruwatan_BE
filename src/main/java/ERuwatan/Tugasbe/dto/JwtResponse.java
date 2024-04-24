package ERuwatan.Tugasbe.dto;

import ERuwatan.Tugasbe.model.UserModel;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private final UserModel userData;
    private final String redirectUrl;

    public JwtResponse(String jwttoken, UserModel userData, String redirectUrl) {
        this.jwttoken = jwttoken;
        this.userData = userData;
        this.redirectUrl = redirectUrl;
    }

    public UserModel getUserData() {
        return userData;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
