package com.example.github.adapters.rest.client;

import java.text.MessageFormat;

public class UserNotFoundException extends Exception {

    public UserNotFoundException(final String login, final Exception cause) {
        super(MessageFormat.format("User with login {} is not found", login), cause);
    }
}
