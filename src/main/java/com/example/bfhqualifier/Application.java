package com.example.bfhqualifier;

import com.example.bfhqualifier.client.HiringClient;
import com.example.bfhqualifier.config.AppProperties;
import com.example.bfhqualifier.dto.GenerateWebhookResponse;
import com.example.bfhqualifier.solver.SqlSolver;
import com.example.bfhqualifier.solver.SolverFactory;
import com.example.bfhqualifier.util.RegNoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

@SpringBootApplication
public class Application implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final AppProperties props;
    private final HiringClient client;
    private final SolverFactory solverFactory;

    public Application(AppProperties props, HiringClient client, SolverFactory solverFactory) {
        this.props = props;
        this.client = client;
        this.solverFactory = solverFactory;
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting BFH Qualifier App...");
        log.debug("Using properties: name='{}', regNo='{}', email='{}'",
                props.getName(), props.getRegNo(), props.getEmail());

        // 1) Generate Webhook
        GenerateWebhookResponse resp = client.generateWebhook(props.getName(), props.getRegNo(), props.getEmail());
        log.info("Received accessToken ({} chars) and webhook: {}", 
                 resp.getAccessToken() != null ? resp.getAccessToken().length() : 0,
                 resp.getWebhook());

        // 2) Decide which question based on regNo's last two digits
        int lastTwo = RegNoUtils.lastTwoDigits(props.getRegNo());
        boolean isOdd = (lastTwo % 2) == 1;
        log.info("regNo last two digits = {}, which is {}", lastTwo, isOdd ? "ODD → Q1" : "EVEN → Q2");

        SqlSolver solver = solverFactory.solverFor(isOdd ? "Q1" : "Q2");
        String finalQuery = solver.buildFinalQuery();

        // 3) Persist the final query to a file
        Path out = Path.of("target", "finalQuery.sql");
        Files.createDirectories(out.getParent());
        Files.writeString(out, finalQuery + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        log.info("Saved final SQL to {}", out.toAbsolutePath());

        // 4) Submit to webhook (preferred), or fallback to /testWebhook/JAVA
        String webhook = resp.getWebhook();
        if (webhook == null || webhook.isBlank()) {
            log.warn("No webhook returned; falling back to /testWebhook/JAVA");
            client.submitFinalQueryToFallback(resp.getAccessToken(), finalQuery);
        } else {
            client.submitFinalQuery(webhook, resp.getAccessToken(), finalQuery);
        }

        log.info("Flow completed.");
    }

    @Bean
    public SolverFactory solverFactory() {
        return new SolverFactory();
    }
}
