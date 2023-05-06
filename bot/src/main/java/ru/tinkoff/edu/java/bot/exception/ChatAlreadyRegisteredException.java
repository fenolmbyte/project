package ru.tinkoff.edu.java.bot.exception;

public class ChatAlreadyRegisteredException extends Exception {
    public ChatAlreadyRegisteredException() {
    }

    public ChatAlreadyRegisteredException(String message) {
        super(message);
    }

    public ChatAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChatAlreadyRegisteredException(Throwable cause) {
        super(cause);
    }
}
