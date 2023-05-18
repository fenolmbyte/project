package ru.tinkoff.edu.java.scrapper.controller;

import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.model.Link;
import ru.tinkoff.edu.java.scrapper.service.domain.api.SubscriptionService;

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
    public LinkResponse create(
        @PathVariable("id") Long id,
        @RequestBody AddLinkRequest request
    ) {
        Link link = subscriptionService.subscribe(id, URI.create(request.link()));
        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
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
    public LinkResponse delete(
        @PathVariable("id") Long id,
        @RequestBody RemoveLinkRequest request
    ) {
        Link link = subscriptionService.unsubscribe(id, URI.create(request.link()));
        return new LinkResponse(link.getId(), URI.create(link.getUrl()));
    }
}
