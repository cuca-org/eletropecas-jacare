package com.jacare.eletropecas.User.Application;

import com.jacare.eletropecas.User.Domain.User;
import com.jacare.eletropecas.User.Persistence.UserEntity;
import com.jacare.eletropecas.User.Persistence.UserMapper;
import com.jacare.eletropecas.User.Persistence.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = userRepository.save(entity);
        return UserMapper.toDomain(saved);
    }

    @Override
    public List<User> getAllUsers() {
        List<UserEntity> entities = userRepository.findAll();
        return entities.stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public User getUserById(Long id) {
        UserEntity found = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID: " + id));

        return UserMapper.toDomain(found);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        // Busca o usuário existente ou lança a exceção tratada no método acima
        User existingUser = getUserById(id);

        // Atualiza os campos do domínio
        existingUser.setName(updatedUser.getName());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setCpf(updatedUser.getCpf());

        // Só atualiza a senha se ela for enviada na requisição
        if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isBlank()) {
            existingUser.setPasswordHash(updatedUser.getPasswordHash());
        }

        UserEntity entity = UserMapper.toEntity(existingUser);
        UserEntity saved = userRepository.save(entity);

        return UserMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        UserEntity entity = UserMapper.toEntity(user);
        userRepository.delete(entity);
    }
}
