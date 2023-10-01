package testable_http_server;

import testable_http_server.content_type.ParsedContentType;

import java.io.IOException;
import java.io.OutputStream;

public interface ResponseContent {
    boolean contentLengthKnown();
    long contentLength();
    ParsedContentType contentType();
    void transferTo(OutputStream os) throws IOException;
}
