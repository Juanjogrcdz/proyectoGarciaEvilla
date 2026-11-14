package com.plazoleta.infrastructure.exception;

import com.plazoleta.domain.exception.NotRestaurantOwnerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;
import com.plazoleta.domain.exception.ClientHasOrderInProgressException;
import com.plazoleta.domain.exception.EmployeeWithoutRestaurantException;
import com.plazoleta.domain.exception.InvalidOrderStatusException;
import com.plazoleta.domain.exception.OrderOutsideEmployeeRestaurantException;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleDomainExceptions(IllegalArgumentException ex) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(NotRestaurantOwnerException.class)
    public ResponseEntity<Map<String, String>> handleNotOwner(NotRestaurantOwnerException ex) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials(BadCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
    }

    private ResponseEntity<Map<String, String>> build(HttpStatus status, String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDuplicates(DataIntegrityViolationException ex) {
        return build(HttpStatus.CONFLICT,
                "Los datos ya existen o violan una restricción (¿correo o documento repetido?)");
    }
    

    // 409: el pedido choca con el estado actual del cliente (ya tiene uno en proceso)
    @ExceptionHandler(ClientHasOrderInProgressException.class)
    public ResponseEntity<Map<String, String>> handleOrderInProgress(ClientHasOrderInProgressException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }
    // 403: es un empleado, pero todavía no está asignado a ningún restaurante
    @ExceptionHandler(EmployeeWithoutRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleEmployeeWithoutRestaurant(EmployeeWithoutRestaurantException ex) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage());
    }
    // 403: el pedido es de otro restaurante
    @ExceptionHandler(OrderOutsideEmployeeRestaurantException.class)
    public ResponseEntity<Map<String, String>> handleOrderOutsideRestaurant(OrderOutsideEmployeeRestaurantException ex) {
        return build(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // 409: el estado actual del pedido no permite esa operación
    @ExceptionHandler(InvalidOrderStatusException.class)
    public ResponseEntity<Map<String, String>> handleInvalidOrderStatus(InvalidOrderStatusException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }
    
}