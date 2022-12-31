package com.poseidon.poseidon.exceptions;

public class RatingNotFoundException extends NotFoundException {
    public RatingNotFoundException() {
        super("Rating could not be found.");
    }
}
