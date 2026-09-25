package com.plazoleta.domain.spi;


public interface IPasswordEncoderPort {
    String encode(String rawPassword);
}