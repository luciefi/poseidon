package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.User;
import com.poseidon.poseidon.exceptions.NewUserWithEmptyPasswordException;
import com.poseidon.poseidon.exceptions.UserNotFoundException;
import com.poseidon.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(User user) {
        if (user.getPassword().isEmpty()) {
            throw new NewUserWithEmptyPasswordException();
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Override
    public void update(User updatedUser, int id) {
        User userFromRepo = findById(id);
        updateFields(userFromRepo, updatedUser);

        if (!updatedUser.getPassword().isEmpty()) {
            userFromRepo.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        repository.save(userFromRepo);
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public User getUserById(Integer id) {
        User user = findById(id);
        user.setPassword("");
        return user;
    }

    @Override
    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private void updateFields(User user, User updatedUser) {
        user.setFullname(updatedUser.getFullname());
        user.setUsername(updatedUser.getUsername());
        user.setRole(updatedUser.getRole());
    }
}
