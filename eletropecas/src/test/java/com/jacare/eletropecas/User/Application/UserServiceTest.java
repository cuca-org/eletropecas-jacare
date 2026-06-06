package com.jacare.eletropecas.User.Application;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jacare.eletropecas.User.Domain.User;
import com.jacare.eletropecas.User.Persistence.UserEntity;
import com.jacare.eletropecas.User.Persistence.UserRepository;

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

        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User savedUser = userService.createUser(userDomain);

        assertNotNull(savedUser);
        assertEquals("andre@email.com", savedUser.getEmail());
        assertEquals("André Silva", savedUser.getName());
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Não deve atualizar a senha se o hash enviado for vazio")
    void shouldKeepCurrentPasswordWhenIncomingHashIsEmpty() {
        UserEntity existingEntity = new UserEntity(
                1L, "André Silva", "andre@email.com", "123.456.789-00", "$2a$original_hash"
        );

        User updateData = new User(
                1L, "André Silva", "andre@email.com", "123.456.789-00", ""
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(1L, updateData);

        assertNotNull(updatedUser);
        assertEquals("$2a$original_hash", updatedUser.getPasswordHash());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve atualizar a senha quando hash novo for informado")
    void shouldUpdatePasswordWhenHashIsProvided() {
        UserEntity existingEntity = new UserEntity(
                1L, "André Silva", "andre@email.com", "123.456.789-00", "$2a$original_hash"
        );

        User updateData = new User(
                1L, "André Silva", "andre@email.com", "123.456.789-00", "$2a$new_hash"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(userRepository.save(any(UserEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User updatedUser = userService.updateUser(1L, updateData);

        assertNotNull(updatedUser);
        assertEquals("$2a$new_hash", updatedUser.getPasswordHash());
        verify(userRepository).findById(1L);
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando tentar atualizar usuário inexistente")
    void shouldThrowWhenUserDoesNotExist() {
        User updateData = new User(
                1L, "André Silva", "andre@email.com", "123.456.789-00", "hash"
        );

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(1L, updateData));

        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }
}