package com.poseidon.poseidon.exceptions;

public class NewUserWithEmptyPasswordException extends IllegalArgumentException {
    public NewUserWithEmptyPasswordException() {
        super("Password is mandatory for new user");
    }

}
