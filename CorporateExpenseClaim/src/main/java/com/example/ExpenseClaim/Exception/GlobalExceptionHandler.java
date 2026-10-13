package com.example.ExpenseClaim.Exception;

import com.example.ExpenseClaim.ApiResponse.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException ex){

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(error ->{
                    errors.put(
                            error.getField(),
                            error.getDefaultMessage()

                    );
                });

        ApiResponse response = ApiResponse.builder()
                .message("Validation Failed")
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .data(null)
                .errors(errors)
                .build();

       return ResponseEntity
               .status(HttpStatus.BAD_REQUEST)
               .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handledRuntimeException(RuntimeException ex){

        ApiResponse response = ApiResponse.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.CONFLICT)
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ResourceNotFoundException ex){
        String message = ex.getMessage();
        ApiResponse response = ApiResponse.builder()
                .message(message)
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .data(null)
                .errors(null)
                .build();
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleResourceNotFoundException(ConstraintViolationException ex){

        String error = ex.getConstraintViolations()
                .iterator()
                .next()
                .getMessage();
        ApiResponse response = ApiResponse.builder()
                .message("Validation Failed")
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .data(null)
                .errors(error)
                .build();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST).body(response);
    }

}
