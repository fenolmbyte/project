package ru.tinkoff.edu.java.bot.exception;

public class ScrapperInternalError extends RuntimeException {
    public ScrapperInternalError() {
    }

    public ScrapperInternalError(String message) {
        super(message);
    }

    public ScrapperInternalError(String message, Throwable cause) {
        super(message, cause);
    }

    public ScrapperInternalError(Throwable cause) {
        super(cause);
    }
}
