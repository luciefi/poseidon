package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.User;
import com.poseidon.poseidon.domain.UserRole;
import com.poseidon.poseidon.exceptions.AlreadyExistsException;
import com.poseidon.poseidon.exceptions.NewUserWithEmptyPasswordException;
import com.poseidon.poseidon.exceptions.UserNotFoundException;
import com.poseidon.poseidon.exceptions.UsernameAlreadyExistsException;
import com.poseidon.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


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
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
        user.setRole(UserRole.User);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
    }

    @Override
    public void update(User updatedUser, int id) {
        if (repository.findByUsernameAndIdNot(updatedUser.getUsername(), id).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }
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

    @Override
    public boolean hasAnAccount(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        if (repository.findByPrincipalAndRegistrationId(client.getPrincipalName(), client.getClientRegistration().getRegistrationId()).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public void saveOAuthUser(String username, OAuth2AuthenticationToken authentication) {

        if (username.isEmpty()) {
            throw new IllegalArgumentException("The username must be between 1 and 125 characters long.");
        }
        if (repository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyExistsException();
        }

        OAuth2AuthorizedClient client = authorizedClientService
                .loadAuthorizedClient(
                        authentication.getAuthorizedClientRegistrationId(),
                        authentication.getName());

        if (repository.findByPrincipalAndRegistrationId(client.getPrincipalName(), client.getClientRegistration().getRegistrationId()).isPresent()) {
            throw new AlreadyExistsException("OAuth user already exists.");
        }

        User user = new User();
        user.setUsername(username);
        user.setPrincipal(client.getPrincipalName());
        user.setRegistrationId(client.getClientRegistration().getRegistrationId());
        user.setFullname(username);
        user.setRole(UserRole.User);
        repository.save(user);
    }

    private void updateFields(User user, User updatedUser) {
        user.setFullname(updatedUser.getFullname());
        user.setUsername(updatedUser.getUsername());
        user.setRole(updatedUser.getRole());
    }


}
