package ru.tinkoff.edu.java.scrapper.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
public class GitHubEventResponse {
    private String type;
    @JsonProperty("created_at")
    private OffsetDateTime createdAt;
    private String repoName;

    @JsonProperty("repo")
    private void unpackRepoName(Map<String, Object> repo) {
        this.repoName = (String) repo.get("name");
    }
}
