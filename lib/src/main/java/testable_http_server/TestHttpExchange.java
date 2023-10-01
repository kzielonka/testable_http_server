package testable_http_server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpPrincipal;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

class TestHttpExchange extends HttpExchange {

    private final String method;
    private final String path;
    public int rCode = 0;
    public long responseLength = 0;
    private final InputStream inputStream;
    public final OutputStream outputStream;
    private final Headers requestHeaders;
    private final Headers responseHeaders;

    public TestHttpExchange(String method, String path, InputStream inputStream, Headers headers) {
        this.method = Objects.requireNonNull(method);
        this.path = Objects.requireNonNull(path);
        this.inputStream = Objects.requireNonNull(inputStream);
        this.outputStream = new ByteArrayOutputStream();
        this.requestHeaders = headers;
        this.responseHeaders = new Headers();
    }

    @Override
    public Headers getRequestHeaders() {
        return this.requestHeaders;
    }

    @Override
    public Headers getResponseHeaders() {
        return this.responseHeaders;
    }

    @Override
    public URI getRequestURI() {
        try {
            return new URI("http://www.example.com/" + this.path);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getRequestMethod() {
        return this.method;
    }

    @Override
    public HttpContext getHttpContext() {
        return null;
    }

    @Override
    public void close() {
        try {
            this.inputStream.close();
            this.outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream getRequestBody() {
        return this.inputStream;
    }

    @Override
    public OutputStream getResponseBody() {
        return this.outputStream;
    }

    @Override
    public void sendResponseHeaders(int rCode, long responseLength) throws IOException {
        this.rCode = rCode;
        this.responseLength = responseLength;
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return null;
    }

    @Override
    public int getResponseCode() {
        return 0;
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return null;
    }

    @Override
    public String getProtocol() {
        return "HTTP";
    }

    @Override
    public Object getAttribute(String name) {
        throw new Error("attributes disabled");
    }

    @Override
    public void setAttribute(String name, Object value) {
        throw new Error("attributes disabled");
    }

    @Override
    public void setStreams(InputStream i, OutputStream o) {
        throw new Error("not supported");
    }

    @Override
    public HttpPrincipal getPrincipal() {
        throw new Error("principal disabled");
    }
}

