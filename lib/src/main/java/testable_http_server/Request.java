package testable_http_server;

import com.sun.net.httpserver.HttpExchange;

import java.io.InputStream;
import java.net.URI;

public class Request {

    private final HttpExchange httpExchange;
    private final RequestAttributes attrs;

    public Request(HttpExchange httpExchange, RequestAttributes attrs) {
        this.httpExchange = httpExchange;
        this.attrs = attrs;
    }

    static Request forHttpExchange(HttpExchange httpExchange) {
        return new Request(httpExchange, RequestAttributes.empty());
    }

    public URI uri() {
        return this.httpExchange.getRequestURI();
    }

    public String method() {
        return this.httpExchange.getRequestMethod();
    }

    public boolean hasHeader(String name) {
        return this.httpExchange.getRequestHeaders().containsKey(name);
    }

    public String header(String name) {
        if (!hasHeader(name)) {
            throw new NoHeaderError(name);
        }
        return this.httpExchange.getRequestHeaders().getFirst(name);
    }

    public <T> Request contextAttribute(T attr) {
        return new Request(httpExchange, attrs.registerAttribute(attr));

    }

    public <T> boolean hasAttribute(Class<T> clazz) {
        return attrs.hasAttribute(clazz);
    }

    public <T> T attribute(Class<T> clazz) {
        return attrs.attribute(clazz);
    }

    public InputStream body() {
        return this.httpExchange.getRequestBody();
    }

    public Response startResponse() {
        return Response.fresh(httpExchange);
    }

    class NoHeaderError extends RuntimeException {

        public NoHeaderError(String header) {
            super("no header %s exception".formatted(header));
        }
    }
}