package vn.hust.easypos.web.rest.errors;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.hust.easypos.service.dto.ResponseDTO;

/**
 * Controller advice to translate the server side exceptions to client-friendly json structures.
 * The error response follows RFC7807 - Problem Details for HTTP APIs (https://tools.ietf.org/html/rfc7807).
 */
@ControllerAdvice
@Log4j2
public class ExceptionTranslator {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ResponseDTO> handleCustomException(CustomException ex) {
        ResponseDTO responseDTO = new ResponseDTO(ex.getMessage(), ex.getMessage(), ex.getCode());
        return ResponseEntity.status(HttpStatus.valueOf(Integer.parseInt(ex.getCode()))).body(responseDTO);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleOtherExceptions(Exception ex) {
        ResponseDTO responseDTO = new ResponseDTO("There is an error occurred", ex.getMessage(), "500");
        log.error(ex.getMessage(), (Object) ex.getStackTrace());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }
}
