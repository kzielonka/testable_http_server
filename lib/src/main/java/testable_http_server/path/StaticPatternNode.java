package testable_http_server.path;

public class StaticPatternNode implements PatternNode {

    private final String name;

    public StaticPatternNode(String name) {
        this.name = name;
    }

    @Override
    public boolean match(PathNode node) {
        return name.equals(node.toString());
    }

    @Override
    public String toRawPatternString() {
        return name;
    }
}
