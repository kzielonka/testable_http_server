package testable_http_server;

import java.io.IOException;

public interface HttpConnectionHandler {
    public void handle(Request request) throws IOException;
}
