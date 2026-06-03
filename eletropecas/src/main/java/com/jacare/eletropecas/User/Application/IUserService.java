package com.jacare.eletropecas.User.Application;

import com.jacare.eletropecas.User.Domain.User;

import java.util.List;

public interface IUserService {
    User createUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
}
