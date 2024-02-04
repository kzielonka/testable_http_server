package testable_http_server.http_connection_handlers;

import org.junit.jupiter.api.Test;
import testable_http_server.*;
import testable_http_server.path.Params;

import java.io.IOException;

import static handlers.TextHandler.text;
import static org.assertj.core.api.Assertions.assertThat;
import static testable_http_server.http_connection_handlers.Router.router;

public class RouteTest {

    public static class ParamsTestHandler extends SimpleHttpConnectionHandler {

        @SuppressWarnings("unused")
        public void handleWithContext(Request request, Params params) throws IOException {
            final var userId = params.valueOf("user_id");
            final var userType = params.valueOf("user_type");
            final var carId = params.valueOf("car_id");
            final var responseBody = "params: %s, %s, %s".formatted(userId, userType, carId);
            final var plainTextResponseBody = new PlainTextContent(responseBody);
            request.startResponse().send(plainTextResponseBody);
        }
    }

    @Test
    void returns_404_for_empty_route() throws IOException {
        final var httpServer = TestableHttpServer.empty()
                .withHandler(Router.empty())
                .startTestHttpServer();
        final var result = httpServer.send(TestRequest.empty().method("POST").path("test").plainTextBody("10"));
        assertThat(result.status).isEqualTo(404);
        assertThat(result.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result.body).isEqualTo("Not found");
        assertThat(result.body.length()).isEqualTo(9);
    }

    @Test
    void routes_nested_path() throws IOException {
        final var httpServer = TestableHttpServer.empty()
                .withHandler(router()
                        .route("/users/:id", router()
                            .route("/nested/abcd", router()
                                .get("/cars/:car_id", text("success 1")))
                            .get("/xyz", text("success 2"))))
                .startTestHttpServer();

        final var result1 = httpServer.send(TestRequest.empty().method("GET").path("users/user-id-1234/nested/abcd/cars/car-id-4321"));
        assertThat(result1.status).isEqualTo(200);
        assertThat(result1.body).isEqualTo("success 1");

        final var result2 = httpServer.send(TestRequest.empty().method("GET").path("users/2345/xyz"));
        assertThat(result2.status).isEqualTo(200);
        assertThat(result2.body).isEqualTo("success 2");
    }

    @Test
    void parses_params() throws IOException {
        final var httpServer = TestableHttpServer.empty()
                .withHandler(router()
                        .route("/users/:user_id", router()
                                .route("/user_type/:user_type", router()
                                        .get("/cars/:car_id", new ParamsTestHandler()))))
                .startTestHttpServer();

        final var result1 = httpServer.send(TestRequest.empty().method("GET").path("users/user-id-1234/user_type/premium/cars/car-id-4321"));
        assertThat(result1.status).isEqualTo(200);
        assertThat(result1.body).isEqualTo("params: user-id-1234, premium, car-id-4321");

        final var result2 = httpServer.send(TestRequest.empty().method("GET").path("users/2345/user_type/standard/cars/car-2-id"));
        assertThat(result2.status).isEqualTo(200);
        assertThat(result2.body).isEqualTo("params: 2345, standard, car-2-id");
    }
}
