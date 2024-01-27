package testable_http_server.path;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class PathTest {

    @ParameterizedTest
    @MethodSource("testCases")
    void pattern_matches_with(String rawPatternText, String realPath, boolean expectedResult) {
        final var rawPattern = new RawPattern(rawPatternText);
        final var pattern = rawPattern.pattern();
        assertThat(pattern.match(new Path(realPath))).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                Arguments.of("/test", "/test", true),
                Arguments.of("/test", "/abcd", false),
                Arguments.of("/test/*", "/test", false),
                Arguments.of("/test/*", "/test/abcd", true),
                Arguments.of("/test/*", "/test/abcd/abcd", true),
                Arguments.of("/test/*", "/test2/abcd/abcd", false),
                Arguments.of("/users/:id/cars", "/users/1234/cars", true),
                Arguments.of("/users/:id/cars", "/users/1234/carsX", false),
                Arguments.of("/users/:id/cars", "/users/1234/cars/bars", false));
    }
}
