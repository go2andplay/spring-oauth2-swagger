package com.drucare.api.advice;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.drucare.api.exception.ChangeNotAllowedException;
import com.drucare.api.exception.CoreServiceConnectionException;
import com.drucare.api.exception.InvalidPayloadException;
import com.drucare.api.exception.RecordNotFoundException;
import com.drucare.api.exception.StatusUpdationException;
import com.drucare.api.exception.WyzbeeException;

@ControllerAdvice
public class ValidationAdvicer {

    @Autowired
    private MessageSource msgSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage processValidationError(MethodArgumentNotValidException ex) {
        final BindingResult result = ex.getBindingResult();

        return this.processFieldError(result.getFieldErrors());
    }

    @ExceptionHandler(InvalidPayloadException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage invalidPayloadError(InvalidPayloadException ex) {
        final Map<String, String> errorMessage = ex.getErrorMap();

        return this.processFieldError(errorMessage);
    }

    @ExceptionHandler(WyzbeeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorMessage wyzbeeException(WyzbeeException ex) {
        final Map<String, String> errorMessage = ex.getErrorMap();

        return this.processFieldError(errorMessage);
    }

    @ExceptionHandler(RecordNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorMessage recordNotFoundException(RecordNotFoundException ex) {
        final Map<String, String> errorMessage = ex.getErrorMap();

        return this.processFieldError(errorMessage);
    }

    @ExceptionHandler(CoreServiceConnectionException.class)
    @ResponseStatus(HttpStatus.GATEWAY_TIMEOUT)
    @ResponseBody
    public ErrorMessage coreServiceException(CoreServiceConnectionException ex) {
        final Map<String, String> errorMessage = ex.getErrorMap();

        return this.processFieldError(errorMessage);
    }

    @ExceptionHandler(StatusUpdationException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ErrorMessage statusUpdationException(StatusUpdationException ex) {
        final Map<String, String> errorMessage = ex.getErrorMap();
        return this.processFieldError(errorMessage);
    }

    @ExceptionHandler(ChangeNotAllowedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorMessage changeNotAllowedException(ChangeNotAllowedException ex) {
        final Map<String, String> errorMessage = ex.getErrorMap();
        return this.processFieldError(errorMessage);
    }

    private ErrorMessage processFieldError(Map<String, String> errors) {
        final ErrorMessage message = new ErrorMessage();

        for (final Entry<String, String> error : errors.entrySet()) {
            message.addError(error.getKey(), this.localizedMessage(error.getValue()));
        }

        return message;
    }

    private ErrorMessage processFieldError(List<FieldError> errors) {
        final ErrorMessage message = new ErrorMessage();

        for (final FieldError error : errors) {
            message.addError(error.getField(), this.localizedMessage(error.getDefaultMessage()));
        }

        return message;
    }

    String localizedMessage(String defaultMsg) {
        return this.msgSource.getMessage(defaultMsg, null, LocaleContextHolder.getLocale());
    }
}
