package subway.acceptance.auth;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class AuthFixture {
    public static Map<String, String> 로그인_요청_만들기(String email, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        return params;
    }
    public static ExtractableResponse<Response> 로그인_호출(String email, String password){
        var response = AuthSteps.로그인_API(로그인_요청_만들기(email, password));
        return response;
    }

    public static String 로그인_후_토큰_추출(String email, String password) {
        var response = AuthSteps.로그인_API(로그인_요청_만들기(email, password));
        return response.jsonPath().getString("accessToken");
    }
}
