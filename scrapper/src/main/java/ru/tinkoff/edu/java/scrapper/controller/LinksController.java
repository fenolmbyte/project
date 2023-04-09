package ru.tinkoff.edu.java.scrapper.controller;

import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.dto.controller.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.dto.controller.LinkResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.ListLinksResponse;
import ru.tinkoff.edu.java.scrapper.dto.controller.RemoveLinkRequest;

import java.net.URI;
import java.util.ArrayList;

@Validated
@RequestMapping("/links")
@RestController
public class LinksController {
    @PostMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LinkResponse create(@PathVariable("id") Long id,
                               @RequestBody AddLinkRequest request) {
        // TODO: implement
        return new LinkResponse(1L, URI.create("cool.url.com/path"));
    }

    @GetMapping(
            path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ListLinksResponse getAll(@PathVariable("id") Long id) {
        // TODO: implement
        return new ListLinksResponse(new ArrayList<>(), 0);
    }

    @DeleteMapping(
            path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public LinkResponse delete(@PathVariable("id") Long id,
                               @RequestBody RemoveLinkRequest request) {
        // TODO: implement
        return new LinkResponse(1L, URI.create("cool.url.com/path"));
    }
}
