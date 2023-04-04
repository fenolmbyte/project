package ru.tinkoff.edu.java.scrapper.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(
    @NotBlank
    String link
) {}
