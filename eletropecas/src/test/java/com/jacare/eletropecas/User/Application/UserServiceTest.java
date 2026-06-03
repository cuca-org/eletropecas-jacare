package com.jacare.eletropecas.User.Application;

import com.jacare.eletropecas.User.Domain.User;
import com.jacare.eletropecas.User.Persistence.UserEntity;
import com.jacare.eletropecas.User.Persistence.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve salvar um usuário com sucesso no sistema")
    void shouldSaveUserSuccessfully() {
        User userDomain = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");

        UserEntity userEntity = new UserEntity(1L, "André Silva", "andre@email.com", "123.456.789-00", "hash");

        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);


        User savedUser = userService.createUser(userDomain);


        assertNotNull(savedUser);
        assertEquals("andre@email.com", savedUser.getEmail());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Não deve atualizar a senha se o hash enviado for vazio")
    void shouldNotUpdatePasswordWhenHashIsEmpty() {
        UserEntity existingEntity = new UserEntity(1L, "André Silva", "andre@email.com", "123.456.789-00", "$2a$original_hash");

        User updateData = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(userRepository.save(any(UserEntity.class))).thenReturn(existingEntity);

        User updatedUser = userService.updateUser(1L, updateData);

        assertEquals("$2a$original_hash", updatedUser.getPasswordHash(), "A senha não deveria ter mudado");
    }
}