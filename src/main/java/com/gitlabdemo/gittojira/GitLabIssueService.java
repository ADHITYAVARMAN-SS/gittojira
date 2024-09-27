package com.gitlabdemo.gittojira;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;

@Service
public class GitLabIssueService {

    private static final String GITLAB_API_URL = "https://gitlab.com/api/v4/projects/{projectId}/issues";

    @Value("${gitlab.token}")
    private String privateToken;

    private final RestTemplate restTemplate;
    private final JiraIssueService jiraIssueService;

    public GitLabIssueService(RestTemplateBuilder restTemplateBuilder, JiraIssueService jiraIssueService) {
        this.restTemplate = restTemplateBuilder.build();
        this.jiraIssueService = jiraIssueService;
    }

    public String createIssue(String projectId, String title, String description) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("PRIVATE-TOKEN", privateToken);

        Map<String, Object> body = new HashMap<>();
        body.put("title", title);
        body.put("description", description);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                GITLAB_API_URL.replace("{projectId}", projectId),
                HttpMethod.POST,
                entity,
                String.class
        );

        return response.getBody();
    }

    // New method to get issues from GitLab

    public String getIssues(String projectId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", privateToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        @SuppressWarnings("rawtypes")
        ResponseEntity<List> response = restTemplate.exchange(
                GITLAB_API_URL.replace("{projectId}", projectId),
                HttpMethod.GET,
                entity,
                List.class
        );

        System.out.println(response);

        @SuppressWarnings("unchecked")
        List<Map<String, Object>> issues = response.getBody();

        // Iterate through each issue and create it in Jira
        for (Map<String, Object> issue : issues) {
            String title = (String) issue.get("title");
            String description = (String) issue.get("description");
            try {
                jiraIssueService.createIssueInJira(title, description);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "Issues pushed to Jira successfully!";
    
    }
}

