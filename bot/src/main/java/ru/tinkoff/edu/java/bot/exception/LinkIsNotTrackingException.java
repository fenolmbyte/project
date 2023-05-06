package ru.tinkoff.edu.java.bot.exception;

public class LinkIsNotTrackingException extends Exception {
    public LinkIsNotTrackingException() {
    }

    public LinkIsNotTrackingException(String message) {
        super(message);
    }

    public LinkIsNotTrackingException(String message, Throwable cause) {
        super(message, cause);
    }

    public LinkIsNotTrackingException(Throwable cause) {
        super(cause);
    }
}
