package com.plazoleta.domain.exception;

public class EmployeeWithoutRestaurantException extends RuntimeException {
    public EmployeeWithoutRestaurantException() {
        super("El empleado no está asociado a ningún restaurante");
    }
}