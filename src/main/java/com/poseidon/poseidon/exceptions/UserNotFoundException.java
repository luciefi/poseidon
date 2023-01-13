package com.poseidon.poseidon.exceptions;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User could not be found.");
    }
}


