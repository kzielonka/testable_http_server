package testable_http_server;

interface EndpointHandler {
    String handle();
}

class Endpoint {
    String path;
    EndpointHandler endpointHandler;

    Endpoint(String path, EndpointHandler endpointHandler) {
        this.path = path;
        this.endpointHandler = endpointHandler;
    }
}

public class HttpServer {
    private final int port;
    private final Endpoint[] endpoints;

    HttpServer(int port, Endpoint[] endpoints) {
        this.port = port;
        this.endpoints = endpoints;
    }

    HttpServer withPort(int newPort) {
        return new HttpServer(port, this.endpoints);
    }

    HttpServer withEndpoint(Endpoint handler) {
        Endpoint[] newEndpoints = new Endpoint[this.endpoints.length + 1];
        for(var i = 0; i < this.endpoints.length; i++) {
            newEndpoints[i] = this.endpoints[i];
        }
        newEndpoints[this.endpoints.length] = handler;
        return new HttpServer(this.port, newEndpoints);
    }
}
