package testable_http_server;

import testable_http_server.content_type.RawContentType;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

public class PlainTextRequest {

    private final Request request;

    public PlainTextRequest(Request request) {
       this.request = request;
    }

    public boolean hasValidContentType() {
        if (!request.hasHeader("Content-Type")) {
            return false;
        }
        final var rawContentType = request.header("Content-Type");
        final var contentType = new RawContentType(request.header("Content-Type")).parse();
        return contentType.hasSameTypeAndSubtypeAs("text/plain");
    }

    public String body() {
        final var rawContentType = request.header("Content-Type");
        final var contentType = new RawContentType(request.header("Content-Type")).parse();
        if (!contentType.hasSameTypeAndSubtypeAs("text/plain")) {
            throw new InvalidContentTypeError(rawContentType);
        }
        final var charset = Charset.forName(contentType.param("charset"));
        BufferedReader in = new BufferedReader(new InputStreamReader(request.body(), charset));
        return in.lines().collect(Collectors.joining());
    }

    public static class InvalidContentTypeError extends RuntimeException {

        public InvalidContentTypeError(String contentType) {
            super("invalid content type: " + contentType);
        }
    }
}
