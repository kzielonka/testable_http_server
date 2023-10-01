package testable_http_server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class ResponseHeaders {

    private final Set<Header> headers;

    public ResponseHeaders(Collection<Header> headers) {
        this.headers = Set.copyOf(headers);
    }

    static ResponseHeaders empty() {
        return new ResponseHeaders(Set.of());
    }

    static ResponseHeaders from(com.sun.net.httpserver.Headers headers) {
        return new ResponseHeaders(headers.entrySet().stream().map((entry) -> new Header(entry.getKey(), entry.getValue().get(0))).collect(Collectors.toSet()));
    }

    public void applyTo(com.sun.net.httpserver.Headers otherHeaders) {
        for (Header header : this.headers) {
            otherHeaders.set(header.name(), header.value());
        }
    }

    public ResponseHeaders withHeader(String name, String value) {
        final var headers = new ArrayList<Header>(this.headers);
        headers.add(new Header(name, value));
        return new ResponseHeaders(headers);
    }

    public String valueOf(String name) {
        return this.headers
                .stream()
                .filter(header -> header.is(name))
                .findFirst()
                .orElseThrow()
                .value();
    }

    public String contentType() {
        return valueOf("Content-type");
    }
}
