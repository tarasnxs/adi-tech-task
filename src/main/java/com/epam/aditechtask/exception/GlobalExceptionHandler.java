package com.epam.aditechtask.exception;

import com.epam.aditechtask.dto.ErrorDTO;
import io.swagger.v3.oas.annotations.Hidden;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceAlreadyExistException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ResponseEntity<ErrorDTO> handleResourceAlreadyExist(ResourceAlreadyExistException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Already Exist", ex.getMessage()));
	}

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ResponseEntity<ErrorDTO> handleResourceNotFound(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Not Found", ex.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<List<ErrorDTO>> handleValidationExceptions(MethodArgumentNotValidException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
			ex.getBindingResult().getFieldErrors().stream()
				.map(fieldError -> new ErrorDTO(fieldError.getField(), fieldError.getDefaultMessage()))
				.toList()
		);
	}

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorDTO("Internal Server Error", ex.getMessage()));
	}
}
