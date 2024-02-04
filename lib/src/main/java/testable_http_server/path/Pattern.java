package testable_http_server.path;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class Pattern {

    private final List<? extends PatternNode> nodes;

    public Pattern(List<? extends PatternNode> nodes) {
        this.nodes = List.copyOf(nodes);
    }

    public RawPattern raw() {
        return new RawPattern(nodes.stream().map(PatternNode::toRawPatternString)
                .collect(joining("/")));

    }

    public Pattern withNoWildcardAtEnd() {
        if (!nodes.get(nodes.size() - 1).toRawPatternString().equals("*")) {
            return this;
        }
        final var newList = new ArrayList<PatternNode>(nodes);
        newList.remove(newList.size() - 1);
        return new Pattern(newList);
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

    public Params extractParams(Path path) {
        if (!match(path)) {
            throw new RuntimeException("does not match path");
        }
        final var pathNodes = path.nodes();
        var params = Params.empty();
        for(var i = 0; i < nodes.size(); i++) {
            params = nodes.get(i).updatedParams(pathNodes.get(i), params);
        }
        return params;
    }
}
