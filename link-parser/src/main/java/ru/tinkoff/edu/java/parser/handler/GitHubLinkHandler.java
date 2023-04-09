package ru.tinkoff.edu.java.parser.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.data.GitHubLinkData;
import ru.tinkoff.edu.java.parser.data.LinkData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Order(1)
@Component
public final class GitHubLinkHandler implements LinkHandler {
    private final Pattern pattern = Pattern.compile(
            "^https://github\\.com/([^/]+)/([^/]+)/?$"
    );

    @Override
    public @Nullable LinkData handleLink(@NotNull String link) {
        Matcher matcher = pattern.matcher(link);
        if (!matcher.matches()) return null;
        String username = matcher.group(1);
        String repository = matcher.group(2);
        return new GitHubLinkData(username, repository);
    }
}
