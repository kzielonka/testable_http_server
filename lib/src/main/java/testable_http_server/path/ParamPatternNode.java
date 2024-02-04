package testable_http_server.path;

public class ParamPatternNode implements PatternNode {

    private final String name;

    public ParamPatternNode(String name) {
       this.name = name;
    }

    @Override
    public boolean match(PathNode node) {
        return true;
    }

    @Override
    public Params updatedParams(PathNode node, Params params) {
        return params.withParam(name, node.toString());
    }

    @Override
    public String toRawPatternString() {
        return ":%s".formatted(name);
    }
}
