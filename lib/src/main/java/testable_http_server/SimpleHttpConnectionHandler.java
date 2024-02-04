package testable_http_server;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

abstract public class SimpleHttpConnectionHandler implements HttpConnectionHandler {

    @Override
    public void handle(Request request) throws IOException {
        final var method = handleWithContextMethod();
        final var params = method.getParameters();
        final var attrs = Arrays.stream(params).map(parameter -> {
            if (parameter.getType().equals(Request.class)) {
                return request;
            }
            return request.attribute(parameter.getType());
        }).toArray();
        try {
            method.invoke(this, attrs);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private Method handleWithContextMethod() {
        for (Method method : getClass().getDeclaredMethods()) {
            if (method.getName().equals("handleWithContext")) {
                return method;
            }
        }
        throw new RuntimeException("no method defined");
    }
}
