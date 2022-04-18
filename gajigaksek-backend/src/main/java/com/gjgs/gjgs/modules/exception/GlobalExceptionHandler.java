package com.gjgs.gjgs.modules.exception;

import com.gjgs.gjgs.modules.exception.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolationException;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String LOG_FORMAT = "Class : {}, Message : {}";
    private static final String LOG_CODE_FORMAT = "Class : {}, Code : {}, Message : {}";


    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e) {

        log.warn(LOG_CODE_FORMAT,
                e.getClass().getSimpleName(),
                ErrorCode.BINDING_EXCEPTION.getCode(),
                ErrorCode.BINDING_EXCEPTION.getMessage());

        ErrorResponse response = ErrorResponse.of(ErrorCode.BINDING_EXCEPTION.getMessage(),
                ErrorCode.BINDING_EXCEPTION.getCode(),
                e.getBindingResult());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }




    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity handleMissingParams(MissingRequestHeaderException e) {

        String name = e.getHeaderName();
        log.warn(LOG_CODE_FORMAT,
                e.getClass().getSimpleName(),
                ErrorCode.MISSING_REQUEST_HEADER.getCode(),
                ErrorCode.MISSING_REQUEST_HEADER.getMessage());

        if (name.equals("Authorization")) {
            ErrorResponse response = ErrorResponse.from(ErrorCode.NO_AUTHORIZATION_TOKEN);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (name.equals("KakaoAccessToken")) {
            ErrorResponse response = ErrorResponse.from(ErrorCode.NO_KAKAO_ACCESS_TOKEN);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        if (name.equals("RefreshToken")) {
            ErrorResponse response = ErrorResponse.from(ErrorCode.NO_REFRESH_TOKEN);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        ErrorResponse response = ErrorResponse.from(ErrorCode.MISSING_REQUEST_HEADER);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(AccessDeniedException e) {

        log.warn(LOG_CODE_FORMAT,
                e.getClass().getSimpleName(),
                ErrorCode.ACCESS_DENIED.getCode(),
                ErrorCode.ACCESS_DENIED.getMessage());

        ErrorResponse response = ErrorResponse.from(ErrorCode.ACCESS_DENIED);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);
    }

    /**
     * request body missing exception
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.warn(LOG_CODE_FORMAT,
                e.getClass().getSimpleName(),
                ErrorCode.MISSING_BODY.getCode(),
                ErrorCode.MISSING_BODY.getMessage());

        ErrorResponse response = ErrorResponse.from(ErrorCode.MISSING_BODY);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> constraintViolationException(ConstraintViolationException e) {
        log.warn(LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage());

        ErrorResponse response = ErrorResponse.from(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }


    /**
     * Multipart-Form-Data Exception
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> missingServletRequestPartException(MissingServletRequestPartException e) {
        log.warn(LOG_CODE_FORMAT,
                e.getClass().getSimpleName(),
                ErrorCode.MISSING_FILE.getCode(),
                ErrorCode.MISSING_FILE.getMessage());

        ErrorResponse response = ErrorResponse.from(ErrorCode.MISSING_FILE);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }



    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> applicationException(ApplicationException e) {

        String errorCode = e.getErrorCode();
        String exceptionClassName = e.getClass().getSimpleName();
        String message = e.getMessage();
        ErrorResponse errorResponse = null;

        if (e.getErrors() != null) {
            log.warn(LOG_CODE_FORMAT, exceptionClassName, errorCode, "@valid");
            errorResponse = ErrorResponse.of(message, errorCode, e.getErrors());
        } else {
            log.warn(LOG_CODE_FORMAT, exceptionClassName, errorCode, message);
            errorResponse = ErrorResponse.of(message, errorCode);
        }

        return ResponseEntity
                .status(e.getHttpStatus())
                .body(errorResponse);
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR_CODE));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(
                LOG_FORMAT,
                e.getClass().getSimpleName(),
                e.getMessage()
        );
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.from(ErrorCode.INTERNAL_SERVER_ERROR_CODE));
    }


}
