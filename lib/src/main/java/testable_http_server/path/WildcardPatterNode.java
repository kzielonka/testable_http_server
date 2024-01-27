package testable_http_server.path;

public class WildcardPatterNode implements PatternNode {

    @Override
    public boolean match(PathNode node) {
        return true;
    }

    @Override
    public boolean stopHere() {
        return true;

    }
}
