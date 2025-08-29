package com.example.bfhqualifier.client;

import com.example.bfhqualifier.config.AppProperties;
import com.example.bfhqualifier.dto.GenerateWebhookRequest;
import com.example.bfhqualifier.dto.GenerateWebhookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class HiringClient {
    private static final Logger log = LoggerFactory.getLogger(HiringClient.class);

    private final WebClient webClient;
    private final AppProperties props;

    public HiringClient(AppProperties props) {
        this.props = props;
        this.webClient = WebClient.builder()
                .baseUrl(props.getBaseUrl())
                .build();
    }

    public GenerateWebhookResponse generateWebhook(String name, String regNo, String email) {
        GenerateWebhookRequest body = new GenerateWebhookRequest(name, regNo, email);
        log.debug("Calling /generateWebhook/JAVA...");
        return webClient.post()
                .uri("/generateWebhook/JAVA")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(body))
                .retrieve()
                .bodyToMono(GenerateWebhookResponse.class)
                .doOnNext(r -> log.debug("GenerateWebhookResponse: {}", r))
                .block();
    }

    public void submitFinalQuery(String webhookUrl, String accessToken, String finalQuery) {
        log.info("Submitting to returned webhook URL: {}", webhookUrl);
        submit(finalQuery, accessToken, webhookUrl).block();
    }

    public void submitFinalQueryToFallback(String accessToken, String finalQuery) {
        String url = props.getBaseUrl() + "/testWebhook/JAVA";
        log.info("Submitting to fallback URL: {}", url);
        submit(finalQuery, accessToken, url).block();
    }

    private Mono<Void> submit(String finalQuery, String accessToken, String url) {
        return WebClient.create()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> {
                    if (accessToken != null && !accessToken.isBlank()) {
                        h.set("Authorization", accessToken);
                    }
                })
                .bodyValue(new com.example.bfhqualifier.dto.FinalQueryPayload(finalQuery))
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(resp -> log.info("Submission response: {}", resp))
                .then();
    }
}
