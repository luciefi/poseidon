package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.User;
import com.poseidon.poseidon.domain.UserDetailsImpl;
import com.poseidon.poseidon.exceptions.UserNotFoundException;
import com.poseidon.poseidon.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(userName).orElseThrow(UserNotFoundException::new);
        return new UserDetailsImpl(user);
    }
}