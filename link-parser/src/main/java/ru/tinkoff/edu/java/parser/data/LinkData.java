package ru.tinkoff.edu.java.parser.data;

public sealed interface LinkData permits
        GitHubLinkData,
        StackOverflowLinkData
{}
