package com.jacare.eletropecas.User.Domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    @DisplayName("Deve criar um usuário com dados válidos")
    void shouldCreateUserWithValidData() {
        User user = new User(1L, "André Silva", "andre@email.com", "123.456.789-00", "$2a$12$hash");

        assertAll("Propriedades do Usuário",
                () -> assertEquals("André Silva", user.getName()),
                () -> assertEquals("andre@email.com", user.getEmail()),
                () -> assertEquals("123.456.789-00", user.getCpf())
        );
    }

    @Test
    @DisplayName("Deve falhar se tentar criar usuário com e-mail inválido")
    void shouldThrowExceptionWhenEmailIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> {
            new User(1L, "André Silva", "123", "123.456.789-00", "senha");
        });
    }


    @Test
@DisplayName("Deve falhar se o e-mail for nulo")
void shouldThrowExceptionWhenEmailIsNull() {
    assertThrows(IllegalArgumentException.class, () -> {
        new User(1L, "André Silva", null, "123.456.789-00", "senha");
    });
}
}