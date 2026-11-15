package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.spi.INotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogNotificationAdapter implements INotificationPort {

    private static final Logger log = LoggerFactory.getLogger(LogNotificationAdapter.class);

    @Override
    public void sendSms(String phoneNumber, String message) {
        log.info("[SMS SIMULADO] Para {} -> {}", phoneNumber, message);
    }
}