package ru.tinkoff.edu.java.scrapper.dto.controller;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, Integer size) {
}
