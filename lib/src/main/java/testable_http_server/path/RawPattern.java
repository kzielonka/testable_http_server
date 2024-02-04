package testable_http_server.path;

import java.util.Arrays;
import java.util.List;

public class RawPattern {

    private final String pattern;

    public RawPattern(String pattern) {
        this.pattern = pattern;
    }

    public String toString() {
        return pattern;
    }

    public static RawPattern blank() {
        return new RawPattern("");
    }

    public RawPattern extend(Pattern pattern) {
        return new RawPattern(this.pattern + pattern.raw().toString());
    }

    public Pattern pattern() {
        final List<? extends PatternNode> nodes = Arrays.stream(pattern.split("/"))
                .map(text -> parseRawPatternNode(text)).toList();
        return new Pattern(nodes);
    }

    private PatternNode parseRawPatternNode(String rawPatterNode) {
        if (rawPatterNode.equals("*")) {
            return new WildcardPatterNode();
        }
        if (rawPatterNode.startsWith(":")) {
            return new ParamPatternNode(rawPatterNode.replaceFirst("^:", ""));
        }
        return new StaticPatternNode(rawPatterNode);
    }
}
