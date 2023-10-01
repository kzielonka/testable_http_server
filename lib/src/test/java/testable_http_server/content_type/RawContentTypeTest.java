package testable_http_server.content_type;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RawContentTypeTest {

    @Test
    void returns_invalid_content_type() {
        final var raw = new RawContentType("INVALID");
        assertThat(raw.isValid()).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "INVALID", "x;b", "a/b;c"})
    void throws_error_trying_to_parse_invalid_content_type(String contentType) {
        final var raw = new RawContentType(contentType);
        assertThatThrownBy(() -> raw.parse()).isInstanceOf(ContentTypeParseError.class);
    }

    @Test
    void parses_text_plain() {
        final var raw = new RawContentType("plain/text");
        final var parsed = raw.parse();
        assertThat(parsed.type()).isEqualTo("plain");
        assertThat(parsed.subtype()).isEqualTo("text");
        assertThat(parsed.params()).isEmpty();
        assertThat(parsed.numberOfParams()).isEqualTo(0);
    }

    @Test
    void parses_application_test() {
        final var raw = new RawContentType("application/test");
        final var parsed = raw.parse();
        assertThat(parsed.type()).isEqualTo("application");
        assertThat(parsed.subtype()).isEqualTo("test");
        assertThat(parsed.params()).isEmpty();
        assertThat(parsed.numberOfParams()).isEqualTo(0);
    }

    @Test
    void parses_plain_text_with_utf_8_charset() {
        final var raw = new RawContentType("plain/text;charset=utf-8");
        final var parsed = raw.parse();
        assertThat(parsed.type()).isEqualTo("plain");
        assertThat(parsed.subtype()).isEqualTo("text");
        assertThat(parsed.numberOfParams()).isEqualTo(1);
        assertThat(parsed.hasParam("charset")).isTrue();
        assertThat(parsed.param("charset")).isEqualTo("utf-8");
    }

    @Test
    void parses_application_test_with_two_params() {
        final var raw = new RawContentType("application/test;param1=value1;param2=value2");
        final var parsed = raw.parse();
        assertThat(parsed.type()).isEqualTo("application");
        assertThat(parsed.subtype()).isEqualTo("test");
        assertThat(parsed.numberOfParams()).isEqualTo(2);
        assertThat(parsed.hasParam("param1")).isTrue();
        assertThat(parsed.param("param1")).isEqualTo("value1");
        assertThat(parsed.hasParam("param2")).isTrue();
        assertThat(parsed.param("param2")).isEqualTo("value2");
        assertThat(parsed.hasParam("param3")).isFalse();
    }

    @Test
    void params_list_is_immutable() {
        final var raw = new RawContentType("application/test;param1=value1;param2=value2");
        final var parsed = raw.parse();
        assertThatThrownBy(() -> parsed.params().add(new ContentTypeParam("a", "b")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void parses_and_converts_back_to_string() {
        final var rawString = "application/test;param1=value1;param2=value2";
        final var raw = new RawContentType(rawString);
        final var parsed = raw.parse();
        final var rawBack = parsed.raw();
        assertThat(rawBack.toString()).isEqualTo(rawString);
    }

    @Test
    void can_be_compared_with_type_and_subtype_in_string_format() {
        final var rawString = "application/text;param1=value1;param2=value2";
        final var raw = new RawContentType(rawString);
        final var parsed = raw.parse();

        assertThat(parsed.hasSameTypeAndSubtypeAs("application/text")).isTrue();
        assertThat(parsed.hasSameTypeAndSubtypeAs("application/text;param1=attr1;param2=attr2")).isTrue();
        assertThat(parsed.hasSameTypeAndSubtypeAs("application/other")).isFalse();
        assertThat(parsed.hasSameTypeAndSubtypeAs("plain/text")).isFalse();
        assertThat(parsed.hasSameTypeAndSubtypeAs("invalid")).isFalse();
    }
}
