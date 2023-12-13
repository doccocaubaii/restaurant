package vn.softdreams.easypos.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.service.dto.ResultException;
import vn.softdreams.easypos.service.impl.PrintConfigServiceImpl;
import vn.softdreams.easypos.web.rest.errors.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(PrintConfigServiceImpl.class);

    @ExceptionHandler({ InternalServerException.class })
    public ResponseEntity<Object> handleInternalServerException(InternalServerException ex, WebRequest request) {
        log.error("InternalServerException:");
        log.error("content:" + ex.getTitle());
        log.error("note:" + ex.getEntityName());
        ResultDTO dto = new ResultDTO(List.of(new ResultException(ex.getErrorKey(), ex.getTitle())), ex.getTitle());
        return new ResponseEntity<>(dto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({ IntegrationException.class })
    public ResponseEntity<ResultDTO> handleIntegrationException(IntegrationException ex, WebRequest request) {
        log.error("Integration Exception");
        String message = ex.getPartyName() + ": " + ex.getMessage();
        return new ResponseEntity<>(new ResultDTO(message, ""), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ BadRequestAlertException.class })
    public ResponseEntity<Object> handleBadRequestAlertException(BadRequestAlertException ex, WebRequest request) {
        addLogException("BadRequestAlertException:", ex.getErrorKey(), ex.getMessage(), ex.getTitle());
        ResultDTO dto = new ResultDTO(Arrays.asList(new ResultException(ex.getErrorKey(), ex.getTitle())), ex.getTitle());
        return new ResponseEntity<>(dto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ AuthenticateException.class })
    public ResponseEntity<Object> handleAuthenticateException(AuthenticateException ex, WebRequest request) {
        addLogException("AuthenticateException:", ex.getErrorKey(), ex.getMessage(), ex.getTitle());
        ResultDTO dto = new ResultDTO(Arrays.asList(new ResultException(ex.getErrorKey(), ex.getTitle())), ex.getTitle());
        return new ResponseEntity<>(dto, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ RequestAlertException.class })
    public ResponseEntity<Object> handleRequestAlertException(RequestAlertException ex, WebRequest request) {
        addLogException("RequestAlertException:", ex.getErrorKey(), ex.getMessage(), ex.getTitle());
        ObjectMapper mapper = new ObjectMapper();
        ResultException[] data = null;
        try {
            data = mapper.readValue("[" + ex.getData().toString() + "]", ResultException[].class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        ResultDTO dto = new ResultDTO(data, ex.getTitle());
        return new ResponseEntity<>(dto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ ExceptionIntegrated.class })
    public ResponseEntity<Object> handleIntegratedException(ExceptionIntegrated ex, WebRequest request) {
        addLogException("RequestAlertException:", ex.getErrorKey(), ex.getMessage(), ex.getTitle());
        ResultDTO dto = new ResultDTO(Arrays.asList(new ResultException(ex.getErrorKey(), ex.getTitle())), ex.getTitle());
        return new ResponseEntity<>(dto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ InsufficientAuthenticationException.class })
    public ResponseEntity<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException ex, WebRequest request) {
        log.error("Exception:");
        for (StackTraceElement item : ex.getStackTrace()) {
            log.error(item.toString());
        }
        ResultDTO dto = new ResultDTO(
            Arrays.asList(new ResultException(ExceptionConstants.UNAUTHORIZED, ExceptionConstants.UNAUTHORIZED_VI)),
            ExceptionConstants.UNAUTHORIZED_VI
        );
        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleErrorException(Exception ex, WebRequest request) {
        log.error("Exception:");
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        log.error(sw.toString());
        ResultDTO dto = new ResultDTO(ExceptionConstants.EXCEPTION_ERROR, ExceptionConstants.EXCEPTION_ERROR_VI);
        return new ResponseEntity<>(dto, HttpStatus.EXPECTATION_FAILED);
    }

    public void addLogException(String exceptionType, String errorKey, String title, String message) {
        log.error(exceptionType + ":\n errorKey: " + errorKey + "\n message:" + message + " \ntitle:" + title);
    }
}
