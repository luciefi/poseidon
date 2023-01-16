package com.poseidon.poseidon.exceptions;

public class UsernameAlreadyExistsException extends AlreadyExistsException{

    public UsernameAlreadyExistsException() {
        super("Username already exists.");
    }
}
