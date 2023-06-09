package ru.tinkoff.edu.java.parser.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.tinkoff.edu.java.parser.data.LinkData;
import ru.tinkoff.edu.java.parser.data.StackOverflowLinkData;

public final class StackOverflowLinkHandler implements LinkHandler {
    private final Pattern pattern = Pattern.compile(
        "^https://stackoverflow\\.com/questions/(\\d+)/[^/]+/?$"
    );

    @Override
    public @Nullable LinkData handleLink(@NotNull String link) {
        Matcher matcher = pattern.matcher(link);
        if (!matcher.matches()) {
            return null;
        }
        Integer questionId = Integer.valueOf(matcher.group(1));
        return new StackOverflowLinkData(questionId);
    }
}
