package com.example.bfhqualifier.dto;

public class GenerateWebhookResponse {
    private String webhook;
    private String accessToken;

    public GenerateWebhookResponse() {}

    public GenerateWebhookResponse(String webhook, String accessToken) {
        this.webhook = webhook;
        this.accessToken = accessToken;
    }

    public String getWebhook() { return webhook; }
    public void setWebhook(String webhook) { this.webhook = webhook; }

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    @Override
    public String toString() {
        return "GenerateWebhookResponse{webhook='%s', accessToken(len)=%d}"
                .formatted(webhook, accessToken != null ? accessToken.length() : 0);
    }
}
