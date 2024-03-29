package testable_http_server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class SampleHandler implements HttpHandler {
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().set("Custom-Header-1", "A~HA");
        exchange.sendResponseHeaders(200, "OK\n".length());
        OutputStream os = exchange.getResponseBody();
        os.write("OK\n".getBytes());
        os.flush();
        os.close();
        exchange.close();
    }
}

