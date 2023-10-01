package testable_http_server;

import com.sun.net.httpserver.HttpExchange;
import testable_http_server.content_type.ParsedContentType;

import java.io.IOException;
import java.util.Objects;

public class Response {

    private final HttpExchange httpExchange;
    private final int code;
    private final boolean chunked;
    private final long contentLength;
    private final ResponseHeaders headers;

    public Response(HttpExchange httpExchange, int code, boolean chunked, long contentLength, ResponseHeaders headers) {
        this.httpExchange = Objects.requireNonNull(httpExchange, "httpExchange must be provided");
        this.code = code;
        this.chunked = chunked;
        this.contentLength = contentLength;
        this.headers = Objects.requireNonNull(headers, "headers must be provided");
    }

    static Response fresh(HttpExchange httpExchange) {
        return new Response(httpExchange, 200, false, 0, ResponseHeaders.empty());
    }

    public Response code(int code) {
        return new Response(httpExchange, code, chunked, contentLength, headers);
    }

    public Response header(String name, String value) {
        final var newHeaders = headers.withHeader(name, value);
        return new Response(httpExchange, code, chunked, contentLength, newHeaders);
    }

    public Response contentType(String value) {
        return this.header("Content-Type", value);
    }

    public Response contentType(ParsedContentType value) { return this.contentType(value.raw().toString()); }

    public Response contentLength(long numberOfBytes) {
        return new Response(httpExchange, code, false, numberOfBytes, headers);
    }

    public ActiveResponse prepareToSend() throws IOException {
        this.headers.applyTo(this.httpExchange.getResponseHeaders());
        this.httpExchange.sendResponseHeaders(code, chunked ? 0 : contentLength);
        return new ActiveResponse(this.httpExchange);
    }

    public void send(ResponseContent content) throws IOException {
        var res = this.contentType(content.contentType());
        if (content.contentLengthKnown()) {
            res = res.contentLength(content.contentLength());
        }
        final var activeResponse = res.prepareToSend();
        content.transferTo(activeResponse.stream());
        activeResponse.close();
    }
}

