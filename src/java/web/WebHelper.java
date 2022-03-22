package web;

import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class WebHelper {

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    public static Optional<Integer> emptyGet(final String uri) {
        try {
            final HttpRequest request = HttpRequest.newBuilder(URI.create(uri))
                    .GET()
                    .build();

            final HttpResponse<String> response = CLIENT.send(request,  HttpResponse.BodyHandlers.ofString());
            return Optional.of(Integer.parseInt(response.body()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    public static void postSendingStringBody(final String uri, final JsonObject body) {
        final HttpRequest req = HttpRequest.newBuilder(URI.create(uri))
                .setHeader("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body.toString()))
                .build();

        try {
            var response = CLIENT.send(req, HttpResponse.BodyHandlers.discarding());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void emptyPost(final String uri) {
        final HttpRequest req = HttpRequest.newBuilder(URI.create(uri))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            var response = HttpClient.newHttpClient().send(req, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void subscribeOn(final String uri, final Consumer<String> subscriber) {
        CLIENT.newWebSocketBuilder().buildAsync(URI.create(uri), new WebSocket.Listener() {

            private List<CharSequence> chars = new ArrayList<>();
            private CompletableFuture<?> completableFuture = new CompletableFuture<>();

            @Override
            public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                chars.add(data);
                webSocket.request(1);
                if (last) {
                    subscriber.accept(chars.stream()
                            .map(String::valueOf)
                            .collect(Collectors.joining()));
                    chars = new ArrayList<>();
                    completableFuture.complete(null);
                    CompletionStage<?> cf = completableFuture;
                    completableFuture = new CompletableFuture<>();
                    return cf;
                }
                return completableFuture;
            }
        });

    }
}
