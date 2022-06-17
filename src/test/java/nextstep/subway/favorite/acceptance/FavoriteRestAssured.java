package nextstep.subway.favorite.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.http.MediaType;

public class FavoriteRestAssured {
    public static ExtractableResponse<Response> 즐겨찾기_생성을_요청(String token, StationResponse 강남역, StationResponse 정자역) {
        FavoriteRequest favoriteRequest = FavoriteRequest.of(String.valueOf(강남역.getId()), String.valueOf((정자역.getId())));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(favoriteRequest)
                .when().post("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_목록_조회_요청(String token) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .when().get("/favorites")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 즐겨찾기_삭제_요청(ExtractableResponse<Response> response, String token) {
        String uri = response.header("Location");
        return RestAssured
                .given().log().all()
                .auth().oauth2(token)
                .when().delete(uri)
                .then().log().all()
                .extract();
    }
}