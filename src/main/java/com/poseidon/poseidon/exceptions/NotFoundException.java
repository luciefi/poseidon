package com.poseidon.poseidon.exceptions;

public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String s) {
        super(s);
    }
}