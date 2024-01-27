package testable_http_server;

import com.sun.net.httpserver.HttpServer;
import testable_http_server.http_connection_handlers.Router;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Objects;


public class TestableHttpServer {
    private final int port;
    private final HttpConnectionHandler handler;

    private TestableHttpServer(int port, HttpConnectionHandler handler) {
        this.port = port;
        this.handler = Objects.requireNonNull(handler);
    }

    public static TestableHttpServer empty() {
        return new TestableHttpServer(80, Router.empty());
    }

    public TestableHttpServer withPort(int newPort) {
        return new TestableHttpServer(newPort, this.handler);
    }

    public TestableHttpServer withHandler(HttpConnectionHandler handler) {
        return new TestableHttpServer(this.port, handler);
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8440), 1024);
        server.createContext("/status", new SampleHandler());
        server.start();
    }

    public StartedTestableHttpServer startTestHttpServer() {
        return new StartedTestableHttpServer(handler);
    }

    public StartedRealHttpServer startRealHttpServer() {
        return new StartedRealHttpServer();
    }
}
