package ru.tinkoff.edu.java.bot.exception;

public class SendingMessageException extends RuntimeException {
    public SendingMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendingMessageException(Long id, Throwable cause) {
        this("Error while sending message with chatId=" + id, cause);
    }
}
