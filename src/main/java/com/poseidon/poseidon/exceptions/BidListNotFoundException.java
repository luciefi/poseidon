package com.poseidon.poseidon.exceptions;

public class BidListNotFoundException extends NotFoundException {
    public BidListNotFoundException() {
        super("Bid list could not be found.");
    }
}
