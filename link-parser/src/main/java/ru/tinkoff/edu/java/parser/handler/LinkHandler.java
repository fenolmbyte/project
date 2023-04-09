package ru.tinkoff.edu.java.parser.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.tinkoff.edu.java.parser.data.LinkData;

public interface LinkHandler {
    @Nullable LinkData handleLink(@NotNull String link);
}
