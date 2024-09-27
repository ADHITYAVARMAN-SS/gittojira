package com.gitlabdemo.gittojira;


import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class JiraIssueService {

    @Value("${jira.username}")
    private String username;

    @Value("${jira.token}")
    private String jiraToken;

    private final String jiraDataCenterBaseUrl = "http://localhost:8080";
    private final String jiraDataCenterUsername = "adhityavarman.2004"; // Change to your username
    private final String jiraDataCenterPassword = "AV@siva1972";
    private final String jiraDataCenterProjectKey = "PROJECT";



    public String createIssueInJira(String title, String description) throws IOException{

        String auth = jiraDataCenterUsername + ":" + jiraDataCenterPassword;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        String url = jiraDataCenterBaseUrl + "/rest/api/2/issue";

        String jsonPayload = "{\n" +
                "    \"fields\": {\n" +
                "       \"project\": {\n" +
                "          \"key\": \"" + jiraDataCenterProjectKey + "\"\n" +
                "       },\n" +
                "       \"summary\": \"" + title + "\",\n" +
                "       \"description\": \"" + description + "\",\n" +
                "       \"issuetype\": {\n" +
                "          \"name\": \"Task\"\n" + // Adjust the issue type if necessary
                "       }\n" +
                "   }\n" +
                "}";

                CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Authorization", "Basic " + encodedAuth);
        httpPost.setHeader("Content-Type", "application/json");

        StringEntity entity = new StringEntity(jsonPayload);
        httpPost.setEntity(entity);

        CloseableHttpResponse response = client.execute(httpPost);
        String responseBody = EntityUtils.toString(response.getEntity());
        client.close();

        System.out.println(responseBody);

        return responseBody;
    }

}

