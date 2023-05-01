package ru.tinkoff.edu.java.scrapper.dto.controller;

import jakarta.validation.constraints.NotBlank;

public record RemoveLinkRequest(@NotBlank String link) {
}
