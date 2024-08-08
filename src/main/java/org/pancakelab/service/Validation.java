package org.pancakelab.service;

public class Validation {

    public static synchronized boolean validatePositveNumber(int number) {
        if (number > 0 && number <= Integer.MAX_VALUE) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }
}
