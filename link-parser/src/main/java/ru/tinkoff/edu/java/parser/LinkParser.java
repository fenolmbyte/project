package ru.tinkoff.edu.java.parser;

import java.net.URI;
import java.net.URISyntaxException;

public class LinkParser {
    public static void main(String[] args) {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        System.out.print("Введите ссылку: ");
        String url = scanner.nextLine();

        LinkHandler handler = new GitHubLinkHandler(new StackOverflowLinkHandler(null)); // Создание цепочки обработчиков
        String result = handler.handle(url); // Обработка ссылки цепочкой обработчиков

        System.out.println(result);
    }

    sealed interface LinkHandler {
        String handle(String url);
    }

    record GitHubLinkHandler(LinkHandler next) implements LinkHandler {
        @Override
        public String handle(String url) {
            try {
                URI uri = new URI(url);
                if (!uri.getHost().equals("github.com")) {
                    return next != null ? next.handle(url) : null;
                }
                String[] pathSegments = uri.getPath().split("/");
                if (pathSegments.length != 3) {
                    return next != null ? next.handle(url) : null;
                }
                return pathSegments[1] + "/" + pathSegments[2];
            } catch (URISyntaxException e) {
                return next != null ? next.handle(url) : null;
            }
        }
    }

    record StackOverflowLinkHandler(LinkHandler next) implements LinkHandler {
        @Override
        public String handle(String url) {
            try {
                URI uri = new URI(url);
                if (!uri.getHost().equals("stackoverflow.com")) {
                    return next != null ? next.handle(url) : null;
                }
                String[] pathSegments = uri.getPath().split("/");
                if (pathSegments.length < 3 || !pathSegments[1].equals("questions")) {
                    return next != null ? next.handle(url) : null;
                }
                return pathSegments[2];
            } catch (URISyntaxException e) {
                return next != null ? next.handle(url) : null;
            }
        }
    }
}