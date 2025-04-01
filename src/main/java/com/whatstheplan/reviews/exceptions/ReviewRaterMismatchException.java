package com.whatstheplan.reviews.exceptions;

public class ReviewRaterMismatchException extends RuntimeException {
    public ReviewRaterMismatchException(String message) {
        super(message);
    }
}