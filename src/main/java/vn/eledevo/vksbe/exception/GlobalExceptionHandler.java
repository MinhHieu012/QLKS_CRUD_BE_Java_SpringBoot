package vn.eledevo.vksbe.exception;

import static org.springframework.http.HttpStatus.*;
import static vn.eledevo.vksbe.constant.ErrorCode.FIELD_INVALID;
import static vn.eledevo.vksbe.constant.ErrorCode.METHOD_ERROR;

import java.lang.reflect.UndeclaredThrowableException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import jakarta.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import vn.eledevo.vksbe.constant.ErrorCode;
import vn.eledevo.vksbe.dto.response.ApiResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends Throwable {

    private ResponseEntity<Object> generateExceptionResponse(int code, String message, Exception ex) {
        if (code == INTERNAL_SERVER_ERROR.value()) log.error(ex.getMessage(), ex);
        return ResponseEntity.status(OK).body(new HashMap<String, Object>() {
            {
                put("message", message);
                put("code", code);
            }
        });
    }

    private ResponseEntity<Object> generateExceptionResponse(ErrorCode errorCode, Exception ex) {
        if (errorCode == ErrorCode.UNCATEGORIZED_EXCEPTION) log.error(ex.getMessage(), ex);
        return ResponseEntity.status(errorCode.getStatusCode()).body(new HashMap<String, Object>() {
            {
                put("message", errorCode.getMessage());
                put("code", errorCode.getCode());
            }
        });
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handlerAccessServiceException(ApiException ex) {
        return generateExceptionResponse(ex.getCode(), ex.getMessage(), ex);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        Map<String, String> errors = ex.getErrors();
        ApiResponse<Map<String, String>> response =
                new ApiResponse<>(FIELD_INVALID.getCode(), FIELD_INVALID.getMessage(), errors);
        return ResponseEntity.status(FIELD_INVALID.getStatusCode()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handlerException(Exception ex) {
        return generateExceptionResponse(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleNoSupportedException(HttpRequestMethodNotSupportedException ex) {
        return generateExceptionResponse(METHOD_ERROR, ex);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handlerNotReadable(Exception ex) {
        return generateExceptionResponse(NOT_ACCEPTABLE.value(), NOT_ACCEPTABLE.getReasonPhrase(), ex);
    }

    @ExceptionHandler(UndeclaredThrowableException.class)
    public ResponseEntity<Object> handlerThrowableException(Exception ex) {
        return generateExceptionResponse(INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.getReasonPhrase(), ex);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<Object> handlerBindException(Exception ex) {
        return generateExceptionResponse(BAD_REQUEST.value(), ex.getMessage(), ex);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<?> response = new ApiResponse<>(FIELD_INVALID.getCode(), FIELD_INVALID.getMessage(), errors);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse<?> response =
                new ApiResponse<>(UNPROCESSABLE_ENTITY.value(), UNPROCESSABLE_ENTITY.getReasonPhrase(), errors);
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleBadCredentials(BadCredentialsException ex) {
        return new ResponseEntity<>("Thông tin đăng nhập không chính xác!", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<String> handleAccessDenied(AuthorizationDeniedException ex) {
        return new ResponseEntity<>("Bạn không có quyền!", HttpStatus.FORBIDDEN);
    }
}
