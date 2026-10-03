package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IUserPersistencePort;
import com.plazoleta.infrastructure.output.entity.UserEntity;
import com.plazoleta.infrastructure.output.mapper.IUserEntityMapper;
import com.plazoleta.infrastructure.output.repository.IUserRepository;
import org.springframework.stereotype.Component;

@Component
public class UserAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    public UserAdapter(IUserRepository userRepository, IUserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

    @Override
    public void saveUser(User user) {
        // 1. Convertimos el objeto de dominio 'User' a la entidad JPA 'UserEntity'
        UserEntity userEntity = userEntityMapper.toEntity(user);
        
        // 2. Guardamos en la base de datos a través del JpaRepository
        userRepository.save(userEntity);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    @Override
public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email).map(userEntityMapper::toModel);
}
}