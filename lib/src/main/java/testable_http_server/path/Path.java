package testable_http_server.path;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

public class Path {

    private final String path;

    public Path(String path) {
        this.path = path;
    }

    public static Path fromUri(URI uri) {
        return new Path(uri.getPath());
    }

    public List<PathNode> nodes() {
        return Arrays.stream(path.split("/")).map(PathNode::new).toList();
    }
}
