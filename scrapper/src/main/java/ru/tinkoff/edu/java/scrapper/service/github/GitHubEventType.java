package ru.tinkoff.edu.java.scrapper.service.github;

public enum GitHubEventType {
    CommitCommentEvent("Commit was committed"),
    PushEvent("Commit was pushed"),
    IssueCommentEvent("Issue was commented"),
    PullRequestReviewCommentEvent("Pull request review was commented"),

    UnknownEvent("Some update happened");

    GitHubEventType(String value) {
        this.description = value;
    }

    private final String description;

    public String getDescription() {
        return description;
    }
}
