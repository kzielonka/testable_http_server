package testable_http_server.content_type;

import testable_http_server.PlainTextRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RawContentType {

    private final String raw;

    public RawContentType(String raw) {
        this.raw = Objects.requireNonNull(raw);
    }

    public String toString() {
        return raw;
    }

    public boolean isValid() {
        try {
            parse();
            return true;
        } catch(Exception error) {
            return false;
        }
    }

    public ParsedContentType parse() {
        // https://www.w3.org/Protocols/rfc1341/4_Content-Type.html
        final var splited = this.raw.split(";");
        final var typeAndSubtype = splited[0].split("/");
        if (typeAndSubtype.length != 2) {
            throw new ContentTypeParseError();
        }
        final var type = typeAndSubtype[0];
        final var subtype = typeAndSubtype[1];

        final var params = new ArrayList<ContentTypeParam>();
        for (int i = 1; i < splited.length; i++) {
            final var splitedParam = splited[i].split("=");
            if (splitedParam.length != 2) {
                throw new ContentTypeParseError();
            }
            final var attribute = splitedParam[0];
            final var value = splitedParam[1];
            params.add(new ContentTypeParam(attribute, value));
        }
        return new ParsedContentType(type, subtype, List.copyOf(params));
    }
}

