package testable_http_server.path;

public interface PatternNode {
    boolean match(PathNode node);
    default boolean stopHere() {
        return false;
    }
}
