package com.poseidon.poseidon.repositories;

import com.poseidon.poseidon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

    Optional<User> findByUsername(String userName);

    Optional<User> findByPrincipalAndRegistrationId(String principalName, String registrationId);

    Optional<User> findByUsernameAndIdNot(String username, int id);
}
