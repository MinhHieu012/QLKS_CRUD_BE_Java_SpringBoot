package vn.eledevo.vksbe.constant;

import static org.springframework.http.HttpStatus.*;

import org.springframework.http.HttpStatusCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(INTERNAL_SERVER_ERROR, 9999, "Lỗi Server, vui lòng thử lại sau!"),
    FIELD_INVALID(UNPROCESSABLE_ENTITY, 1001, "Các trường không hợp lệ!"),
    METHOD_ERROR(METHOD_NOT_ALLOWED, 1002, "Phương thức không hợp lệ!");

    ErrorCode(HttpStatusCode statusCode, int code, String message) {
        this.statusCode = statusCode;
        this.code = code;
        this.message = message;
    }

    private HttpStatusCode statusCode;
    private int code;
    private String message;
}
