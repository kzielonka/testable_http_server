package testable_http_server.http_connection_handlers;

import testable_http_server.HttpConnectionHandler;
import testable_http_server.PlainTextRequest;
import testable_http_server.Request;

import java.io.IOException;

public class PlainTextParser implements HttpConnectionHandler {

    private final HttpConnectionHandler handler;

    private PlainTextParser(HttpConnectionHandler handler) {
        this.handler = handler;
    }

    public static PlainTextParser plainTextParser(HttpConnectionHandler handler) {
        return new PlainTextParser(handler);
    }

    @Override
    public void handle(Request request) throws IOException {
        final var plainTextBody = new PlainTextBody(request);
        final var requestWithContext = request.contextAttribute(plainTextBody);
        handler.handle(requestWithContext);
    }

    public static class PlainTextBody {

        private final PlainTextRequest request;

        public PlainTextBody(Request request) {
            this.request = new PlainTextRequest(request);
        }

        public String body() {
            return request.body();
        }
    }
}
