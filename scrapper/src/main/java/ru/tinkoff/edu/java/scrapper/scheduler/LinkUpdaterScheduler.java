package ru.tinkoff.edu.java.scrapper.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.service.LinkUpdatesService;

@Slf4j
@Component
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
    private final LinkUpdatesService linkUpdatesService;

    @Scheduled(fixedDelayString = "#{@applicationConfig.scheduler.interval}")
    public void update() {
        log.info("Update!");
        linkUpdatesService.updateLinks();
    }
}
