package ERuwatan.Tugasbe.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {
    public static <T> CommonResponse<T> ok(T data) {
        CommonResponse<T> response = new CommonResponse<T>();
        response.setMessage("Success");
        response.setStatus("200");
        response.setData(data);
        return response;
    }

    public static <T> ResponseEntity<CommonResponse<T>> error(String eror, HttpStatus httpStatus) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setStatus(String.valueOf(httpStatus.value()));
        response.setMessage(httpStatus.name());
        response.setData((T) eror);
        return new ResponseEntity<>(response, httpStatus);
    }

}


