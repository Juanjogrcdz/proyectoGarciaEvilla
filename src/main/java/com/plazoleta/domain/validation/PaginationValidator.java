package com.plazoleta.domain.validation;

public final class PaginationValidator {

    public static final int MAX_PAGE_SIZE = 100;   

    private PaginationValidator() {
    }

    public static void validate(int page, int size) {
        if (page < 0) {
            throw new IllegalArgumentException("La página no puede ser negativa");
        }
        if (size < 1 || size > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(
                    "El tamaño de página debe estar entre 1 y " + MAX_PAGE_SIZE);
        }
    }
}