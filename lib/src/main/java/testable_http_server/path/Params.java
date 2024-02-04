package testable_http_server.path;

import java.util.HashMap;
import java.util.Map;

public class Params {

    private final Map<String, String> params;

    private Params(Map<String, String> params) {
        this.params = Map.copyOf(params);
    }

    public static Params empty() {
        return new Params(Map.of());
    }

    public Params withParam(String name, String value) {
        if (params.containsKey(name)) {
            throw new DuplicatedParamException(name);
        }
        final var newParams = new HashMap<String, String>(params);
        newParams.put(name, value);
        return new Params(newParams);
    }

    public String valueOf(String name) {
        final var value = params.get(name);
        if (value == null) {
            return "";
        }
        return value;
    }

    public static class DuplicatedParamException extends RuntimeException {

        public DuplicatedParamException(String paramName) {
            super("param %s has already been set".formatted(paramName));
        }
    }
}
