package com.nnk.springboot.exceptions;

public class NotFoundException extends IllegalArgumentException {
    public NotFoundException(String s) {
        super(s);
    }
}