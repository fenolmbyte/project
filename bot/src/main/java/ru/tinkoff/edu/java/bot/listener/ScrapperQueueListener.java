package ru.tinkoff.edu.java.bot.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import ru.tinkoff.edu.java.bot.dto.controller.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.telegram.TrackerBot;

@Slf4j
@RabbitListener(queues = "${app.rabbit-queue.queue-name}")
@RequiredArgsConstructor
public class ScrapperQueueListener {
    private final TrackerBot bot;

    @RabbitHandler
    public void receive(LinkUpdateRequest message) {
        log.info(message.toString());
        bot.sendUpdates(message);
    }
}
