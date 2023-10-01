package testable_http_server;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class RequestAttributesTest {

    class CurrentUserTestType {

        public String username;

        public CurrentUserTestType(String username) {
            this.username = username;
        }
    }

    @Test
    void registers_and_returns_proper_attribute_with_its_class() {
        final var stringAttr = "abcd";
        final var longAttr = Long.valueOf(1234);
        final var currentUserAttr = new CurrentUserTestType("username");
        final var attrs = RequestAttributes.empty()
                .registerAttribute(stringAttr)
                .registerAttribute(longAttr)
                .registerAttribute(currentUserAttr);
        assertThat(attrs.attribute(String.class)).isEqualTo(stringAttr);
        assertThat(attrs.attribute(String.class).getClass()).isEqualTo(String.class);
        assertThat(attrs.attribute(Long.class)).isEqualTo(longAttr);
        assertThat(attrs.attribute(Long.class).getClass()).isEqualTo(Long.class);
        assertThat(attrs.attribute(CurrentUserTestType.class)).isEqualTo(currentUserAttr);
        assertThat(attrs.attribute(CurrentUserTestType.class).getClass()).isEqualTo(CurrentUserTestType.class);
    }

    @Test
    void throws_error_trying_to_register_null() {
        final var attrs = RequestAttributes.empty();
        assertThatThrownBy(() -> attrs.registerAttribute(null))
                .isInstanceOf(java.lang.NullPointerException.class)
                .hasMessage("can not register null");
    }

    @Test
    void throws_error_trying_to_get_null_attribute() {
        final var attrs = RequestAttributes.empty();
        assertThatThrownBy(() -> attrs.attribute(null))
                .isInstanceOf(java.lang.NullPointerException.class)
                .hasMessage("can not be null");
    }

    @Test
    void throws_error_when_attribute_has_not_been_registered_yet() {
        final var stringAttr = "abcd";
        final var longAttr = Long.valueOf(1234);
        final var attrs = RequestAttributes.empty()
                .registerAttribute(stringAttr)
                .registerAttribute(longAttr);
        assertThatThrownBy(() -> attrs.attribute(CurrentUserTestType.class).getClass())
                .isInstanceOf(RequestAttributes.UnregisteredAttributeError.class)
                .hasMessage("unregistered attribute for testable_http_server.RequestAttributesTest$CurrentUserTestType");
    }
}
