package testable_http_server;

import com.sun.net.httpserver.Headers;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PlainTextRequestTest {

    @Test
    void parses_utf8_plain_text() {
        final var text = "Hello World!!!";
        final var inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        final var headers = new Headers();
        headers.add("Content-type", "text/plain;charset=utf-8");
        final var httpExchange = new TestHttpExchange("POST", "/test", inputStream, headers);
        final var request = Request.forHttpExchange(httpExchange);
        final var plainTextRequest = new PlainTextRequest(request);
        assertThat(plainTextRequest.body()).isEqualTo(text);
    }

    @Test
    void throws_error_trying_to_parse_non_plain_text_request() {
        final var text = "Hello World!!!";
        final var inputStream = new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
        final var headers = new Headers();
        headers.add("Content-type", "text/html;charset=utf-8");
        final var httpExchange = new TestHttpExchange("POST", "/test", inputStream, headers);
        final var request = Request.forHttpExchange(httpExchange);
        final var plainTextRequest = new PlainTextRequest(request);
        assertThatThrownBy(() -> assertThat(plainTextRequest.body()))
                .isInstanceOf(PlainTextRequest.InvalidContentTypeError.class)
                .hasMessage("invalid content type: text/html;charset=utf-8");

    }
}
