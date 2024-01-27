package testable_http_server.path;

public class ParamPatternNode implements PatternNode {

    @Override
    public boolean match(PathNode node) {
        return true;
    }
}
