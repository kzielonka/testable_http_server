package testable_http_server.path;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ParamsTest {

    @Test
    void returns_correct_param_value() {
        final var params = Params.empty()
                .withParam("user_id", "user-id-1234")
                .withParam("user_type", "standard")
                .withParam("other_param", "other_value");

        assertThat(params.valueOf("user_id")).isEqualTo("user-id-1234");
        assertThat(params.valueOf("user_type")).isEqualTo("standard");
        assertThat(params.valueOf("other_param")).isEqualTo("other_value");
        assertThat(params.valueOf("non_existing_param")).isEqualTo("");
    }

    @Test
    void copies_its_internal_param_list_each_time_param_is_added() {
        final var paramsBase = Params.empty()
                .withParam("user_id", "user-id-1234")
                .withParam("user_type", "standard")
                .withParam("other_param", "other_value");
        final var params1 = paramsBase.withParam("param1", "value1");
        final var params2 = paramsBase.withParam("param2", "value2");

        assertThat(params1.valueOf("param1")).isEqualTo("value1");
        assertThat(params1.valueOf("param2")).isEqualTo("");
        assertThat(params2.valueOf("param1")).isEqualTo("");
        assertThat(params2.valueOf("param2")).isEqualTo("value2");
    }

    @Test
    void throw_error_for_params_with_duplicated_names() {
        final var params = Params.empty()
                .withParam("param1", "value1")
                .withParam("param2", "value2");

        assertThatThrownBy(() -> params.withParam("param1", "value3"))
                .isInstanceOf(Params.DuplicatedParamException.class)
                .hasMessage("param param1 has already been set");
    }
}
