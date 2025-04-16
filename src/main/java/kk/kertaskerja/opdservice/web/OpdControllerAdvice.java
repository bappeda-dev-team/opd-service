package kk.kertaskerja.opdservice.web;

import jakarta.servlet.http.HttpServletRequest;
import kk.kertaskerja.opdservice.domain.OpdAlreadyExistsException;
import kk.kertaskerja.opdservice.common.exception.ApiError;
import kk.kertaskerja.opdservice.domain.OpdNotFoundException;
import kk.kertaskerja.opdservice.domain.OpdParentNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class OpdControllerAdvice {
    @ExceptionHandler(OpdNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ApiError opdNotFoundHandler(OpdNotFoundException ex, HttpServletRequest request) {
        return new ApiError(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                Instant.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(OpdAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ApiError opdAlreadyExistsHandler(OpdAlreadyExistsException ex, HttpServletRequest request) {
        return new ApiError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage(),
                Instant.now(),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(OpdParentNotFoundException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    ApiError opdParentNotFoundHandler(OpdParentNotFoundException ex, HttpServletRequest request) {
        return new ApiError(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage(),
                Instant.now(),
                request.getRequestURI()
        );
    }
}
