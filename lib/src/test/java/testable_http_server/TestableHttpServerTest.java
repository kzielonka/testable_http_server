package testable_http_server;

import org.junit.jupiter.api.Test;
import testable_http_server.http_connection_handlers.Router;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static testable_http_server.http_connection_handlers.Router.router;

public class TestableHttpServerTest {
    static class SampleHandler implements HttpConnectionHandler {

        private final String text;

        public SampleHandler(String text) {
            this.text = text;
        }

        public void handle(Request request) throws IOException {
            final var plainTextRequest = new PlainTextRequest(request);
            final var body = text + " " + plainTextRequest.body() + "\n";
            final var content = new PlainTextContent(body);
            request.startResponse().code(200).send(content);
        }
    }
    static class Authorized implements HttpConnectionHandler {

        private final HttpConnectionHandler handler;

        public Authorized(HttpConnectionHandler handler) {
            this.handler = handler;
        }

        public void handle(Request request) throws IOException {
            final var authToken = request.header("Authorization");
            if (authToken.equals("Bearer stas-access-token")) {
                this.handler.handle(request.contextAttribute(new CurrentUser("Stas")));
            } else {
                this.handler.handle(request.contextAttribute(new CurrentUser("Anonymous")));
            }
        }

        public static class CurrentUser {

            public final String name;

            public CurrentUser(String name) {
                this.name = name;
            }
        }
    }

    static class ComplexHandler extends SimpleHttpConnectionHandler {

        private final String text;

        public ComplexHandler(String text) {
            this.text = text;
        }

        public void handleWithContext(Request request, Authorized.CurrentUser currentUser) throws IOException {
            final var plainTextRequest = new PlainTextRequest(request);
            final var body = text + " " + plainTextRequest.body() + " " + currentUser.name + "\n";
            final var content = new PlainTextContent(body);
            request.startResponse().code(200).send(content);
        }
    }

    @Test
    void starts_testable_server() throws IOException {
        final var httpServer = TestableHttpServer.empty()
                .withPort(1000)
                .withHandler(router()
                    .post("/test", new SampleHandler("OK1"))
                    .get("/test", new SampleHandler("OK3"))
                    .post("/test2", new SampleHandler("OK2"))
                    .get("/companies/:id/*", router()
                            .get("/products", new SampleHandler("OK4"))))
                .startTestHttpServer();

        final var request1 = TestRequest.empty().method("POST").path("test").plainTextBody("10");
        final var result1 = httpServer.send(request1);
        assertThat(result1.status).isEqualTo(200);
        assertThat(result1.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result1.body).isEqualTo("OK1 10\n");

        final var request2 = TestRequest.empty().method("POST").path("test2").plainTextBody("20");
        final var result2 = httpServer.send(request2);
        assertThat(result2.status).isEqualTo(200);
        assertThat(result2.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result2.body).isEqualTo("OK2 20\n");

        final var request3 = TestRequest.empty().method("GET").path("test").plainTextBody("30");
        final var result3 = httpServer.send(request3);
        assertThat(result3.status).isEqualTo(200);
        assertThat(result3.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result3.body).isEqualTo("OK3 30\n");

        final var request4 = TestRequest.empty().method("GET").path("companies/1234/products").plainTextBody("40");
        final var result4 = httpServer.send(request4);
        assertThat(result4.status).isEqualTo(200);
        assertThat(result4.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result4.body).isEqualTo("OK4 40\n");
    }

    @Test
    void starts_testable_server2() throws IOException {
        final var httpServer = TestableHttpServer.empty()
                .withPort(1000)
                .withHandler(Router.empty()
                        .post("/test",new Authorized(new ComplexHandler("OK1"))))
                .startTestHttpServer();

        final var request1 = TestRequest.empty().method("POST").path("test")
                .header("Authorization", "Bearer stas-access-token")
                .plainTextBody("10");
        final var result1 = httpServer.send(request1);
        assertThat(result1.status).isEqualTo(200);
        assertThat(result1.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result1.body).isEqualTo("OK1 10 Stas\n");

        final var request2 = TestRequest.empty().method("POST").path("test")
                .header("Authorization", "Bearer abcd1234")
                .plainTextBody("10");
        final var result2 = httpServer.send(request2);
        assertThat(result2.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result2.status).isEqualTo(200);
        assertThat(result2.body).isEqualTo("OK1 10 Anonymous\n");
    }
}
