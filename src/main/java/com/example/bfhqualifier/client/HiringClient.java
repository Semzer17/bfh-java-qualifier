package com.example.bfhqualifier.client;

import com.example.bfhqualifier.config.AppProperties;
import com.example.bfhqualifier.dto.GenerateWebhookRequest;
import com.example.bfhqualifier.dto.GenerateWebhookResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HiringClient {
    private final WebClient webClient;
    private final AppProperties props;

    public HiringClient(AppProperties props) {
        this.props = props;
        this.webClient = WebClient.builder().baseUrl(props.getBaseUrl()).build();
    }

    public GenerateWebhookResponse generateWebhook(String name, String regNo, String email) {
        GenerateWebhookRequest body = new GenerateWebhookRequest(name, regNo, email);
        return webClient.post()
                .uri("/generateWebhook/JAVA")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(GenerateWebhookResponse.class)
                .block();
    }

    public void submitFinalQuery(String webhookUrl, String accessToken, String finalQuery) {
        submit(finalQuery, accessToken, webhookUrl).block();
    }

    public void submitFinalQueryToFallback(String accessToken, String finalQuery) {
        String url = props.getBaseUrl() + "/testWebhook/JAVA";
        submit(finalQuery, accessToken, url).block();
    }

    private Mono<Void> submit(String finalQuery, String accessToken, String url) {
        return WebClient.create()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> { if (accessToken != null && !accessToken.isBlank()) h.set("Authorization", accessToken); })
                .bodyValue(new com.example.bfhqualifier.dto.FinalQueryPayload(finalQuery))
                .retrieve()
                .bodyToMono(String.class)
                .then();
    }
}
