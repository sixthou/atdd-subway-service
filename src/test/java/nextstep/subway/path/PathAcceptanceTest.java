package nextstep.subway.path;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.auth.acceptance.AuthAcceptanceTest.로그인_되어_있음;
import static nextstep.subway.line.acceptance.LineAcceptanceTest.지하철_노선_등록되어_있음;
import static nextstep.subway.line.acceptance.LineSectionAcceptanceTest.지하철_노선에_지하철역_등록되어_있음;
import static nextstep.subway.member.MemberAcceptanceTest.회원_생성을_요청;
import static nextstep.subway.station.StationAcceptanceTest.지하철역_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("지하철 경로 조회")
public class PathAcceptanceTest extends AcceptanceTest {
    private LineResponse 신분당선;
    private LineResponse 이호선;
    private LineResponse 삼호선;
    private LineResponse 오호선;
    private StationResponse 교대역;
    private StationResponse 강남역;
    private StationResponse 역삼역;
    private StationResponse 신림역;
    private StationResponse 남부터미널역;
    private StationResponse 양재역;
    private StationResponse 판교역;
    private StationResponse 왕십리역;
    private StationResponse 마장역;
    private StationResponse 창동역;

    /**
     * (*3호선*)               (*신분당선*)
     * 교대역    --- 10 ------  강남역 --- 10 ---  역삼역 --- 46 ---  신림역 (*2호선*)
     * |                      |
     * 3                      10
     * |                      |
     * 남부터미널역 ---  2 ------ 양재역
     *                        |
     *                        10
     *                        |
     *                        판교역
     */
    @BeforeEach
    public void setUp() {
        super.setUp();

        교대역 = 지하철역_등록되어_있음("교대역").as(StationResponse.class);
        강남역 = 지하철역_등록되어_있음("강남역").as(StationResponse.class);
        역삼역 = 지하철역_등록되어_있음("역삼역").as(StationResponse.class);
        신림역 = 지하철역_등록되어_있음("신림역").as(StationResponse.class);
        남부터미널역 = 지하철역_등록되어_있음("남부터미널역").as(StationResponse.class);
        양재역 = 지하철역_등록되어_있음("양재역").as(StationResponse.class);
        판교역 = 지하철역_등록되어_있음("판교역").as(StationResponse.class);
        왕십리역 = 지하철역_등록되어_있음("왕십리역").as(StationResponse.class);
        마장역 = 지하철역_등록되어_있음("마장역").as(StationResponse.class);
        창동역 = new StationResponse(99L, "창동역", null, null);

        신분당선 = 지하철_노선_등록되어_있음(LineRequest.of("신분당선", "bg-red-600", 900, 강남역.getId(), 양재역.getId(), 10))
                .as(LineResponse.class);
        이호선 = 지하철_노선_등록되어_있음(LineRequest.of("이호선", "bg-green-600", 0, 교대역.getId(), 강남역.getId(), 10))
                .as(LineResponse.class);
        삼호선 = 지하철_노선_등록되어_있음(LineRequest.of("삼호선", "bg-orange-600", 0, 교대역.getId(), 양재역.getId(), 5))
                .as(LineResponse.class);
        오호선 = 지하철_노선_등록되어_있음(LineRequest.of("오호선", "bg-purple-600", 0, 왕십리역.getId(), 마장역.getId(), 5))
                .as(LineResponse.class);

        지하철_노선에_지하철역_등록되어_있음(삼호선, 교대역, 남부터미널역, 3);
        지하철_노선에_지하철역_등록되어_있음(이호선, 강남역, 역삼역, 10);
        지하철_노선에_지하철역_등록되어_있음(이호선, 역삼역, 신림역, 46);
        지하철_노선에_지하철역_등록되어_있음(신분당선, 양재역, 판교역, 10);
    }

    @DisplayName("최단 경로 10km 이내 조회 - 성공")
    @Test
    void 최단_경로_조회_성공_1() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 양재역.getId());

        // Then 최단 경로 조회됨
        최단_경로_조회_조회됨(response, 5, 1_250);
    }

    @DisplayName("최단 경로 10km~50km 조회 - 성공")
    @Test
    void 최단_경로_조회_성공_2() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 역삼역.getId());

        // Then 최단 경로 조회됨
        최단_경로_조회_조회됨(response, 20, 1_450);
    }

    @DisplayName("최단 경로 50km이상 조회 - 성공")
    @Test
    void 최단_경로_조회_성공_3() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 신림역.getId());

        // Then 최단 경로 조회됨
        최단_경로_조회_조회됨(response, 66, 1_450);
    }

    @DisplayName("최단 경로 10km~50km & 노선 추가 요금 조회 - 성공")
    @Test
    void 최단_경로_조회_성공_4() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 판교역.getId());

        // Then 최단 경로 조회됨
        최단_경로_조회_조회됨(response, 15, 2_250);
    }

    @DisplayName("최단 경로 10km~50km & 로그인 사용자(어린이) & 노선 추가 요금 조회 - 성공")
    @Test
    void 로그인사용자_최단_경로_조회_성공_5() {
        // When 사용자 로그인 되어 있음
        String email = "test@test.com";
        String password = "password";
        회원_생성을_요청(email, password, 10);
        String 사용자_토큰 = 로그인_되어_있음(email, password);
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 로그인사용자_최단_경로_조회_요청(사용자_토큰, 교대역.getId(), 판교역.getId());

        // Then 최단 경로 조회됨
        최단_경로_조회_조회됨(response, 15, 1_300);
    }

    @DisplayName("최단 경로 10km~50km & 로그인 사용자(청소년) & 노선 추가 요금 조회 - 성공")
    @Test
    void 로그인사용자_최단_경로_조회_성공_6() {
        // When 사용자 로그인 되어 있음
        String email = "test@test.com";
        String password = "password";
        회원_생성을_요청(email, password, 17);
        String 사용자_토큰 = 로그인_되어_있음(email, password);
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 로그인사용자_최단_경로_조회_요청(사용자_토큰, 교대역.getId(), 판교역.getId());

        // Then 최단 경로 조회됨
        최단_경로_조회_조회됨(response, 15, 1_870);
    }

    @DisplayName("최단 경로 10km~50km & 로그인 사용자(성인) & 노선 추가 요금 조회 - 성공")
    @Test
    void 로그인사용자_최단_경로_조회_성공_7() {
        // When 사용자 로그인 되어 있음
        String email = "test@test.com";
        String password = "password";
        회원_생성을_요청(email, password, 19);
        String 사용자_토큰 = 로그인_되어_있음(email, password);
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 로그인사용자_최단_경로_조회_요청(사용자_토큰, 교대역.getId(), 판교역.getId());

        // Then 최단 경로 조회됨
        최단_경로_조회_조회됨(response, 15, 2_250);
    }

    @DisplayName("최단 경로 조회 - 실패 (출발역과 도착역이 같은 경우)")
    @Test
    void 최단_경로_조회_실패_1() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 교대역.getId());

        // Then 최단 경로 실패
        최단_경로_조회_실패(response);
    }

    @DisplayName("최단 경로 조회 - 실패 (출발역과 도착역이 연결이 되어 있지 않은 경우)")
    @Test
    void 최단_경로_조회_실패_2() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 마장역.getId());

        // Then 최단 경로 실패
        최단_경로_조회_실패(response);
    }

    @DisplayName("최단 경로 조회 - 실패 (존재하지 않은 출발역이나 도착역을 조회 할 경우)")
    @Test
    void 최단_경로_조회_실패_3() {
        // when 최단 경로 조회 요청
        ExtractableResponse<Response> response = 최단_경로_조회_요청(교대역.getId(), 창동역.getId());

        // Then 최단 경로 실패
        최단_경로_조회_실패(response);
    }

    public static ExtractableResponse<Response> 최단_경로_조회_요청(Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 로그인사용자_최단_경로_조회_요청(String accessToken, Long sourceId, Long targetId) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/paths?source={sourceId}&target={targetId}", sourceId, targetId)
                .then().log().all()
                .extract();
    }

    public static void 최단_경로_조회_조회됨(ExtractableResponse<Response> response, int distance, int fare) {
        PathResponse path = response.as(PathResponse.class);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(path.getDistance()).isEqualTo(distance);
        assertThat(path.getFare()).isEqualTo(fare);
    }

    public static void 최단_경로_조회_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
