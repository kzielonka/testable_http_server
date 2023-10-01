package testable_http_server;

import testable_http_server.content_type.ParsedContentType;
import testable_http_server.content_type.RawContentType;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class PlainTextContent implements ResponseContent {

    private final String body;

    public PlainTextContent(String body) {
        this.body = body;
    }

    @Override
    public boolean contentLengthKnown() {
        return true;
    }

    @Override
    public long contentLength() {
        return bytes().length;
    }

    @Override
    public ParsedContentType contentType() {
        return new RawContentType("text/plain;charset=%s".formatted(charset().name())).parse();
    }

    @Override
    public void transferTo(OutputStream os) throws IOException {
        os.write(bytes());
        os.flush();
        os.close();
    }

    private Charset charset() {
        return StandardCharsets.UTF_8;
    }

    private byte[] bytes() {
        return this.body.getBytes(charset());
    }
}
