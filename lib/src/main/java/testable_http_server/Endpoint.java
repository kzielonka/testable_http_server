package testable_http_server;

import com.sun.net.httpserver.HttpHandler;

public class Endpoint {
    String method;
    String path;
    HttpConnectionHandler httpHandler;

    Endpoint(String method, String path, HttpConnectionHandler httpHandler) {
        this.method = method;
        this.path = path;
        this.httpHandler = httpHandler;
    }
}
