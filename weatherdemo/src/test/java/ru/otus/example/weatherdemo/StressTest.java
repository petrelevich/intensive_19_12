package ru.otus.example.weatherdemo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

public class StressTest {
    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    private static final String url = "http://localhost:8080/api/weather";
    private static final String urlNull = "http://localhost:8080/api/weatherNull";

    public static void main(String[] args) {
        var st = new StressTest();
        long start = System.currentTimeMillis();
        IntStream.range(0, 1_000_000).forEach((i) -> st.sendRequest(urlNull));
        long end = System.currentTimeMillis();
        System.out.println("duration:" + (end - start) / 1000);
    }

    private void sendRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create(url))
                    .setHeader("User-Agent", "test")
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            assertThat(response.statusCode()).isEqualTo(200);
            //System.out.println(response.body());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
