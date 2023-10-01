package testable_http_server;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static handlers.TextHandler.text;
import static org.assertj.core.api.Assertions.assertThat;
import static testable_http_server.RouteHttpConnectionHandler.router;

public class RouteHttpConnectionHandlerTest {

    @Test
    void returns_404_for_empty_route() throws IOException {
        final var httpServer = TestableHttpServer.empty()
                .withHandler(RouteHttpConnectionHandler.empty())
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

        final var result1 = httpServer.send(TestRequest.empty().method("GET").path("users/:id/nested/abcd/cars/:car_id"));
        assertThat(result1.status).isEqualTo(200);
        assertThat(result1.body).isEqualTo("success 1");

        final var result2 = httpServer.send(TestRequest.empty().method("GET").path("users/:id/xyz"));
        assertThat(result2.status).isEqualTo(200);
        assertThat(result2.body).isEqualTo("success 2");
    }
}
