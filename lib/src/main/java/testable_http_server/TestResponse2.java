package testable_http_server;

public class TestResponse2 {
    int status;
    String body;
    ResponseHeaders headers;

    public TestResponse2(int status, String body, ResponseHeaders headers) {
        this.status = status;
        this.body = body;
        this.headers = headers;
    }

    public String contentType() {
        return headers.contentType();
    }
}

