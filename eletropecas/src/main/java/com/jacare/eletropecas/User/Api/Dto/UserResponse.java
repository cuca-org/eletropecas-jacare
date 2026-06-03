package com.jacare.eletropecas.User.Api.Dto;

import com.jacare.eletropecas.User.Domain.User;

public record UserResponse(
        Long id,
        String name,
        String email,
        String cpf
) {
    public static UserResponse toResponse(User user) {
        if (user == null) return null;
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCpf()
        );
    }
}