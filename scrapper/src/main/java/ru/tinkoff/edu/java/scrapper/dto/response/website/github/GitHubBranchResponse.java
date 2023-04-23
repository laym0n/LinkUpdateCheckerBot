package ru.tinkoff.edu.java.scrapper.dto.response.website.github;

import lombok.*;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;

@Data
@ToString
@EqualsAndHashCode
public class GitHubBranchResponse {
    private String name;
    public GitHubBranch getGitHubBranch(){
        return new GitHubBranch(name);
    }

}
