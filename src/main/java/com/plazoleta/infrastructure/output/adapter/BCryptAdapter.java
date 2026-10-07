package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.spi.IPasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BCryptAdapter implements IPasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    public BCryptAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}