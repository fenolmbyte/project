package ru.tinkoff.edu.java.scrapper.dto.controller;

import jakarta.validation.constraints.NotBlank;

public record AddLinkRequest(@NotBlank String link) {
}
