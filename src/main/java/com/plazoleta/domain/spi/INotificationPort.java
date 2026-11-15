package com.plazoleta.domain.spi;

public interface INotificationPort {
    // Debe lanzar NotificationFailedException si el proveedor no pudo enviar el mensaje
    void sendSms(String phoneNumber, String message);
}