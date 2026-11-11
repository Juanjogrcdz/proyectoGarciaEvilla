package com.plazoleta.infrastructure.input.rest.dto;

import jakarta.validation.constraints.*;

public class EmployeeRequestDto {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

    @NotBlank(message = "El documento es obligatorio")
    @Pattern(regexp = "^[0-9]+$", message = "El documento de identidad debe ser únicamente numérico")
    private String identityDocument;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 13, message = "El teléfono debe contener máximo 13 caracteres")
    @Pattern(regexp = "^\\+?[0-9]+$", message = "El teléfono solo puede contener números y el símbolo +")
    private String phone;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Estructura de email inválida")
    private String email;

    @NotBlank(message = "La clave es obligatoria")
    private String password;

    @NotNull(message = "El restaurante del empleado es obligatorio")
    private Long restaurantId;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getIdentityDocument() { return identityDocument; }
    public void setIdentityDocument(String identityDocument) { this.identityDocument = identityDocument; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
}