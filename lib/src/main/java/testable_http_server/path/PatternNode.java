package testable_http_server.path;

public interface PatternNode {
    boolean match(PathNode node);
    default Params updatedParams(PathNode node, Params params) {
        return params;
    };
    String toRawPatternString();
    default boolean stopHere() {
        return false;
    }
}
