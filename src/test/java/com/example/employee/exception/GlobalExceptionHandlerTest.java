package com.example.employee.exception;

import com.example.employee.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleRuntime_ShouldReturnBadRequest_WhenRuntimeExceptionThrown() {
        String errorMessage = "Test runtime exception";
        RuntimeException exception = new RuntimeException(errorMessage);

        ResponseEntity<?> response = exceptionHandler.handleRuntime(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo(errorMessage);
    }

    @Test
    void handleRuntime_ShouldReturnBadRequest_WithNullMessage() {
        RuntimeException exception = new RuntimeException((String) null);

        ResponseEntity<?> response = exceptionHandler.handleRuntime(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNull();
    }

    @Test
    void handleRuntime_ShouldReturnBadRequest_WithEmptyMessage() {
        RuntimeException exception = new RuntimeException("");

        ResponseEntity<?> response = exceptionHandler.handleRuntime(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("");
    }

    @Test
    void handleRuntime_ShouldHandleSpecificRuntimeExceptions() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<?> response = exceptionHandler.handleRuntime(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Invalid argument");
    }

    @Test
    void handleValidation_ShouldReturnBadRequest_WhenValidationFails() {
        // Create a mock BindingResult with validation errors
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        bindingResult.addError(new FieldError("testObject", "field1", "Field1 is required"));
        bindingResult.addError(new FieldError("testObject", "field2", "Field2 is invalid"));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<?> response = exceptionHandler.handleValidation(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        
        
        assertThat(response.getBody().toString()).contains("testObject");
    }

    @Test
    void handleValidation_ShouldReturnBadRequest_WhenNoValidationErrors() {
        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "testObject");
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<?> response = exceptionHandler.handleValidation(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void handleRuntime_ShouldHandleNestedExceptions() {
        RuntimeException cause = new RuntimeException("Root cause");
        RuntimeException exception = new RuntimeException("Wrapper exception", cause);

        ResponseEntity<?> response = exceptionHandler.handleRuntime(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Wrapper exception");
    }

    @Test
    void handleRuntime_ShouldHandleCustomRuntimeExceptions() {
        class CustomRuntimeException extends RuntimeException {
            public CustomRuntimeException(String message) {
                super(message);
            }
        }

        CustomRuntimeException exception = new CustomRuntimeException("Custom error");

        ResponseEntity<?> response = exceptionHandler.handleRuntime(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isEqualTo("Custom error");
    }
}