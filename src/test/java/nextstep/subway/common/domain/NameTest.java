package nextstep.subway.common.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("이름")
class NameTest {

    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 객체화")
    @DisplayName("객체화")
    @ValueSource(strings = {"name", "이름"})
    void instance(String name) {
        assertThatNoException()
            .isThrownBy(() -> Name.from(name));
    }

    @NullAndEmptySource
    @DisplayName("이름의 정보는 필수")
    @ParameterizedTest(name = "[{index}] {argumentsWithNames} 으로 객체화 불가능")
    void instance_emptyValue_thrownIllegalArgumentException(String value) {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Name.from(value))
            .withMessage("이름이 공백일 수 없습니다.");
    }

}