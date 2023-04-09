package ru.tinkoff.edu.java.bot.dto.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record LinkUpdateRequest(
        // TODO: or zero?
        @Positive
        Integer id,
        @NotBlank
        String url,
        @NotBlank
        String description,
        @NotNull
        List<Integer> tgChatsIds
) {}
