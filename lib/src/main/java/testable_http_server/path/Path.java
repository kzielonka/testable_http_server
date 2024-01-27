package testable_http_server.path;

import java.util.Arrays;
import java.util.List;

public class Path {

    private final String path;

    public Path(String path) {
        this.path = path;
    }

    public List<PathNode> nodes() {
        return Arrays.stream(path.split("/")).map(PathNode::new).toList();
    }
}
