package ru.tinkoff.edu.java.scrapper.dataaccess.impl.jpa.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsExclude;
import org.apache.commons.lang3.builder.HashCodeExclude;
import parserservice.dto.GitHubLinkInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.GitHubInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.WebsiteInfo;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubBranch;
import ru.tinkoff.edu.java.scrapper.entities.websiteinfo.github.GitHubCommit;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Data
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "website_info_id")
@Table(name = "github_info")
public class GitHubInfoEntity extends WebsiteInfoEntity{
    @Column(name = "repository_name")
    private String repositoryName;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "last_activity_date_time")
    private Timestamp lastActiveTime;
    @OneToMany
    @JoinColumn(name = "website_info_id")
    private Collection<GitHubBranchEntity> branches;
    @OneToMany
    @JoinColumn(name = "website_info_id")
    private Collection<GitHubCommitEntity> commits;
    public GitHubInfoEntity(GitHubInfo gitHubInfo){
        super(gitHubInfo);
        this.repositoryName = gitHubInfo.getLinkInfo().repositoryName();
        this.userName = gitHubInfo.getLinkInfo().userName();
        this.lastActiveTime = Timestamp.valueOf(gitHubInfo.getLastActiveTime().toLocalDateTime());
        this.branches = gitHubInfo.getBranches().values().stream()
                .map(i-> new GitHubBranchEntity(i, gitHubInfo.getId())).collect(Collectors.toCollection(ArrayList::new));
        this.commits = gitHubInfo.getCommits().values().stream()
                .map(i-> new GitHubCommitEntity(i, gitHubInfo.getId())).collect(Collectors.toCollection(ArrayList::new));
    }

    public GitHubInfoEntity(Timestamp lastCheckUpdate,String repositoryName, String userName, Timestamp lastActiveTime, Collection<GitHubBranchEntity> branches, Collection<GitHubCommitEntity> commits) {
        super(lastCheckUpdate);
        this.repositoryName = repositoryName;
        this.userName = userName;
        this.lastActiveTime = lastActiveTime;
        this.branches = branches;
        this.commits = commits;
    }

    @Override
    public GitHubInfo getWebsiteInfo() {
        Map<String, GitHubCommit> commitsForResult = commits.stream()
                .map(GitHubCommitEntity::getGitHubCommit)
                .collect(Collectors.toMap(GitHubCommit::getSha, i->i));
        Map<String, GitHubBranch> branchesForResult = branches.stream()
                .map(GitHubBranchEntity::getGitHubBranch)
                .collect(Collectors.toMap(GitHubBranch::getBranchName, i->i));
        GitHubInfo result = new GitHubInfo(
                id,
                OffsetDateTime.of(lastCheckUpdate.toLocalDateTime(), ZoneOffset.MIN),
                new GitHubLinkInfo(userName, repositoryName),
                branchesForResult,
                commitsForResult,
                OffsetDateTime.of(lastActiveTime.toLocalDateTime(), ZoneOffset.MIN)
        );
        return result;
    }
    @Override
    public void setId(int id){
        super.setId(id);
        commits.forEach(i->i.getPrimaryKey().setGitHubInfoId(id));
        branches.forEach(i->i.getPrimaryKey().setGitHubSiteId(id));
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GitHubInfoEntity entity = (GitHubInfoEntity) o;
        return getRepositoryName().equals(entity.getRepositoryName())
                &&
                getUserName().equals(entity.getUserName()) &&
                getLastActiveTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)
                        .equals(entity.getLastActiveTime().toLocalDateTime().truncatedTo(ChronoUnit.SECONDS)) &&
                new HashSet<>(getBranches())
                        .equals(new HashSet<>(entity.getBranches())) &&
                new HashSet<>(getCommits())
                        .equals(new HashSet<>(entity.getCommits()));
    }
}
