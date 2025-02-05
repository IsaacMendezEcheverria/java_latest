package com.kousenit.astro;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kousenit.http.AstroResponse;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AstroGateway implements Gateway<AstroResponse> {
    private static final String DEFAULT_URL = "http://api.open-notify.org/astros.json";
    private final String url;

    public AstroGateway() {
        this(DEFAULT_URL);
    }

    public AstroGateway(String url) {
        this.url = url;
    }

    private final HttpClient client = HttpClient.newHttpClient();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Result<AstroResponse> getResponse() {
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .header("Accept", "application/json")
                    .GET() // default (could leave that out)
                    .build();
            HttpResponse<String> httpResponse =
                    client.send(request, HttpResponse.BodyHandlers.ofString());
            return new Success<>(
                    objectMapper.readValue(httpResponse.body(), AstroResponse.class));
        } catch (IOException | InterruptedException e) {
            return new Failure<>(new RuntimeException(e));
        }
    }
}
