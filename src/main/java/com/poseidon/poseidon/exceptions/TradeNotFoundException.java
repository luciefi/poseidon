package com.poseidon.poseidon.exceptions;

public class TradeNotFoundException extends NotFoundException {
    public TradeNotFoundException() {
        super("Trade could not be found.");
    }
}
