package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.User;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.List;

public interface IUserService {
    List<User> findAll();

    void save(User user);

    void update(User user, int id);

    User findById(Integer id);

    User getUserById(Integer id);

    void delete(Integer id);

    boolean hasAnAccount(OAuth2AuthenticationToken authentication);

    void saveOAuthUser(String username, OAuth2AuthenticationToken authentication);
}
