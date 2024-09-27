package com.gitlabdemo.gittojira;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gitlab")
public class GitLabController {

    private final GitLabIssueService gitLabIssueService;

    public GitLabController(GitLabIssueService gitLabIssueService) {
        this.gitLabIssueService = gitLabIssueService;
    }

    @PostMapping("/create-issue")
    public ResponseEntity<String> createIssue(@RequestParam String projectId, @RequestParam String title, @RequestParam String description) {
        String response = gitLabIssueService.createIssue(projectId, title, description);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/get-issues")
    public ResponseEntity<String> getIssues(@RequestParam String projectId) {
        String response = gitLabIssueService.getIssues(projectId);
        return ResponseEntity.ok(response);
    }
}
