package com.dreamsol.exceptions;

import com.dreamsol.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler 
{
    public ApiResponse apiResponse = new ApiResponse();
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),false));
    }
    @ExceptionHandler(ImageNotUploadedException.class)
    public ResponseEntity<ApiResponse> imageNotUploadedExceptionHandler(ImageNotUploadedException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(e.getMessage(),false));
    }
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> methodArgumentNotFoundExceptionHandler(MethodArgumentNotValidException ex)
	{
		Map<String,String> errorResponseMap = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error)->{
			String fieldName = ((FieldError)error).getField();
			String message = error.getDefaultMessage();
			errorResponseMap.put(fieldName, message);
		});
		return new ResponseEntity<>(errorResponseMap,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ApiResponse> nullPointerExceptionHandler(NullPointerException e)
	{
		apiResponse.setMessage(e.getMessage());
		apiResponse.setSuccess(false);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiResponse> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e)
	{
		apiResponse.setMessage(e.getMessage());
		apiResponse.setSuccess(false);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<ApiResponse> sqlIntegrityConstraintViolationExceptionHandler(SQLIntegrityConstraintViolationException e)
	{
		apiResponse.setMessage(e.getMessage());
		apiResponse.setSuccess(false);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}
	@ExceptionHandler(DataAccessException.class)
	public ResponseEntity<ApiResponse> dataAccessExceptionHandler(DataAccessException e)
	{
		apiResponse.setMessage(e.getMessage());
		apiResponse.setSuccess(false);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
	}
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiResponse> constraintViolationExceptionHandler(ConstraintViolationException e)
	{
		apiResponse.setMessage(e.getMessage());
		apiResponse.setSuccess(false);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}
	@ExceptionHandler(InputMismatchException.class)
	public ResponseEntity<ApiResponse> inputMismatchExceptionHandler(InputMismatchException e)
	{
		apiResponse.setMessage(e.getMessage());
		apiResponse.setSuccess(false);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
	}
}
