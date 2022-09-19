package com.learntocode.productservice.exception;

import com.learntocode.productservice.dto.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ProductExceptionHandler {
    /***
     * Method to handle ProductNotFoundException
     * @param exception ProductNotFoundException custom exception
     * @param request
     * @return ResponseEntity wrapping ExceptionResponseDTO containing HttpStatus and Message
     */
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> productNotFoundExceptionHandler(
            ProductNotFoundException exception,
            WebRequest request){
        ExceptionResponseDTO responseDTO = ExceptionResponseDTO
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}
