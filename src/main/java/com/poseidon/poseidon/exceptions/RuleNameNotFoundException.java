package com.poseidon.poseidon.exceptions;

public class RuleNameNotFoundException extends NotFoundException {
    public RuleNameNotFoundException() {
        super("Rule name could not be found.");
    }

}
