package testable_http_server;

import java.io.*;

public class StartedTestableHttpServer implements StartedServer {

    private HttpConnectionHandler handler;

    public StartedTestableHttpServer(HttpConnectionHandler handler) {
        this.handler = handler;
    }

    public TestResponse2 request(TestRequest testRequest) {
        return new TestResponse2(200, "", ResponseHeaders.empty());
    }

    @Override
    public void stop() {

    }

    public TestResponse2 send(TestRequest request) throws IOException {
        final var testHttpExchange = request.httpExchange();
        final var req = Request.forHttpExchange(testHttpExchange);
        this.handler.handle(req);
        return new TestResponse2(testHttpExchange.rCode, testHttpExchange.outputStream.toString(),
                ResponseHeaders.from(testHttpExchange.getResponseHeaders()));
        //throw new Error("404");
    }
}
