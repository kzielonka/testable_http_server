package testable_http_server;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class RequestTest {

    @Test
    void build_request_for_test_http_exchange() throws IOException {
        final var httpExchange = TestRequest
                .empty()
                .method("POST")
                .path("test")
                .plainTextBody("body")
                .header("Header1", "Content1")
                .header("Header2", "Content2")
                .httpExchange();
        final var request = Request.forHttpExchange(httpExchange);
        assertThat(request.method()).isEqualTo("POST");
        assertThat(request.uri().getPath()).isEqualTo("/test");
        assertThat(request.body().readAllBytes()).isEqualTo("body".getBytes(StandardCharsets.UTF_8));
        assertThat(request.header("header1")).isEqualTo("Content1");
        assertThat(request.header("header2")).isEqualTo("Content2");
    }
}
