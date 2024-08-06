package vn.eledevo.vksbe.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.io.Serial;

import lombok.Getter;

@Getter
public class ApiException extends Exception {
    @Serial
    private static final long serialVersionUID = 1651914954615L;

    private final int code;

    public ApiException(String message) {
        super(message);
        this.code = BAD_REQUEST.value();
    }

    public ApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.code = BAD_REQUEST.value();
    }

    public ApiException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
