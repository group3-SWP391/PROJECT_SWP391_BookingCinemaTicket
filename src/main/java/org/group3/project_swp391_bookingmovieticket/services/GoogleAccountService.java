package org.group3.project_swp391_bookingmovieticket.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.group3.project_swp391_bookingmovieticket.dtos.GoogleAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@Service
public class GoogleAccountService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    @Autowired
    private RestTemplate restTemplate;

    // Lấy access token từ Google
    public String getToken(String code) {
        String url = "https://oauth2.googleapis.com/token";

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);
        form.add("redirect_uri", redirectUri);
        form.add("code", code);
        form.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

        ResponseEntity<JsonNode> response = restTemplate.exchange(
                url, HttpMethod.POST, request, JsonNode.class
        );

        return response.getBody().get("access_token").asText();
    }

    // Lấy thông tin người dùng từ access token
    public GoogleAccount getUserInfo(String accessToken) {
        String url = "https://www.googleapis.com/oauth2/v1/userinfo?access_token=" + accessToken;
        ResponseEntity<GoogleAccount> response = restTemplate.getForEntity(url, GoogleAccount.class);
        System.out.println(response.getBody() + "Lấy thông tin người dùng từ access token");
        return response.getBody();
    }
}
