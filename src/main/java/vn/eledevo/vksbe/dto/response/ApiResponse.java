package vn.eledevo.vksbe.dto.response;

import static org.springframework.http.HttpStatus.OK;

import org.springframework.http.ResponseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
    int code;
    String message;
    T result;

    public ApiResponse() {
        this.code = OK.value();
        this.message = OK.getReasonPhrase();
    }

    public ApiResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResponse(int code, String message, T result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public static <T> ApiResponse<T> ok(T body) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setResult(body);
        return response;
    }

    public static <T> ResponseEntity<ApiResponse<T>> okEntity(T body) {
        return ResponseEntity.ok(ok(body));
    }
}
