package com.plazoleta.infrastructure.output.adapter;

import com.plazoleta.domain.model.User;
import com.plazoleta.domain.spi.IUserPersistencePort;
import com.plazoleta.infrastructure.output.jpa.entity.UserEntity;
import com.plazoleta.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.plazoleta.infrastructure.output.jpa.repository.IUserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAdapter implements IUserPersistencePort {

    private final IUserRepository userRepository;
    private final IUserEntityMapper userEntityMapper;

    public UserAdapter(IUserRepository userRepository, IUserEntityMapper userEntityMapper) {
        this.userRepository = userRepository;
        this.userEntityMapper = userEntityMapper;
    }

       @Override
    public User saveUser(User user) {
        UserEntity saved = userRepository.save(userEntityMapper.toEntity(user));
        return userEntityMapper.toModel(saved);   // ya trae el id generado
    }

    @Override
    public User findById(Long id) {
        if (id == null) {
            return null;
        }
        return userRepository.findById(id)
                .map(userEntityMapper::toModel)
                .orElse(null);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userEntityMapper::toModel);
    }
}