package testable_http_server.content_type;

public class ContentTypeParseError extends RuntimeException {

    public ContentTypeParseError() {
        super("Invalid content type");
    }
}
