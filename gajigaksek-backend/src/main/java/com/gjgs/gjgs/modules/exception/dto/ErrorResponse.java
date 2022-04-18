package com.gjgs.gjgs.modules.exception.dto;


import com.gjgs.gjgs.modules.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class ErrorResponse {

    private String message;

    private String code;

    private List<FieldError> errors = new ArrayList<>();

    private ErrorResponse(String message) {
        this.message = message;
    }

    private ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    private ErrorResponse(String message, String code, List<FieldError> errors) {
        this.message = message;
        this.code = code;
        this.errors = errors;
    }

    public static ErrorResponse from(ErrorCode errorCode){
        return new ErrorResponse(errorCode.getMessage(),errorCode.getCode());
    }

    public static ErrorResponse from(String message){
        return new ErrorResponse(message);
    }


    public static ErrorResponse of(String message, String code) {
        return new ErrorResponse(message, code);
    }

    public static ErrorResponse of(String message, String code, BindingResult result) {
        return new ErrorResponse(message, code, FieldError.of(result));
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @Builder
    public static class FieldError {
        private String field;

        private String value;

        private String reason;

        private FieldError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }


        private static List<FieldError> of(final BindingResult bindingResult) {
            final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(toList());
        }
    }


}