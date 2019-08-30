package com.example.photoapp.api.users.exceptions;

public class UserServiceException extends RuntimeException {
    private static final long serialVersionUID = 6776267132922184105L;

    public UserServiceException(String message) {
        super(message);
    }
}
