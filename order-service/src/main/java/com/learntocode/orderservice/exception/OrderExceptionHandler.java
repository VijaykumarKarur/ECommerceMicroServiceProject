package com.learntocode.orderservice.exception;

import com.learntocode.orderservice.dto.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ExceptionResponseDTO> orderNotFoundExceptionHandler(OrderNotFoundException exception,
                                                                              WebRequest request){
        ExceptionResponseDTO responseDTO = new ExceptionResponseDTO();
        responseDTO.setStatus(HttpStatus.NOT_FOUND);
        responseDTO.setMessage(exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}
