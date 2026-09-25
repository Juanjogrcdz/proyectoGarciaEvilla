package com.plazoleta.infrastructure.output.repository;

import com.plazoleta.infrastructure.output.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {

    // Método derivado de Spring Data JPA para consultar si un email ya existe
    boolean existsByEmail(String email);

    // Opcional por si necesitamos buscar el usuario completo por email más adelante
    Optional<UserEntity> findByEmail(String email);
}