package com.plazoleta.application.usecase;

import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IPasswordEncoderPort;
import com.plazoleta.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseEmployeeTest {

    @Mock private IUserPersistencePort userPersistencePort;
    @Mock private IPasswordEncoderPort passwordEncoderPort;
    @InjectMocks private UserUseCase userUseCase;

    private User buildEmployee() {
        User employee = new User();
        employee.setName("Carlos");
        employee.setLastName("Pérez");
        employee.setIdentityDocument("123456789");
        employee.setPhone("+573001234567");
        employee.setEmail("carlos@mail.com");
        employee.setPassword("clave-plana");
        return employee;
    }

    @Test
    void saveEmployee_assignsEmployeeRole_encodesPassword_andPersists() {
        when(passwordEncoderPort.encode("clave-plana")).thenReturn("$2a$hash");

        userUseCase.saveEmployee(buildEmployee());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userPersistencePort).saveUser(captor.capture());
        User saved = captor.getValue();

        assertEquals(3L, saved.getRole().getId());
        assertEquals("ROLE_EMPLEADO", saved.getRole().getName());
        assertEquals("$2a$hash", saved.getPassword());   // nunca se guarda la clave plana
    }

    @Test
    void saveEmployee_overridesAnyRolePreviouslySet() {
        User employee = buildEmployee();
        employee.setRole(new com.plazoleta.domain.model.Role(1L, "ROLE_ADMINISTRADOR", "intruso"));
        when(passwordEncoderPort.encode("clave-plana")).thenReturn("$2a$hash");

        userUseCase.saveEmployee(employee);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userPersistencePort).saveUser(captor.capture());
        assertEquals("ROLE_EMPLEADO", captor.getValue().getRole().getName());
    }
}