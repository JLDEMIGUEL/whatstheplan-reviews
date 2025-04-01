package com.whatstheplan.reviews.exceptions;

public class ReviewNotExistsException extends RuntimeException {
    public ReviewNotExistsException(String message) {
        super(message);
    }
}
