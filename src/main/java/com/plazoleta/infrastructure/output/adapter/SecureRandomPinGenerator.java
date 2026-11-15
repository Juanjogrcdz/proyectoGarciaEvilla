package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.spi.IPinGeneratorPort;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class SecureRandomPinGenerator implements IPinGeneratorPort {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public String generatePin() {
        // 6 dígitos, con ceros a la izquierda si hace falta (ej. 004821)
        return String.format("%06d", secureRandom.nextInt(1_000_000));
    }
}