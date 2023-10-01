package testable_http_server;

import com.sun.net.httpserver.Headers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Set;

public class TestRequest {
    String method;
    String path;
    InputStream inputStream;
    Headers headers;

    public TestRequest(String method, String path, InputStream inputStream, Headers headers) {
        this.method = Objects.requireNonNull(method);
        this.path = Objects.requireNonNull(path);
        this.inputStream = Objects.requireNonNull(inputStream);
        this.headers = Objects.requireNonNull(headers);
    }

    public static TestRequest empty() {
        return new TestRequest("", "", InputStream.nullInputStream(), new Headers());
    }

    public TestRequest method(String method) {
        return new TestRequest(method, path, inputStream, headers);
    }

    public TestRequest path(String path) {
        return new TestRequest(method, path, inputStream, headers);
    }

    public TestRequest inputStream(InputStream is) {
        return new TestRequest(method, path, inputStream, headers);
    }

    public TestRequest header(String name, String value) {
        final var newHeaders = headersCopy();
        newHeaders.add(name, value);
        return new TestRequest(method, path, inputStream, newHeaders);
    }

    public TestRequest plainTextBody(String text) {
        return new TestRequest(method, path,
                new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)),
                headers).header("Content-type", "text/plain;charset=utf-8");
    }

    public TestHttpExchange httpExchange() {
        return new TestHttpExchange(method, path, inputStream, headersCopy());
    }

    private Headers headersCopy() {
        return new Headers(headers);
    }
}

