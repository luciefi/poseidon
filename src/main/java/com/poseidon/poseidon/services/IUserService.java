package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.User;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    void save(User user);

    void update(User user, int id);

    User findById(Integer id);

    User getUserById(Integer id);

    void delete(Integer id);
}
