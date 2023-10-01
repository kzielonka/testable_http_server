package testable_http_server;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RequestAttributes {

    private final Map<Class, Object> attrs;

    public RequestAttributes(Map<Class, Object> attrs) {
        this.attrs = Map.copyOf(attrs);
    }

    public static RequestAttributes empty() {
        return new RequestAttributes(Map.of());
    }

    public <T> RequestAttributes registerAttribute(T obj) {
        Objects.requireNonNull(obj, "can not register null");
        final var map = new HashMap<Class, Object>();
        map.putAll(attrs);
        map.put(obj.getClass(), obj);
        return new RequestAttributes(map);
    }

    public <T> boolean hasAttribute(Class<T> clazz) {
        Objects.requireNonNull(clazz, "can not be null");
        final var attr = attrs.get(clazz);
        return attr != null;
    }

    public <T> T attribute(Class<T> clazz) {
        if (!hasAttribute(clazz)) {
            throw new UnregisteredAttributeError(clazz);
        }
        // TODO : add check
        return (T) attrs.get(clazz);
    }

    public static class UnregisteredAttributeError extends RuntimeException {

        public UnregisteredAttributeError(Class clazz) {
            super("unregistered attribute for %s".formatted(clazz.getName()));
        }
    }
}
