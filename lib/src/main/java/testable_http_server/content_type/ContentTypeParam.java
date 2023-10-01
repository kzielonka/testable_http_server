package testable_http_server.content_type;

public class ContentTypeParam {

    private final String attribute;
    private final String value;

    public ContentTypeParam(String attribute, String value) {
        this.attribute = attribute;
        this.value = value;
    }

    public boolean is(String attribute) {
        return this.attribute.equals(attribute);
    }

    public String attribute() {
        return this.attribute;
    }

    public String value() {
        return this.value;
    }
}
