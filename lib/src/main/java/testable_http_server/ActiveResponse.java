package testable_http_server;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;

class ActiveResponse {

    private final HttpExchange httpExchange;

    public ActiveResponse(HttpExchange httpExchange) {
        this.httpExchange = httpExchange;
    }

    public OutputStream stream() {
        return this.httpExchange.getResponseBody();
    }

    public void close() {
        this.httpExchange.close();
    }
}