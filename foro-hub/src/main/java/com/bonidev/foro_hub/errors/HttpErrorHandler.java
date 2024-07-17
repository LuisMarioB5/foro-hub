package com.bonidev.foro_hub.errors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
public class HttpErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> errorEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<?> error400(HandlerMethodValidationException e) {
        var errors = e.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof FieldError fieldError) {
                        return new DatosErrorValidacion(fieldError.getField(), fieldError.getDefaultMessage());
                    }
                    return new DatosErrorValidacion("unknown", error.getDefaultMessage());
                })
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<?> errorDuplicidad(SQLIntegrityConstraintViolationException e) {
        return ResponseEntity.badRequest().body("No se aceptan datos duplicados en la base de datos...");
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> errorParse(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest().body("El id introducido no es válido");
    }

    private record DatosErrorValidacion(String campo, String mensajeError) {
        public DatosErrorValidacion(String campo, String mensajeError) {
            this.campo = campo;
            this.mensajeError = mensajeError;
        }
    }
}
