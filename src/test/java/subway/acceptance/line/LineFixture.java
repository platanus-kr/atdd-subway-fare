package subway.acceptance.line;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("NonAsciiCharacters")
public class LineFixture {

    public static Map<String, String> 일호선_요청_만들기(final Long upStationId, final Long downStationId) {
        return generateLineCreateRequest("1호선", "bg-blue-600", upStationId, downStationId, 10L, 10L);
    }

    public static Map<String, String> 이호선_요청_만들기(final Long upStationId, final Long downStationId) {
        return generateLineCreateRequest("2호선", "bg-green-600", upStationId, downStationId, 10L, 10L);
    }

    public static Map<String, String> generateLineCreateRequest(final String name,
                                                                final String color,
                                                                final Long upStationId,
                                                                final Long downStationId,
                                                                final Long distance,
                                                                final Long duration) {
        Map<String, String> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        lineRequest.put("upStationId", String.valueOf(upStationId));
        lineRequest.put("downStationId", String.valueOf(downStationId));
        lineRequest.put("distance", String.valueOf(distance));
        lineRequest.put("duration", String.valueOf(duration));
        return lineRequest;
    }

    public static Map<String, String> generateLineCreateRequest(final String name,
                                                                final String color,
                                                                final Long upStationId,
                                                                final Long downStationId,
                                                                final Long distance,
                                                                final Long duration,
                                                                final Long surcharge) {
        Map<String, String> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        lineRequest.put("upStationId", String.valueOf(upStationId));
        lineRequest.put("downStationId", String.valueOf(downStationId));
        lineRequest.put("distance", String.valueOf(distance));
        lineRequest.put("duration", String.valueOf(duration));
        lineRequest.put("surcharge", String.valueOf(surcharge));
        return lineRequest;
    }

    public static Map<String, String> generateLineModifyRequest(String name,
                                                                String color) {
        Map<String, String> lineRequest = new HashMap<>();
        lineRequest.put("name", name);
        lineRequest.put("color", color);
        return lineRequest;
    }


}
