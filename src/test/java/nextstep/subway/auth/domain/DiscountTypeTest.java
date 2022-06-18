package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountTypeTest {

    @DisplayName("지하철 어린이 할인 정책 확인")
    @Test
    void ChildDiscountPolicy() {
        // given
        DiscountPolicy policy6 = DiscountType.findDiscountPolicy(new Age(6));
        DiscountPolicy policy12 = DiscountType.findDiscountPolicy(new Age(12));

        // when, then
        assertThat(policy6).isExactlyInstanceOf(ChildDiscountPolicy.class);
        assertThat(policy12).isExactlyInstanceOf(ChildDiscountPolicy.class);
    }

    @DisplayName("지하철 청소년 할인 정책 확인")
    @Test
    void YouthDiscountPolicy() {
        // given
        DiscountPolicy policy13 = DiscountType.findDiscountPolicy(new Age(13));
        DiscountPolicy policy18 = DiscountType.findDiscountPolicy(new Age(18));

        // when, then
        assertThat(policy13).isExactlyInstanceOf(YouthDiscountPolicy.class);
        assertThat(policy18).isExactlyInstanceOf(YouthDiscountPolicy.class);
    }

    @DisplayName("지하철 성인 정책 확인")
    @Test
    void AdultDiscountPolicy() {
        // given
        DiscountPolicy policy = DiscountType.findDiscountPolicy(new Age(19));

        // when, then
        assertThat(policy).isExactlyInstanceOf(AdultDiscountPolicy.class);
    }
}