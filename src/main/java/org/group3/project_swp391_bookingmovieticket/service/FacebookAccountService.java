package org.group3.project_swp391_bookingmovieticket.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.group3.project_swp391_bookingmovieticket.dto.FacebookAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FacebookAccountService {

    @Value("${facebook.client.id}")
    private String clientId;

    @Value("${facebook.client.secret}")
    private String clientSecret;

    @Value("${facebook.redirect.uri}")
    private String redirectUri;

    @Value("${facebook.link.token}")
    private String tokenLink;

    @Value("${facebook.link.user_info}")
    private String userInfoLink;

    @Autowired
    private RestTemplate restTemplate;

    public String getAccessToken(String code) {
        String url = tokenLink +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&client_secret=" + clientSecret +
                "&code=" + code;

        ResponseEntity<JsonNode> response = restTemplate.getForEntity(url, JsonNode.class);
        return response.getBody().get("access_token").asText();
    }

    public FacebookAccount getUserInfo(String accessToken) {
        String url = userInfoLink + "&access_token=" + accessToken;
        ResponseEntity<FacebookAccount> response = restTemplate.getForEntity(url, FacebookAccount.class);
        return response.getBody();
    }
}
