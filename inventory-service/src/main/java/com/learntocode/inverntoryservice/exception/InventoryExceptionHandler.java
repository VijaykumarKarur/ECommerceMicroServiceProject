package com.learntocode.inverntoryservice.exception;

import com.learntocode.inverntoryservice.dto.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class InventoryExceptionHandler {
    /***
     * Method to handle InventoryNotFoundException
     * @param exception InventoryNotFoundException
     * @param request WebRequest
     * @return ResponseEntity wrapping ExceptionResponseDTO containing HttpStatus and Message
     */
    @ExceptionHandler(InventoryNotFoundException.class)
    public ResponseEntity<ExceptionResponseDTO> inventoryNotFoundExceptionHandler
            (InventoryNotFoundException exception, WebRequest request){
        ExceptionResponseDTO responseDTO = ExceptionResponseDTO
                .builder()
                .status(HttpStatus.NOT_FOUND)
                .message(exception.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
}
