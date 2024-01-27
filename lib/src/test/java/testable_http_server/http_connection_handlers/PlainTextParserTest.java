package testable_http_server.http_connection_handlers;

import org.junit.jupiter.api.Test;
import testable_http_server.*;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static testable_http_server.http_connection_handlers.PlainTextParser.plainTextParser;
import static testable_http_server.http_connection_handlers.Router.router;

public class PlainTextParserTest {

    public static class TestHandler extends SimpleHttpConnectionHandler {

        private final String prefix;

        public TestHandler(String prefix) {
            this.prefix = prefix;
        }

        public void handleWithContext(Request request, PlainTextParser.PlainTextBody plainTextBody) throws IOException {
            final var responseBody = "%s %s copy".formatted(prefix, plainTextBody.body());
            final var plainTextResponseBody = new PlainTextContent(responseBody);
            request.startResponse().send(plainTextResponseBody);
        }
    }

    @Test
    void parses_plain_text_body() throws IOException {
        final var httpServer = TestableHttpServer.empty()
                .withPort(1000)
                .withHandler(
                        plainTextParser(router()
                            .get("/endpoint1", new TestHandler("e1"))
                            .get("/endpoint2", new TestHandler("e2"))))
                .startTestHttpServer();

        final var request1 = TestRequest.empty().method("GET").path("endpoint1").plainTextBody("request body 1");
        final var result1 = httpServer.send(request1);
        assertThat(result1.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result1.status).isEqualTo(200);
        assertThat(result1.body).isEqualTo("e1 request body 1 copy");

        final var request2 = TestRequest.empty().method("GET").path("endpoint2").plainTextBody("request body 2");
        final var result2 = httpServer.send(request2);
        assertThat(result2.contentType()).isEqualTo("text/plain;charset=UTF-8");
        assertThat(result2.status).isEqualTo(200);
        assertThat(result2.body).isEqualTo("e2 request body 2 copy");
    }
}
