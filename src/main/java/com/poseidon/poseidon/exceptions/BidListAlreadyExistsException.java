package com.poseidon.poseidon.exceptions;

public class BidListAlreadyExistsException extends AlreadyExistsException {
    public BidListAlreadyExistsException() {
        super("Bid list already exists.");
    }

}
