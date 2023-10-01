package handlers;

import testable_http_server.HttpConnectionHandler;
import testable_http_server.PlainTextContent;
import testable_http_server.PlainTextRequest;
import testable_http_server.Request;

import java.io.IOException;

public class TextHandler implements HttpConnectionHandler {

    private final String text;

    public TextHandler(String text) {
        this.text = text;
    }

    public static TextHandler text(String text) {
        return new TextHandler(text);
    }

    public void handle(Request request) throws IOException {
        final var content = new PlainTextContent(text);
        request.startResponse().code(200).send(content);
    }
}
