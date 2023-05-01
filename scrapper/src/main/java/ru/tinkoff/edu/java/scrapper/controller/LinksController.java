package ru.tinkoff.edu.java.scrapper.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.entity.LinkEntity;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;

import java.net.URI;
import java.util.List;

@Validated
@RequestMapping("/links")
@RestController
@RequiredArgsConstructor
public class LinksController {
    private final SubscriptionService subscriptionService;

    @PostMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LinkResponse create(@PathVariable("id") Long id,
                               @RequestBody AddLinkRequest request) {
        LinkEntity linkEntity = subscriptionService.subscribe(id, URI.create(request.link()));
        return new LinkResponse(linkEntity.getId(), URI.create(linkEntity.getUrl()));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ListLinksResponse getAll(@PathVariable("id") Long id) {
        List<LinkResponse> links = subscriptionService.getChatSubscriptions(id)
                .stream()
                .map(linkEntity -> new LinkResponse(linkEntity.getId(), URI.create(linkEntity.getUrl())))
                .toList();
        return new ListLinksResponse(links, links.size());
    }

    @DeleteMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LinkResponse delete(@PathVariable("id") Long id,
                               @RequestBody RemoveLinkRequest request) {
        LinkEntity linkEntity = subscriptionService.unsubscribe(id, URI.create(request.link()));
        return new LinkResponse(linkEntity.getId(), URI.create(linkEntity.getUrl()));
    }
}
