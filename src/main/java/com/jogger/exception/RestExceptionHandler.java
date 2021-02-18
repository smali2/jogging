// Bismillah Hirrahman Nirrahim

package com.jogger.exception;

import javax.persistence.EntityNotFoundException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

   @Override
   protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
       String error = "Malformed JSON request";
       return buildResponseEntity(new ApiError(HttpStatus.BAD_REQUEST, error, ex));
   }

   private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
       return new ResponseEntity<>(apiError, apiError.getStatus());
   }

   //other exception handlers below
   
   @ExceptionHandler(EntityNotFoundException.class)
   protected ResponseEntity<Object> handleEntityNotFound(
           EntityNotFoundException ex) {
       ApiError apiError = new ApiError(HttpStatus.NOT_FOUND);
       apiError.setMessage(ex.getMessage());
       return buildResponseEntity(apiError);
   }
   
   //@ResponseStatus(HttpStatus.CONFLICT)  // 409
   @ExceptionHandler(DataIntegrityViolationException.class)
   public ResponseEntity<Object> handleConflict(DataIntegrityViolationException ex) {
	   String error = "Ensure that the fields are mapped correctly.";
	   ApiError apiError = new ApiError(HttpStatus.CONFLICT, error, ex);
       return buildResponseEntity(apiError);
   }

   @ExceptionHandler(ConstraintViolationException.class)
   public ResponseEntity<Object> handleConflict(ConstraintViolationException ex) {
	   String error = "Ensure that the fields are mapped correctly.";
	   ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE, error, ex);
       return buildResponseEntity(apiError);
   }
   
   @ExceptionHandler(IllegalArgumentException.class)
   public ResponseEntity<Object> handleConflict(IllegalArgumentException ex) {
	   String error = "Ensure that the fields are mapped correctly.";
	   ApiError apiError = new ApiError(HttpStatus.NOT_ACCEPTABLE, error, ex);
       return buildResponseEntity(apiError);
   }
}
