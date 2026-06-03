package com.jacare.eletropecas.User.Persistence;

import com.jacare.eletropecas.User.Domain.User;

public class UserMapper {
    public static User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getPasswordHash()
        );
    }

    public static UserEntity toEntity(User user) {
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf(),
                user.getPasswordHash()
        );
    }
}
