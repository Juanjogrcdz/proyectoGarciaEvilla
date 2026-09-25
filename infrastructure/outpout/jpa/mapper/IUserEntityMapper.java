package com.plazoleta.infrastructure.output.mapper;

import com.plazoleta.domain.model.Role;
import com.plazoleta.domain.model.User;
import com.plazoleta.infrastructure.output.entity.RoleEntity;
import com.plazoleta.infrastructure.output.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class IUserEntityMapper {

    // Convierte del Modelo de Dominio a Entidad JPA (Para guardar)
    public UserEntity toEntity(User user) {
        if (user == null)
            return null;

        RoleEntity roleEntity = null;
        if (user.getRole() != null) {
            roleEntity = new RoleEntity(
                    user.getRole().getId(),
                    user.getRole().getName(),
                    user.getRole().getDescription());
        }

        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getIdentityDocument(),
                user.getPhone(),
                user.getBirthDate(),
                user.getEmail(),
                user.getPassword(),
                roleEntity);
    }

    // Convierte de Entidad JPA al Modelo de Dominio (Para consultar)
    public User toDomain(UserEntity entity) {
        if (entity == null)
            return null;

        Role role = null;
        if (entity.getRole() != null) {
            role = new Role(
                    entity.getRole().getId(),
                    entity.getRole().getName(),
                    entity.getRole().getDescription());
        }

        return new User(
                entity.getId(),
                entity.getName(),
                entity.getLastName(),
                entity.getIdentityDocument(),
                entity.getPhone(),
                entity.getBirthDate(),
                entity.getEmail(),
                entity.getPassword(),
                role);
    }
}