package testable_http_server;

public class Header {

    private final String name;
    private final String value;

    public Header(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public boolean is(String name) {
        return this.name.equals(name);
    }

    public String name() {
        return this.name;
    }

    public String value() {
        return this.value;
    }
}
