package testable_http_server.content_type;

import javax.swing.text.AbstractDocument;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParsedContentType {

    private final String type;
    private final String subtype;
    private final List<ContentTypeParam> params;

    public ParsedContentType(String type, String subtype, List<ContentTypeParam> params) {
        this.type = Objects.requireNonNull(type);
        this.subtype = Objects.requireNonNull(subtype);
        this.params = Objects.requireNonNull(params);
    }

    public RawContentType raw() {
        final var rawTypeSubtype = this.type + "/" + this.subtype;
        final var list = new ArrayList<String>();
        list.add(rawTypeSubtype);
        list.addAll(params.stream().map(p -> p.attribute() + "=" + p.value()).toList());
        final var rawString = String.join(";", list);
        return new RawContentType(rawString);
    }

    public String type() {
        return this.type;
    }

    public String subtype() {
        return this.subtype;
    }

    public List<ContentTypeParam> params() {
        return this.params;
    }

    public int numberOfParams() {
        return params.size();
    }

    public boolean hasParam(String attribute) {
        Objects.requireNonNull(attribute);
        return params.stream().filter(param -> param.is(attribute)).count() > 0;
    }

    public String param(String attribute) {
        Objects.requireNonNull(attribute);
        return params.stream().filter(param -> param.is(attribute)).findFirst().get().value();
    }

    public boolean hasSameTypeAndSubtypeAs(String typeAndSubtype) {
        try {
            final var rawContentType = new RawContentType(typeAndSubtype);
            final var contentType = rawContentType.parse();
            return hasSameTypeAndSubtypeAs(contentType);
        } catch(ContentTypeParseError error) {
            return false;
        }
    }

    public boolean hasSameTypeAndSubtypeAs(ParsedContentType contentType) {
        return type().equals(contentType.type()) && subtype().equals(contentType.subtype());
    }
}
