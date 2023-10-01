package testable_http_server;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static testable_http_server.RouteHttpConnectionHandler.router;

public class PlainTextParserTest {

    @Test
    void test() {
        final var httpServer = TestableHttpServer.empty()
                .withPort(1000)
                .withHandler(plainTextParser(router()
                        .get("/test", new TestableHttpServerTest.SampleHandler("OK3"))))
                .startTestHttpServer();

        final var request1 = TestRequest.empty().method("POST").path("test").plainTextBody("10");
        final var result1 = httpServer.send(request1);
        assertThat(result1.contentType()).isEqualTo("plain/text");
        assertThat(result1.status).isEqualTo(200);
        assertThat(result1.body).isEqualTo("OK1 10\n");
        assertThat(result1.headers.contentType()).isEqualTo("text/plain; charset=UTF-8");

    }
}
