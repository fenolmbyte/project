package ru.tinkoff.edu.java.bot.exception;

public class SendingMessageException extends RuntimeException {
    public SendingMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public SendingMessageException(Long id, Throwable cause) {
        this("Ошибка при отправке сообщения с chatId=" + id, cause);
    }
}
