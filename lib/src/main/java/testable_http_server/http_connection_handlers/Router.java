package testable_http_server.http_connection_handlers;

import testable_http_server.HttpConnectionHandler;
import testable_http_server.PlainTextContent;
import testable_http_server.Request;

import java.io.IOException;
import java.util.*;

public class Router implements HttpConnectionHandler {

    private final List<RouteHandler> handlers;

    private Router(List<RouteHandler> handlers) {
        this.handlers = List.copyOf(handlers);
    }

    public static Router router() {
        return Router.empty();
    }

    public static Router empty() {
        return new Router(List.of());
    }

    public void handle(Request request) throws IOException {
        for (RouteHandler handler : this.handlers) {
            if (handler.handles(request)) {
                handler.handle(request);
                return;
            }
        }
        request.startResponse().code(404).send(new PlainTextContent("Not found"));
    }

    public Router get(String path, HttpConnectionHandler handler) {
        return route(Method.GET, path, handler);
    }

    public Router post(String path, HttpConnectionHandler handler) {
        return route(Method.POST, path, handler);
    }


    public Router route(AcceptedMethod method, String path, HttpConnectionHandler handler) {
        final var handlers = new ArrayList<RouteHandler>(this.handlers);
        handlers.add(new RouteHandler(method, path, handler));
        return new Router(handlers);
    }

    public Router route(Method method, String path, HttpConnectionHandler handler) {
        return route(new AcceptedMethod(Set.of(method)), path, handler);
    }

    public Router route(String path, Router handler) {
        return route(AcceptedMethod.any(), path + "/*", handler);
    }

    private class RouteHandler implements HttpConnectionHandler {

        public final AcceptedMethod method;
        public final String path;
        public final HttpConnectionHandler handler;

        public RouteHandler(AcceptedMethod method, String path, HttpConnectionHandler handler) {
            this.method = method;
            this.path = path;
            this.handler = handler;
        }

        public boolean handles(Request request) {
            if (path.endsWith("/*")) {
                final var expectedPathBase = basePath(request) + path.replace("/*", "");
                return method.includes(request.method()) &&
                    request.uri().getPath().startsWith(expectedPathBase);
            }
            return method.includes(request.method()) &&
                    Objects.equals(request.uri().getPath(), basePath(request) + path);
        }

        private String basePath(Request request) {
            if (request.hasAttribute(RoutePath.class)) {
                final var routePath = request.attribute(RoutePath.class);
                return routePath.path;
            }
            return "";
        }


        public void handle(Request request) throws IOException {
            final var newBasePath = basePath(request) + path.replace("/*", "");
            final var requestInRoute = request.contextAttribute(new RoutePath(newBasePath));
            this.handler.handle(requestInRoute);
        }
    }

    public static class RoutePath {

        public String path;

        public RoutePath(String path) {
            this.path = path;
        }

    }

    static class AcceptedMethod {

        private final Set<Method> methods;

        public AcceptedMethod(Collection<Method> methods) {
            this.methods = Set.copyOf(methods);
        }

        public static AcceptedMethod any() {
            final var allMethods = Set.of(Method.POST, Method.PUT, Method.GET, Method.DELETE);
            return new AcceptedMethod(allMethods);
        }

        public boolean includes(Method method) {
            return this.methods.contains(method);
        }

        public boolean includes(String method) {
            return this.methods.contains(Method.fromRequestMethod(method));
        }
    }

    public enum Method {
        POST,
        PUT,
        DELETE,
        PATCH,
        GET;

        public static Method fromRequestMethod(String text) {
            if (text.equals("POST")) {
                return Method.POST;
            }
            if (text.equals("PUT")) {
                return Method.PUT;
            }
            if (text.equals("DELETE")) {
                return Method.DELETE;
            }
            if (text.equals("PATCH")) {
                return Method.PATCH;
            }
            if (text.equals("GET")) {
                return Method.GET;
            }
            throw new RuntimeException("unrecognised method");
        }
    }
}
