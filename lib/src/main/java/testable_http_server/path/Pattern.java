package testable_http_server.path;

import java.util.List;

public class Pattern {

    private final List<? extends PatternNode> nodes;

    public Pattern(List<? extends PatternNode> nodes) {
        this.nodes = nodes;
    }

    public boolean match(Path path) {
        final var pathNodes = path.nodes();
        if (nodes.size() > pathNodes.size()) {
            return false;
        }
        for(var i = 0; i < nodes.size(); i++) {
            if (!nodes.get(i).match(pathNodes.get(i))) {
                return false;
            }
            if (nodes.get(i).stopHere()) {
                return true;
            }
        }
        if (pathNodes.size() > nodes.size()) {
            return false;
        }
        return true;
    }
}
