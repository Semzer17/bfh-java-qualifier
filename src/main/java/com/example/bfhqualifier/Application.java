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

        GenerateWebhookResponse resp = client.generateWebhook(props.getName(), props.getRegNo(), props.getEmail());
        int lastTwo = RegNoUtils.lastTwoDigits(props.getRegNo());
        boolean isOdd = (lastTwo % 2) == 1;
        SqlSolver solver = solverFactory.solverFor(isOdd ? "Q1" : "Q2");
        String finalQuery = solver.buildFinalQuery();

        Path out = Path.of("target", "finalQuery.sql");
        Files.createDirectories(out.getParent());
        Files.writeString(out, finalQuery + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        String webhook = resp.getWebhook();
        if (webhook == null || webhook.isBlank()) {
            client.submitFinalQueryToFallback(resp.getAccessToken(), finalQuery);
        } else {
            client.submitFinalQuery(webhook, resp.getAccessToken(), finalQuery);
        }
    }

    @Bean
    public SolverFactory solverFactory() {
        return new SolverFactory();
    }
}
